package com.clothit



import com.clothit.config.corsConfigure
import com.clothit.config.openApiConfigure
import com.clothit.config.serializationConfigure
import com.clothit.config.swaggerConfigure
import com.clothit.server.api.fileRoutingConfigure
import com.clothit.server.api.itemRoutingConfigure
import com.clothit.server.api.outfitRoutingConfigure
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

val PASSWORD_DATABASE = System.getenv("PASSWORDCLOTHIT")


fun main() {

    Database.connect("jdbc:postgresql://localhost:5432/clothit_new", driver = "org.postgresql.Driver",
        user = "postgres", password = PASSWORD_DATABASE
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    ///--------------routing
    itemRoutingConfigure()
    fileRoutingConfigure()
    outfitRoutingConfigure()

    ///-------------config
    serializationConfigure()

    corsConfigure()



    swaggerConfigure()
    openApiConfigure()
}
