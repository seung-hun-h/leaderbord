package org.example.runningleaderboard.search

import org.example.runningleaderboard.redis.RedisPriorityKeyValueStore
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class SearchService(
	private val redisPriorityKeyValueStore: RedisPriorityKeyValueStore
) {
	fun saveSearchKeyword(key: String, keyword: String) {
		redisPriorityKeyValueStore.save("search-keyword:$key", keyword, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toDouble())
		redisPriorityKeyValueStore.remove("search-keyword:$key", 0, 0)
	}

	fun getSearchKeywords(key: String): List<String> {
		return redisPriorityKeyValueStore.getList("search-keyword:$key", 0, -1)
			.mapNotNull { it.value }
	}
}