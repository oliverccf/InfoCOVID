package com.serasaexperian.infocovid.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurity {

    @Autowired
    private lateinit var securityContextRepository: ServerSecurityContextRepository

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity
    ): SecurityWebFilterChain? {
        return http
            .cors().disable().csrf().disable()
            .csrf().disable()
            .authorizeExchange {
                it.anyExchange().permitAll()
            }.securityContextRepository(securityContextRepository)
            .build()
    }

}

