package com.clothit.error

data class ErrorCustomMessage(
    val cause: Throwable?,
    val errorType: ErrorTypes
) {
    constructor(errorType: ErrorTypes) : this(cause = null, errorType = errorType)


    fun toException() : ErrorCustom{
        if(cause != null) {
            return ErrorCustom(cause.message.toString(), errorCustomMessage = this)
        }
        return ErrorCustom(errorCustomMessage = this)
    }
}