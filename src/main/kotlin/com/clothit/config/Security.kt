package com.clothit.config

import com.clothit.server.service.TokenService
import com.clothit.util.JwtUtils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val jwtService: TokenService by inject()
    install(Authentication) {
        jwt {
            realm = JwtUtils.realm
            verifier(JwtUtils.jwtVerifier)
            validate { credential ->
                jwtService.validateAccessToken(credential)
            }

        }
    }
}

fun Application.corsConfigure() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }

}
