package com.illenko.gateway.config

import com.illenko.gateway.util.MapperUtils
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisHashComponent(val redisTemplate: RedisTemplate<String, Any>) {
    private val log = KotlinLogging.logger {}

    fun set(key: String, hashKey: String, value: Any) {
        val ruleHash = MapperUtils.map(obj = value, Map::class.java)
        log.debug { "set:ruleHash - $ruleHash" }
        redisTemplate.opsForHash<Any, Any>().put(key, hashKey, ruleHash)
    }

    fun values(key: String): List<Any> = redisTemplate.opsForHash<Any, Any>().values(key)

    fun get(key: String, hashKey: Any) = redisTemplate.opsForHash<Any, Any>().get(key, hashKey)
}