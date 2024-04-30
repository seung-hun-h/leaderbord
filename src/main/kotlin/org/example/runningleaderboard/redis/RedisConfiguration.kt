package org.example.runningleaderboard.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class RedisConfiguration(
	private val redisConnectionFactory: RedisConnectionFactory
) {
	@Bean
	fun redisTemplate(): RedisTemplate<String, String> {
		val redisTemplate = RedisTemplate<String, String>()
		redisTemplate.connectionFactory = redisConnectionFactory


		redisTemplate.keySerializer = RedisSerializer.string()
		redisTemplate.valueSerializer = RedisSerializer.string()

		redisTemplate.hashKeySerializer = RedisSerializer.string()
		redisTemplate.hashValueSerializer = RedisSerializer.string()
		redisTemplate.setEnableTransactionSupport(true)
		return redisTemplate
	}
}