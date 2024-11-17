package com.clothit.error
data class ExceptionCustom(
    val exceptionCustomMessage: ExceptionCustomMessage
) : RuntimeException() {
    constructor(message: String, exceptionCustomMessage: ExceptionCustomMessage) : this(exceptionCustomMessage) {
        super.initCause(RuntimeException(message))
    }
}
