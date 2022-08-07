package com.illenko.gateway.config

import com.illenko.gateway.util.MapperUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisCache(val redisTemplate: RedisTemplate<String, Any>) {
    fun set(key: String, hashKey: String, value: Any) {
        redisTemplate.opsForHash<Any, Any>().put(key, hashKey, MapperUtils.map(obj = value, Map::class.java))
    }

    fun values(key: String): List<Any> = redisTemplate.opsForHash<Any, Any>().values(key)

    fun get(key: String, hashKey: Any) = redisTemplate.opsForHash<Any, Any>().get(key, hashKey)
}