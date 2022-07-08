package com.global_money.security

object SecurityConstants {
    const val SECRET = "SecretKeyToGenJWTs"
    const val EXPIRATION_TIME: Long = 864000000 // 10 days
    const val HEADER_STRING = "Authorization"
    const val TOKEN_PREFIX = "Bearer "
    const val SIGNUP = "/auth/signup"
    const val PASSWORD_RECOVERY = "/auth/password-recovery"
    const val ACIVATE = "/auth/activate"
    const val SIGNIN = "/auth/signin"
    const val CHANGE_PASSWORD = "/auth/change-password"
}