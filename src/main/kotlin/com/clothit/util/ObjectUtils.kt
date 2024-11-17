package com.clothit.util

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes

class ObjectUtils {

    companion object {
        fun <T> checkNotNull(o: T?): T {
            return o ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
        }

        fun <T> checkNotNullBadRequest(value: T?) : T {
           return value ?: throw ExceptionCustomMessage(ExceptionTypes.BAD_REQUEST).toException()
        }
    }
}