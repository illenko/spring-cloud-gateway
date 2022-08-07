package com.illenko.gateway.util

import com.fasterxml.jackson.databind.ObjectMapper

object MapperUtils {

    @JvmStatic
    private val mapper = ObjectMapper()

    @JvmStatic
    fun <T : Any> map(obj: Any, clazz: Class<T>): T = mapper.convertValue(obj, clazz)
}