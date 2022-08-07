package com.illenko.gateway.filter

import com.illenko.gateway.config.RedisCache
import com.illenko.gateway.config.RoutesConfig
import com.illenko.gateway.dto.Client
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
class AuthFilter(private val redisCache: RedisCache) : GlobalFilter, Ordered {

    private val log = KotlinLogging.logger {}

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {

        val apiKeyHeader = exchange.request.headers["key"]
        val routeId = exchange.getAttribute<Route?>(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)?.id
        log.debug("Handled request with API Key: $apiKeyHeader and routeId: $routeId")

        if (routeId == null || apiKeyHeader == null || !isAuthorized(routeId, apiKeyHeader.first())) {
            log.debug { "Request is not authorized" }
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Request is not authorized")
        }

        return chain.filter(exchange)
    }

    override fun getOrder() = Ordered.LOWEST_PRECEDENCE

    private fun isAuthorized(routeId: String, apiKey: String): Boolean {
        val apiKeyObject = redisCache.get(RoutesConfig.RECORDS_KEY, apiKey)
        return if (apiKeyObject != null) {
            val (_, _, services) = MapperUtils.map(apiKeyObject, Client::class.java)
            services.contains(routeId)
        } else {
            false
        }
    }

} 