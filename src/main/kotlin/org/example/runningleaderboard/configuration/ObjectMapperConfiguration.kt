package org.example.runningleaderboard.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration {
	@Bean
	fun objectMapper(): ObjectMapper {
		return jacksonObjectMapper().apply {
			registerModule(JavaTimeModule())
			registerKotlinModule()
			disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		}
	}
}