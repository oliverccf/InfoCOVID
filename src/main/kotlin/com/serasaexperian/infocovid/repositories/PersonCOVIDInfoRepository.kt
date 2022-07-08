package com.serasaexperian.infocovid.repositories

import com.serasaexperian.infocovid.models.PessoaCOVIDInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonCOVIDInfoRepository: ReactiveMongoRepository<PessoaCOVIDInfo, String>