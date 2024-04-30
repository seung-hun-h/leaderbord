package org.example.runningleaderboard.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.DataType
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component

@Component
class RedisKeyValueStore(
	private val redisTemplate: RedisTemplate<String, String>,
	private val objectMapper: ObjectMapper
) {
	fun save(key: String, value: Any) {
		redisTemplate.opsForValue().set(
			key,
			objectMapper.writeValueAsString(value)
		)
	}

	fun <T> get(key: String, type: Class<T>): T? {
		val result = redisTemplate.opsForValue().get(key).toString()

		return if (result.isBlank()) null
		else {
			return objectMapper.readValue(result, type)
		}
	}

	fun getKeys(pattern: String): List<String> {
		val result = mutableListOf<String>()

		redisTemplate.scan(ScanOptions.scanOptions()
			.match(pattern)
			.type(DataType.STRING)
			.build())
			.forEachRemaining {
				result += it
			}

		return result
	}
}