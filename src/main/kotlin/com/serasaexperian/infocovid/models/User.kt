/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serasaexperian.infocovid.models

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author Oliver
 */
@Document("users")
data class User(
    @Id
    val id: String? = "",
    val username: String = "",
    val email: String = "",
    var password: String = "",
    val telephone: String = "",
    val created: LocalDateTime = LocalDateTime.now(),
    val enabled: Boolean = false
): Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    @Version
    val version = 0L

}