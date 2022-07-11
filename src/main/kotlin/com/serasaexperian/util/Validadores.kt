package com.serasaexperian.util

import br.com.caelum.stella.validation.CPFValidator
import br.com.caelum.stella.validation.InvalidStateException
import org.apache.logging.log4j.util.Strings
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress


@Throws(Exception::class)
fun checkCPF(cpf: String?) {
    try {
        val validator = CPFValidator()
        validator.assertValid(cpf)
    } catch (ex: InvalidStateException) {
        throw Exception("CPF inválido")
    }
}

@Throws(Exception::class)
fun checkEmail(email: String?) {
    try {
        val emailAddr = InternetAddress(email)
        emailAddr.validate()
    } catch (ex: AddressException) {
        throw Exception("Email inválido")
    }
}

@Throws(Exception::class)
fun checkIsNullOrEmpty(campo: String?, value: String?) {
    if (Strings.isEmpty(value)) {
        throw Exception("$campo não pode ser vazio")
    }
}
