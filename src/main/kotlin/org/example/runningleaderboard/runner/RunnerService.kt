package org.example.runningleaderboard.runner

import org.example.runningleaderboard.redis.RedisKeyValueStore
import org.example.runningleaderboard.search.SearchService
import org.springframework.stereotype.Service

@Service
class RunnerService(
	private val runnerIdProvider: RunnerIdProvider,
	private val redisKeyValueStore: RedisKeyValueStore,
	private val searchService: SearchService
) {

	fun save(saveRunnerRequest: SaveRunnerRequest): Runner {
		val runner = Runner(
			id = runnerIdProvider.provide(),
			name = saveRunnerRequest.runnerName,
		)

		redisKeyValueStore.save(key(runner.id), runner)

		return runner
	}

	fun get(id: Long): Runner {
		return redisKeyValueStore.get(key(id), Runner::class.java)
			?: throw NoSuchElementException("No runner with id = $id")
	}

	private fun key(id: Long) = "runner:$id"

	fun update(id: Long, updateRunnerRequest: UpdateRunnerRequest) {
		val runner = get(id)

		runner.name = updateRunnerRequest.runnerName

		redisKeyValueStore.save(key(runner.id), runner)
	}

	fun getRunners(searchRunnerRequest: SearchRunnerRequest): List<Runner> {
		val result = redisKeyValueStore.getKeys("runner:[0-9]*")
			.mapNotNull { redisKeyValueStore.get(it, Runner::class.java) }
			.filter { it.name == searchRunnerRequest.name }

		searchService.saveSearchKeyword(searchRunnerRequest.runnerId.toString(), searchRunnerRequest.name)

		return result
	}
}
