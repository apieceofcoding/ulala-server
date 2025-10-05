package com.apiece.ulala.adapter.generator

import com.apiece.ulala.app.db.IdGenerator
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val log = KotlinLogging.logger {}

/**
 * Snowflake ID 64bit Structure
 *
 * [Sign(1)] [Timestamp(41)] [Datacenter(5)] [Worker(5)] [Sequence(12)]
 *
 * - Sign bit (1bit)        : 항상 0 (양수 보장)
 * - Timestamp (41bit)      : epoch 이후 경과 시간 (ms 단위)
 * - Datacenter ID (5bit)   : 데이터센터/클러스터 구분 (0~31)
 * - Worker ID (5bit)       : 데이터센터 내 워커/노드 구분 (0~31)
 * - Sequence (12bit)       : 같은 ms 내에서 0~4095까지 증가 (최대 4096개)
 *
 * 즉, 한 밀리초에 최대 32 * 32 * 4096 = 약 420만 개의 ID 생성 가능
 * 전체 유효 기간은 epoch 기준 약 69년
 */
@Component
class SnowflakeIdGenerator(
    private val workerId: Long = 0,
    private val datacenterId: Long = 0,
) : IdGenerator {

    init {
        require(workerId in 0..MAX_WORKER_ID) {
            "workerId must be between 0 and $MAX_WORKER_ID (got $workerId)"
        }
        require(datacenterId in 0..MAX_DATACENTER_ID) {
            "datacenterId must be between 0 and $MAX_DATACENTER_ID (got $datacenterId)"
        }
    }

    companion object {
        private const val EPOCH = 1735689600000L // 2025-01-01 00:00:00 UTC
        private const val WORKER_ID_BITS = 5L
        private const val DATACENTER_ID_BITS = 5L
        private const val SEQUENCE_BITS = 12L

        private const val MAX_WORKER_ID = -1L xor (-1L shl WORKER_ID_BITS.toInt())
        private const val MAX_DATACENTER_ID = -1L xor (-1L shl DATACENTER_ID_BITS.toInt())
        private const val SEQUENCE_MASK = -1L xor (-1L shl SEQUENCE_BITS.toInt())

        private const val WORKER_ID_SHIFT = SEQUENCE_BITS
        private const val DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS
        private const val TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS
    }

    private val lock = ReentrantLock()
    private var sequence = 0L
    private var lastTimestamp = System.currentTimeMillis()

    override fun nextId(): Long = lock.withLock {
        var timestamp = System.currentTimeMillis()

        if (timestamp < lastTimestamp) {
            log.warn { "서버 시간이 과거로 흘렀습니다." }
            timestamp = lastTimestamp + 1
        }

        // 동일 ms 라면 시퀀스를 4096 까지 발급하여 고유값 생성
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) and SEQUENCE_MASK
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0L
        }

        lastTimestamp = timestamp

        return ((timestamp - EPOCH) shl TIMESTAMP_LEFT_SHIFT.toInt()) or
                (datacenterId shl DATACENTER_ID_SHIFT.toInt()) or
                (workerId shl WORKER_ID_SHIFT.toInt()) or
                sequence
    }

    // 시퀀스가 4096을 넘으면(=0으로 롤오버) 다음 ms가 될 때까지 대기해 초당 수백만 건을 안전하게 처리
    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = System.currentTimeMillis()
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis()
        }
        return timestamp
    }
}
