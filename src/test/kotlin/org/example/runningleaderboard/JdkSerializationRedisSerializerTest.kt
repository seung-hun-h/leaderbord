package org.example.runningleaderboard

import org.junit.jupiter.api.Test
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

class JdkSerializationRedisSerializerTest {
	@Test
	fun test() {
		val num = 0L

		val value = "test$num"

		val defaultSerializer = JdkSerializationRedisSerializer()
		val stringSerializer = StringRedisSerializer()

		val result1 = defaultSerializer.serialize(value)
		val result2 = stringSerializer.serialize(value)
		println(result1)
		println(result2)
	}
}