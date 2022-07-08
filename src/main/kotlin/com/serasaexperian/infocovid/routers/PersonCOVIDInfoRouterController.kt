package com.serasaexperian.infocovid.routers

import com.serasaexperian.constants.*
import com.serasaexperian.dto.LoginDTO
import com.serasaexperian.dto.PersonCOVIDInfoDTO
import com.serasaexperian.infocovid.models.PessoaCOVIDInfo
import com.serasaexperian.infocovid.repositories.PersonCOVIDInfoRepository
import com.serasaexperian.util.create
import org.apache.logging.log4j.util.Strings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
class PersonCOVIDInfoRouterController {

    @Autowired
    lateinit var personCOVIDInfoRepository: PersonCOVIDInfoRepository

    @GetMapping("/token/{email}")
    fun getJWTToken(@PathVariable email: String): Mono<ServerResponse> {
        val token = create(LoginDTO(email = email))
        return ServerResponse.ok().bodyValue(Token(token = token))
    }

    class Token(token: String = "") {
    }

    @GetMapping("/personCOVIDInfos")
    fun getPersonCOVIDInfos(): Flux<PersonCOVIDInfoDTO> {
        return personCOVIDInfoRepository.findAll().map {
            PersonCOVIDInfoDTO(
                id = it.id,
                cpf = it.cpf,
                email = it.email,
                nome = it.nome,
                vacinado = it.vacinado
            )
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/personCOVIDInfos/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postPersonCOVIDInfos(
        @PathVariable id: String,
        @Validated @RequestBody personCOVIDInfoDTO: PersonCOVIDInfoDTO
    ): Mono<ServerResponse> {
       return Mono.just(personCOVIDInfoDTO).map {
            PessoaCOVIDInfo(
                id = null,
                cpf = it.cpf,
                email = it.email,
                nome = it.nome,
                vacinado = it.vacinado
            )

        }.flatMap {
            val message = it.validaCampos()
            if (Strings.isNotEmpty(message)) {
                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message, String::class.java)
            }

            personCOVIDInfoRepository.save(it).flatMap { ServerResponse.ok().body(INFORMACOES_GRAVADAS_COM_SUCESSO, String::class.java) }
                .switchIfEmpty {  ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERRO_INTERNO_NO_SERVIDOR, String::class.java) }

        }.switchIfEmpty { ServerResponse.ok().bodyValue(INFORMACOES_GRAVADAS_COM_SUCESSO) }

    }

    @PutMapping("/personCOVIDInfos")
    fun putPersonCOVIDInfos(@RequestBody personCOVIDInfoDTO: PersonCOVIDInfoDTO): Mono<ServerResponse> {
        if (Strings.isNotEmpty(personCOVIDInfoDTO.id)) {
            val personCOVIDInfo = PessoaCOVIDInfo(
                id = personCOVIDInfoDTO.id,
                cpf = personCOVIDInfoDTO.cpf,
                email = personCOVIDInfoDTO.email,
                nome = personCOVIDInfoDTO.nome,
                vacinado = personCOVIDInfoDTO.vacinado
            )
            val message = personCOVIDInfo.validaCampos()
            if (Strings.isEmpty(message)) {
                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message, String::class.java)
            }
            val personCOVIDInfoUpdated = personCOVIDInfoRepository.save(personCOVIDInfo).map { it }.block()
            if (personCOVIDInfo != personCOVIDInfoUpdated) {
                return ServerResponse.ok().body(INFOMACOES_ATUALIZADAS_COM_SUCESSO, String::class.java)
            }
        }
        return ServerResponse.badRequest().body(INVALID_ARGUMENT, String::class.java)
    }

    @DeleteMapping("/personCOVIDInfos/{id}")
    fun deletePersonCOVIDInfos(@PathVariable id: String): Mono<ServerResponse> {
        return if (Strings.isNotEmpty(id)) {
            personCOVIDInfoRepository.deleteById(id).flatMap {
                ServerResponse.ok().body(EXCLUSAO_REALIZADA_COM_SUCESSO, String::class.java)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(OBJECT_NOT_FOUND))
        } else {
            return ServerResponse.badRequest().bodyValue(INVALID_ARGUMENT)
        }
    }

    // Não consegui habilitar o swagger com reactive functions usando springdoc-openapi. Ver com calma depois.
//    @Bean
//    @RouterOperations(value = [RouterOperation(path = "/personCOVIDInfos", beanClass = PersonCOVIDInfoRouter::class, beanMethod = "getAll"),
//        RouterOperation(path = "/personCOVIDInfos", beanClass = PersonCOVIDInfoHandler::class, beanMethod = "post"),
//        RouterOperation(path = "/personCOVIDInfos", beanClass = PersonCOVIDInfoHandler::class, beanMethod = "update"),
//        RouterOperation(path = "/personCOVIDInfos", beanClass = PersonCOVIDInfoHandler::class, beanMethod = "delete")])
//    fun router(personCOVIDInfoHandler: PersonCOVIDInfoHandler): RouterFunction<ServerResponse> {
//        return route(GET("/personCOVIDInfos").and(accept(MediaType.TEXT_PLAIN))) { request ->
//            ServerResponse.ok().body(personCOVIDInfoHandler.find(request), PersonCOVIDInfo::class.java)
//        }.andRoute(POST("/personCOVIDInfos").and(accept(MediaType.APPLICATION_JSON)).and(contentType(MediaType.APPLICATION_JSON))) {
//            request -> ServerResponse.ok().body(personCOVIDInfoHandler.create(request), PersonCOVIDInfo::class.java)
//        }.andRoute(PUT("/personCOVIDInfos").and(accept(MediaType.APPLICATION_JSON)).and(contentType(MediaType.APPLICATION_JSON))) {
//                request -> ServerResponse.ok().body(personCOVIDInfoHandler.update(request), PersonCOVIDInfo::class.java)
//        }.andRoute(DELETE("/personCOVIDInfos").and(accept(MediaType.APPLICATION_JSON)).and(contentType(MediaType.APPLICATION_JSON))) {
//                request -> ServerResponse.ok().body(personCOVIDInfoHandler.delete(request), PersonCOVIDInfo::class.java)
//        }
//    }


}