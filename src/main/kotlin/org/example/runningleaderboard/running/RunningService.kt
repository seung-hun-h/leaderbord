package org.example.runningleaderboard.running

import org.example.runningleaderboard.redis.RedisKeyValueStore
import org.example.runningleaderboard.redis.RedisPriorityKeyValueStore
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class RunningService(
	private val redisKeyValueStore: RedisKeyValueStore,
	private val redisPriorityKeyValueStore: RedisPriorityKeyValueStore,
	private val runningIdProvider: RunningIdProvider
) {
	fun save(saveRunningRequest: SaveRunningRequest): Running {
		val running = Running(
			id = runningIdProvider.provide(),
			runnerId = saveRunningRequest.runnerId,
			distance = saveRunningRequest.distance,
			startAt = saveRunningRequest.ranAt,
			seconds = saveRunningRequest.seconds
		)

		redisKeyValueStore.save("running:${running.id}", running)

		val startAt = running.startAt

		redisPriorityKeyValueStore.increment(
			key = "daily-distance:${startAt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))}",
			value = running.runnerId,
			score = running.distance.toDouble()
		)

		redisPriorityKeyValueStore.increment(
			key = "total-distance",
			value = running.runnerId,
			score = running.distance.toDouble()
		)

		return running
	}
}
