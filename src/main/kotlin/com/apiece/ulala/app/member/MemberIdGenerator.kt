package com.apiece.ulala.app.member

interface UsernameGenerator {

    fun generate(length: Int = 20): String
}
