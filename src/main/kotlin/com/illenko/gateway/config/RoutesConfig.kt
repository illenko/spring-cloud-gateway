package com.illenko.gateway.config

import com.illenko.gateway.properties.GatewayProperties
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

    companion object {
        const val RECORDS_KEY = "apiKeys"
    }
}