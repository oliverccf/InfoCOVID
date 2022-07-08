package com.serasaexperian.infocovid.infocovidapi.test.validadores

import com.serasaexperian.util.checkCPF
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TestaValidadores {

    @Test
    @DisplayName("Teste Unitário de CPF")
    fun fazendoTestedeCpfValido() {
        val cpf = "94622036010"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(true, isValid)
    }

    @Test
    fun fazendoTesteDeCpfInvalido() {
        val cpf = "94622036011"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfComDigitoAMenos() {
        val cpf = "9462203602"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfValidoComPontos() {
        val cpf = "946.220.360-10"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfInvalidoComPontos() {
        val cpf = "946.220.360-11"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfInvalidoComVirgula() {
        val cpf = "946,220.360-11"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfInvalidoComEspacoNoFinal() {
        val cpf = "94622036010 "
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfInvalidoComEspacoNoComeco() {
        val cpf = " 94622036010"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteDeCpfInvalidoComEspacoNoMeio() {
        val cpf = "946220 36010"
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

    @Test
    fun fazendoTesteSemPassarCPF() {
        val cpf = " "
        var isValid = true
        try {
            checkCPF(cpf)
        } catch(ex: Exception) {
            isValid = false
        }
        assertEquals(false, isValid)
    }

}
