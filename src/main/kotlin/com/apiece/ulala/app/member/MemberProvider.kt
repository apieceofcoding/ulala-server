package com.apiece.ulala.app.member

enum class MemberProvider(
    private val provider: String,
) {
    KAKAO("kakao"),
    ULALA("ulala");

    companion object {
        fun of(provider: String): MemberProvider {
            return entries.find { it.provider == provider }
                ?: throw IllegalStateException("Provider $provider not found")
        }
    }
}
