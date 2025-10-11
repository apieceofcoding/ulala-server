package com.apiece.ulala.app.member

interface MemberIdGenerator {

    fun generate(length: Int = 20): String
}
