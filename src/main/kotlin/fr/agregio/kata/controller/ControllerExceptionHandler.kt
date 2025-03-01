package org.example.fr.agregio.kata.controller

import org.example.fr.agregio.kata.controller.dto.ErrorApiDto
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(value = [IllegalArgumentException::class])
    @ResponseStatus(BAD_REQUEST)
    fun handleIllegalArgumentException(exception: IllegalArgumentException) =
        ResponseEntity(ErrorApiDto(exception.message), BAD_REQUEST)
}