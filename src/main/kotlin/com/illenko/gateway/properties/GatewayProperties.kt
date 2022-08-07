package com.illenko.gateway.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gateway")
class GatewayProperties(val clients: List<Client>, val services: List<Service>)