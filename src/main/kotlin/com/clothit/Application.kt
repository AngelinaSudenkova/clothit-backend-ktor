package com.clothit



import com.clothit.config.*
import com.clothit.config.di.appModule
import com.clothit.server.api.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.koin.core.context.startKoin

val PASSWORD_DATABASE = System.getenv("CLOTHITPASSWORD")


fun main() {

    Database.connect("jdbc:postgresql://localhost:5432/clothit_new", driver = "org.postgresql.Driver",
        user = "postgres", password = PASSWORD_DATABASE
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {

    startKoin{
        modules(appModule())
    }

    configureRouting()
    configureLogging()

    ///--------------security
    configureSecurity()

    ///--------------routing
    itemRoutingConfigure()
    fileRoutingConfigure()
    outfitRoutingConfigure()
    configureRouting()
    userRoutingConfigure()
    friendRoutingConfigure()

    ///-------------config
    serializationConfigure()
    corsConfigure()
    configureSwagger()
    openApiConfigure()

}
