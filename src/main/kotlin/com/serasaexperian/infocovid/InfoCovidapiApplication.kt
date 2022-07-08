package com.serasaexperian.infocovid

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = io.swagger.v3.oas.annotations.info.Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
class InfoCovidapiApplication

fun main(args: Array<String>) {
    runApplication<InfoCovidapiApplication>(*args)
}
