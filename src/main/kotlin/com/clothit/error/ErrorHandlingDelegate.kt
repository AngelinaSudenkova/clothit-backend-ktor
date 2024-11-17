package com.clothit.error

import com.clothit.error.annotations.HandleErrors
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.full.findAnnotation

class ErrorHandlingDelegate<T : Any>(private val target: T) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        val clazz = target::class
        val annotation = clazz.findAnnotation<HandleErrors>()

        return try {
            method.invoke(target, *(args ?: emptyArray()))
        } catch (e: Exception) {
            if (annotation != null) {
                val stackTrace = StringWriter().apply {
                    e.printStackTrace(PrintWriter(this))
                }.toString()
                println("Stack trace:\n$stackTrace") //TODO add logger
            }
            throw ExceptionCustomMessage(ExceptionTypes.INTERNAL_SERVER_ERROR).toException()
        }
    }

    companion object {
        inline fun <reified T : Any> create(target: T): T {
            return Proxy.newProxyInstance(
                T::class.java.classLoader,
                arrayOf(T::class.java),
                ErrorHandlingDelegate(target)
            ) as T
        }
    }
}
