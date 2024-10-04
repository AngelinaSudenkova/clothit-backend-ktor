package com.clothit



import com.clothit.config.*
import com.clothit.server.api.fileRoutingConfigure
import com.clothit.server.api.itemRoutingConfigure
import com.clothit.server.api.outfitRoutingConfigure
import com.clothit.server.api.userRoutingConfigure
import com.clothit.server.dao.impl.TokenDaoImpl
import com.clothit.server.dao.impl.UserDaoImpl
import com.clothit.server.service.impl.JwtServiceImpl
import com.clothit.server.service.impl.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

val PASSWORD_DATABASE = System.getenv("CLOTHITPASSWORD")


fun main() {

    Database.connect("jdbc:postgresql://localhost:5432/clothit_new", driver = "org.postgresql.Driver",
        user = "postgres", password = PASSWORD_DATABASE
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    ///--------------security
    configureSecurity(jwtService = JwtServiceImpl(userService = UserServiceImpl(userDao = UserDaoImpl), tokenDao = TokenDaoImpl))

    ///--------------routing
    itemRoutingConfigure()
    fileRoutingConfigure()
    outfitRoutingConfigure()
    configureRouting()
    userRoutingConfigure()

    ///-------------config
    serializationConfigure()
    corsConfigure()
    swaggerConfigure()

}
