package com.illenko.gateway.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate


@Configuration
class RedisConfig(private val redisProperties: RedisProperties) {

    @Bean
    fun jedisConnectionFactory() =
        JedisConnectionFactory(RedisStandaloneConfiguration(redisProperties.host, redisProperties.port))

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(jedisConnectionFactory())
        template.setEnableTransactionSupport(true)
        return template
    }
}