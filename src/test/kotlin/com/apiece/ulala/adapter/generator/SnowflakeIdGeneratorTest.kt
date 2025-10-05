package com.apiece.ulala.adapter.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class SnowflakeIdGeneratorTest {

    @Test
    fun `양수 ID가 생성되어야 한다`() {
        val generator = SnowflakeIdGenerator()
        val id = generator.nextId()
        println(id)

        assertThat(id).isGreaterThan(0)
    }

    @Test
    fun `연속된 ID는 고유해야 한다`() {
        val generator = SnowflakeIdGenerator()
        val ids = (1..1000).map { generator.nextId() }.toSet()

        assertThat(ids).hasSize(1000)
    }

    @Test
    fun `생성된 ID는 증가해야 한다`() {
        val generator = SnowflakeIdGenerator()
        val ids = (1..100).map { generator.nextId() }

        assertThat(ids).isSorted
    }

    @Test
    fun `workerId와 datacenterId가 범위를 벗어나면 예외가 발생해야 한다`() {
        assertThrows<IllegalArgumentException> {
            SnowflakeIdGenerator(workerId = 32)
        }

        assertThrows<IllegalArgumentException> {
            SnowflakeIdGenerator(datacenterId = 32)
        }

        assertThrows<IllegalArgumentException> {
            SnowflakeIdGenerator(workerId = -1)
        }

        assertThrows<IllegalArgumentException> {
            SnowflakeIdGenerator(datacenterId = -1)
        }
    }

    @Test
    fun `다른 workerId를 가진 생성기는 서로 다른 ID를 생성해야 한다`() {
        val generator1 = SnowflakeIdGenerator(workerId = 0)
        val generator2 = SnowflakeIdGenerator(workerId = 1)

        val ids1 = (1..100).map { generator1.nextId() }.toSet()
        val ids2 = (1..100).map { generator2.nextId() }.toSet()

        assertThat(ids1.intersect(ids2)).isEmpty()
    }

    @Test
    fun `다른 datacenterId를 가진 생성기는 서로 다른 ID를 생성해야 한다`() {
        val generator1 = SnowflakeIdGenerator(datacenterId = 0)
        val generator2 = SnowflakeIdGenerator(datacenterId = 1)

        val ids1 = (1..100).map { generator1.nextId() }.toSet()
        val ids2 = (1..100).map { generator2.nextId() }.toSet()

        assertThat(ids1.intersect(ids2)).isEmpty()
    }

    @Test
    fun `멀티스레드 환경에서 ID는 고유해야 한다`() {
        val generator = SnowflakeIdGenerator()
        val threadCount = 10
        val idsPerThread = 1000
        val ids = ConcurrentHashMap.newKeySet<Long>()
        val latch = CountDownLatch(threadCount)
        val executor = Executors.newFixedThreadPool(threadCount)

        repeat(threadCount) {
            executor.submit {
                try {
                    repeat(idsPerThread) {
                        ids.add(generator.nextId())
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executor.shutdown()

        assertThat(ids).hasSize(threadCount * idsPerThread)
    }

    @Test
    fun `같은 밀리초에 여러 ID를 생성할 수 있어야 한다`() {
        val generator = SnowflakeIdGenerator()
        val threadCount = 10
        val idsPerThread = 500
        val totalIds = threadCount * idsPerThread // 5000개
        val ids = ConcurrentHashMap.newKeySet<Pair<Long, Long>>() // <ID, timestamp>
        val latch = CountDownLatch(threadCount)
        val executor = Executors.newFixedThreadPool(threadCount)

        // 워밍업: JIT 컴파일 및 캐시 워밍
        repeat(1000) { generator.nextId() }

        // 멀티스레드로 동시에 ID 생성하여 같은 밀리초에 많은 ID 생성 유도
        repeat(threadCount) {
            executor.submit {
                try {
                    repeat(idsPerThread) {
                        val timestamp = System.currentTimeMillis()
                        val id = generator.nextId()
                        ids.add(Pair(id, timestamp))
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executor.shutdown()

        // 모든 ID는 고유해야 함
        val uniqueIds = ids.map { it.first }.toSet()
        assertThat(uniqueIds).hasSize(totalIds)

        // 같은 밀리초에 여러 ID가 생성되었는지 확인
        val timestampGroups = ids.groupBy { it.second }
        val maxIdsInSameMs = timestampGroups.values.maxOfOrNull { it.size } ?: 0

        assertThat(maxIdsInSameMs)
            .withFailMessage("같은 밀리초에 생성된 ID 개수: $maxIdsInSameMs (기대값: 100 이상)")
            .isGreaterThanOrEqualTo(100)
    }

    @Test
    fun `최대 workerId와 datacenterId로 ID를 생성할 수 있어야 한다`() {
        val generator = SnowflakeIdGenerator(workerId = 31, datacenterId = 31)
        val id = generator.nextId()

        assertThat(id).isGreaterThan(0)
    }

    @Test
    fun `생성된 ID에서 workerId와 datacenterId를 추출할 수 있어야 한다`() {
        val workerId = 15L
        val datacenterId = 10L
        val generator = SnowflakeIdGenerator(workerId = workerId, datacenterId = datacenterId)
        val id = generator.nextId()

        val extractedWorkerId = (id shr 12) and 0x1F
        val extractedDatacenterId = (id shr 17) and 0x1F

        assertThat(extractedWorkerId).isEqualTo(workerId)
        assertThat(extractedDatacenterId).isEqualTo(datacenterId)
    }
}
