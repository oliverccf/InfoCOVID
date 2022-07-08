package com.serasaexperian.infocovid.models

import com.serasaexperian.util.checkCPF
import com.serasaexperian.util.checkEmail
import com.serasaexperian.util.checkIsNullOrEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author Oliver
 */
@Document("pessoas_COVID_infos")
data class PessoaCOVIDInfo(
    @Id
    val id: String? = null,
    val cpf: String?,
    val email: String?,
    val nome: String?,
    val vacinado: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PessoaCOVIDInfo

        if (id != other.id) return false
        if (cpf != other.cpf) return false
        if (email != other.email) return false
        if (nome != other.nome) return false
        if (vacinado != other.vacinado) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + cpf.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + nome.hashCode()
        result = 31 * result + vacinado.hashCode()
        return result
    }

    @Throws(Exception::class)
    fun validaCampos(): String {
      var message: String = ""
      message += validaCPF() +
        validaEmail() +
        validaCampoNomeVazio()
      return message
    }

    private fun validaCPF(): String? {
        try {
            checkCPF(cpf!!)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }

    private fun validaEmail(): String? {
        try {
            checkEmail(email!!)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }

    private fun validaCampoNomeVazio(): String? {
        try {
            checkIsNullOrEmpty("Nome", nome!!)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
}