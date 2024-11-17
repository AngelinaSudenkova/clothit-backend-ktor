package com.clothit.error

data class ExceptionCustomMessage(
    val cause: Throwable?,
    val errorType: ExceptionTypes
) {
    constructor(errorType: ExceptionTypes) : this(cause = null, errorType = errorType)


    fun toException(): ExceptionCustom {
        if (cause != null) {
            return ExceptionCustom(cause.message.toString(), exceptionCustomMessage = this)
        }
        return ExceptionCustom(exceptionCustomMessage = this)
    }
}