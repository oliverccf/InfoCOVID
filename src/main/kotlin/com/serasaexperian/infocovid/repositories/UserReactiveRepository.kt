package com.serasaexperian.infocovid.repositories

import com.serasaexperian.infocovid.models.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface UserReactiveRepository : ReactiveMongoRepository<User?, String?> {
    fun findByEmail(email: String): Flux<User>
}