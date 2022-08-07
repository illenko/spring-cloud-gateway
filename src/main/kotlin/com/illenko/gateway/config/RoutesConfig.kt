package com.illenko.gateway.config

import com.illenko.gateway.properties.GatewayProperties
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer
import javax.annotation.PostConstruct


@Configuration
class RoutesConfig(
    private val redisCache: RedisCache,
    private val gatewayProperties: GatewayProperties
) {
    @PostConstruct
    fun initKeysToRedis() {
        if (redisCache.values(RECORDS_KEY).isEmpty()) {
            gatewayProperties.clients.forEach(Consumer { redisCache.set(RECORDS_KEY, it.key, it) })
        }
    }

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        val rouses = builder.routes()
        gatewayProperties.services.forEach { service ->
            rouses.route(service.key) { route ->
                route.path("${service.path}/**").filters { it.stripPrefix(service.prefix) }.uri(service.url)
            }
        }
        return rouses.build()
    }

    companion object {
        const val RECORDS_KEY = "apiKeys"
    }
}