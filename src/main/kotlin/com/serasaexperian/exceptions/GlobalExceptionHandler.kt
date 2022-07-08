package com.serasaexperian.exceptions

import com.serasaexperian.exceptions.ApiError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.WebUtils

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(ex: ResponseStatusException, headers: HttpHeaders?, request: WebRequest): ResponseEntity<ApiError> {
        return handleExceptionInternal(ex, ApiError(ex.message), headers, request)
    }

    protected fun handleExceptionInternal(
        ex: ResponseStatusException,
        body: ApiError?,
        headers: HttpHeaders?,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        if (HttpStatus.INTERNAL_SERVER_ERROR == ex.status) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST)
        }
        return ResponseEntity(body, headers, ex.status)
    }
}