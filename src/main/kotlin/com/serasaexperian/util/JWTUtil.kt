package com.serasaexperian.util

import com.global_money.security.SecurityConstants.SECRET
import com.google.gson.Gson
import com.serasaexperian.dto.LoginDTO
import io.jsonwebtoken.*
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.Subject
import javax.xml.bind.DatatypeConverter


fun parse(key: String?): String {
    return Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
        .parseClaimsJws(key).body.subject
}

fun create(hashSubject: Subject): String {
    val id = UUID.randomUUID().toString().replace("-", "")
    return Jwts.builder()
        .setId(id)
        .setSubject(Gson().toJson(hashSubject))
        .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
        .setNotBefore(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
        .setExpiration(Date.from(LocalDateTime.now().plusDays(2L).toInstant(ZoneOffset.UTC)))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
}

fun create(loginDTO: LoginDTO): String {
    val id = UUID.randomUUID().toString().replace("-", "")
    return Jwts.builder()
        .setId(id)
        .setSubject(Gson().toJson(loginDTO))
//        .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
//        .setNotBefore(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
//        .setExpiration(Date.from(LocalDateTime.now().plusDays(1L).toInstant(ZoneOffset.UTC)))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
}

fun isTokenValid(key: String): Boolean {
    val secretKeySpec = SecretKeySpec(SECRET.encodeToByteArray(), SignatureAlgorithm.HS256.jcaName)
    val validator = DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, secretKeySpec)
    return !validator.isValid(SECRET, key)
}

fun isTokenExpired(key: String): Boolean {
    val expiresAt = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
        .parseClaimsJws(key).body.expiration
    return expiresAt.before(Date())
}
