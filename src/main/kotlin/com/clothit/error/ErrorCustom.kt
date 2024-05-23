package com.clothit.error
data class ErrorCustom(
    val errorCustomMessage: ErrorCustomMessage
) : RuntimeException() {
    constructor(message: String, errorCustomMessage: ErrorCustomMessage) : this(errorCustomMessage) {
        super.initCause(RuntimeException(message))
    }
}
