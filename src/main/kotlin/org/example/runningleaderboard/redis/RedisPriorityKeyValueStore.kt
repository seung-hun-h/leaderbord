package org.example.runningleaderboard.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.zset.Aggregate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Component

@Component
class RedisPriorityKeyValueStore(
	private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
	fun increment(key: String, value: Any, score: Double) {
		redisTemplate.opsForZSet().incrementScore(
			key,
			objectMapper.writeValueAsString(value),
			score
		)
	}

	fun save(key: String, value: Any, score: Double) {
		redisTemplate.opsForZSet().add(
			key,
			objectMapper.writeValueAsString(value),
			score
		)
	}

	fun getList(key: String, from: Int, to: Int): List<ZSetOperations. TypedTuple<String>> {
		return getList(key, from.toLong(), to.toLong())
	}

	fun getList(key: String, from: Long, to: Long): List<ZSetOperations. TypedTuple<String>> {
		val result = redisTemplate.opsForZSet()
			.reverseRangeWithScores(key, from, to) ?: emptySet()

		return result.toList()
	}

	fun remove(key: String, from: Int, to: Int) {
		redisTemplate.opsForZSet()
			.removeRange(key, from.toLong(), to.toLong())
	}

	fun existsKey(key: String): Boolean {
		return redisTemplate.hasKey(key)
	}

	fun union(keys: Set<String>, destinationKey: String) {
		if (keys.isEmpty()) {
			return
		}

		val first = keys.first()
		val otherKeys = keys.minus(first)

		redisTemplate.opsForZSet()
			.unionAndStore(first, otherKeys, destinationKey, Aggregate.SUM)
	}
}