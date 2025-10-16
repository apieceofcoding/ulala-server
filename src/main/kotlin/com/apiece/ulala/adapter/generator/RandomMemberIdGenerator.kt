package com.apiece.ulala.adapter.generator

import com.apiece.ulala.app.member.UsernameGenerator
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class RandomUsernameGenerator : UsernameGenerator {

    override fun generate(length: Int): String {
        val random = SecureRandom()
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
        val sb = StringBuilder()
        repeat(length) {
            val index = random.nextInt(chars.length)
            sb.append(chars[index])
        }
        return sb.toString()
    }
}
