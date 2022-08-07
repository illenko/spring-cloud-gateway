package com.illenko.gateway.filter

import com.illenko.gateway.config.RedisHashComponent
import com.illenko.gateway.config.RoutesConfig
import com.illenko.gateway.dto.ApiKey
import com.illenko.gateway.util.MapperUtils
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class AuthFilter(private val redisHashComponent: RedisHashComponent) : GlobalFilter, Ordered {
    private val log = KotlinLogging.logger {}

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void>? {
        val apiKeyHeader = exchange.request.headers["key"]
        log.info("api key {} ", apiKeyHeader)
        val routeId = exchange.getAttribute<Route?>(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)?.id

        if (routeId == null || apiKeyHeader == null || apiKeyHeader.isEmpty() || !isAuthorize(
                routeId, apiKeyHeader.first()
            )
        ) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "you can't consume this service , Please validate your apikeys"
            )
        }
        return chain.filter(exchange)
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }

    private fun isAuthorize(routeId: String, apiKey: String): Boolean {
        val apiKeyObject = redisHashComponent.get(RoutesConfig.RECORDS_KEY, apiKey)
        return if (apiKeyObject != null) {
            val (_, services) = MapperUtils.map(apiKeyObject, ApiKey::class.java)
            services.contains(routeId)
        } else {
            false
        }
    }

} 