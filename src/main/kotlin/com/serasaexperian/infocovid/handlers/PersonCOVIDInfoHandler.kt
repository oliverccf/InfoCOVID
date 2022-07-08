package com.serasaexperian.infocovid.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.serasaexperian.constants.*
import com.serasaexperian.infocovid.models.PessoaCOVIDInfo
import com.serasaexperian.infocovid.repositories.PersonCOVIDInfoRepository
import org.apache.logging.log4j.util.Strings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


@Component
class PersonCOVIDInfoHandler {

    @Autowired
    lateinit var personCOVIDInfoRepository: PersonCOVIDInfoRepository

    fun find(request: ServerRequest): Flux<PessoaCOVIDInfo> {
        return personCOVIDInfoRepository.findAll()
    }

    fun create(request: ServerRequest): Mono<PessoaCOVIDInfo> {
        val personCOVIDInfo = parseJsonToPersonCOVIDInfo(request = request, value = PERSON_COVID_INFO)
        return personCOVIDInfoRepository.save(personCOVIDInfo)
    }

    fun update(request: ServerRequest): Boolean {
        val personCOVIDInfo = parseJsonToPersonCOVIDInfo(request = request, value = PERSON_COVID_INFO)
        if (Strings.isNotEmpty(personCOVIDInfo.id)) {
            val personCOVIDInfoUpdated = personCOVIDInfoRepository.save(personCOVIDInfo).map { it }.block()
            return personCOVIDInfo != personCOVIDInfoUpdated
        } else {
            throw InvalidPropertiesFormatException(INVALID_ARGUMENT)
        }
    }

    fun delete(request: ServerRequest): Mono<Void> {
        val id = attributeToString(request = request, value = PERSON_COVID_INFO_ID)
        if (Strings.isNotEmpty(id)) {
            return personCOVIDInfoRepository.deleteById(id)
        } else {
            throw throw InvalidPropertiesFormatException(INVALID_ARGUMENT)
        }
    }

    fun parseJsonToPersonCOVIDInfo(request: ServerRequest, value: String): PessoaCOVIDInfo {
        return ObjectMapper().readValue(request.attribute(value).orElseThrow { ClassNotFoundException(OBJECT_NOT_FOUND) }.toString(), PessoaCOVIDInfo::class.java)
    }

    fun attributeToString(request: ServerRequest, value: String): String {
        return ObjectMapper().readValue(request.attribute(value).orElseThrow { ClassNotFoundException(
            ATTRIBUTE_NOT_FOUND
        ) }.toString(), String::class.java)
    }

}