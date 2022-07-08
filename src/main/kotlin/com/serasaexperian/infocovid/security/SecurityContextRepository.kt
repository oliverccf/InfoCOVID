package com.serasaexperian.infocovid.security

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.serasaexperian.dto.LoginDTO
import com.serasaexperian.exceptions.DisabledUserException
import com.serasaexperian.infocovid.models.User
import com.serasaexperian.infocovid.repositories.UserReactiveRepository
import com.serasaexperian.util.isTokenExpired
import com.serasaexperian.util.isTokenValid
import com.serasaexperian.util.parse
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.SignatureException
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException


@Repository
class SecurityContextRepository : ServerSecurityContextRepository {


    @Autowired
    private lateinit var userReactiveRepository: UserReactiveRepository

    override fun save(
        exchange: ServerWebExchange?,
        context: org.springframework.security.core.context.SecurityContext?
    ): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun load(swe: ServerWebExchange): Mono<org.springframework.security.core.context.SecurityContext>? {
        val request: ServerHttpRequest = swe.request
        if (request.getURI().path.contains("/token")) {
           return Mono.just(SecurityContextImpl(UsernamePasswordAuthenticationToken("", "", arrayListOf())))
        }
        val authHeader: String? = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        var authToken: String? = null
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            authToken = authHeader.replace(TOKEN_PREFIX, "")
        } else {
            logger.warn("couldn't find bearer string, will ignore the header.")
        }
        try {
            val login =
                if (isTokenValid(authToken ?: "")) {
                    parse(authToken)
                } else {
                    ""
                }
            return if (StringUtils.isNotEmpty(login)) {
                val loginDTO = GsonBuilder().create().fromJson(login, LoginDTO::class.java)
                val grantedAuthority = SimpleGrantedAuthority("ADMINISTRATOR")
                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    loginDTO, authToken, arrayListOf(grantedAuthority)
                )
                Mono.just(
                    SecurityContextImpl(
                        authentication
                    )
                )
            } else {
                Mono.empty()
            }
        } catch (e: DisabledUserException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: ExpiredJwtException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        } catch (e: MalformedJwtException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        } catch (e: SignatureException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        } catch (e: JsonSyntaxException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        } catch (e: CancellationException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: ExecutionException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: InterruptedException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun checkUser(user: User): Unit {
        if (!user.enabled) {
            throw DisabledUserException("Disabled User")
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SecurityContextRepository::class.java)
        private const val TOKEN_PREFIX = "Bearer "
    }
}