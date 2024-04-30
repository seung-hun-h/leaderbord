package org.example.runningleaderboard.rank

import org.example.runningleaderboard.redis.RedisPriorityKeyValueStore
import org.example.runningleaderboard.runner.RunnerService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

@Service
class RankService(
	private val runnerService: RunnerService,
	private val redisPriorityKeyValueStore: RedisPriorityKeyValueStore,
) {
	fun getTotalRanks(totalRankRequest: GetTotalRankRequest): GetRankResponse {
		val ranks = getRanks(totalRankRequest.currentRank, totalRankRequest.size, "total-distance")

		return GetRankResponse(ranks, totalRankRequest.currentRank + ranks.size)
	}

	fun getDailyRanks(dailyRankRequest: GetDailyRankRequest): GetRankResponse {
		val ranks = getRanks(dailyRankRequest.currentRank, dailyRankRequest.size, "daily-distance:${dailyRankRequest.date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}")

		return GetRankResponse(ranks, dailyRankRequest.currentRank + ranks.size)
	}

	fun getWeeklyRanks(weeklyRankRequest: GetWeeklyRankRequest): GetRankResponse {
		val key = "weekly-distance:${weeklyRankRequest.yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"))}-${weeklyRankRequest.weekOfMonth}"

		if (latestWeek(weeklyRankRequest.yearMonth, weeklyRankRequest.weekOfMonth)
			|| !redisPriorityKeyValueStore.existsKey(key)) {
			val dailyKeys = makeWeekDayWeeks(weeklyRankRequest.yearMonth, weeklyRankRequest.weekOfMonth)
			redisPriorityKeyValueStore.union(dailyKeys, key)
		}

		val ranks = getRanks(weeklyRankRequest.currentRank, weeklyRankRequest.size, key)

		return GetRankResponse(ranks, weeklyRankRequest.currentRank + ranks.size)
	}

	private fun makeWeekDayWeeks(yearMonth: YearMonth, weekOfMonth: Int): Set<String> {
		val weekFields = WeekFields.of(Locale.getDefault())

		return (1..yearMonth.lengthOfMonth())
			.map { day -> yearMonth.atDay(day) }
			.filter { date ->
				val week = date.get(weekFields.weekOfMonth())
				week == weekOfMonth
			}
			.map { date -> "daily-distance:${date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}" }
			.toSet()
	}

	private fun latestWeek(yearMonth: YearMonth, weekOfMonth: Int): Boolean {
		val latestDate = LocalDate.now()
		val latestYearMonth = YearMonth.now()
		val latestWeekOfMonth = latestDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth())

		return latestYearMonth == yearMonth && latestWeekOfMonth == weekOfMonth
	}

	private fun getRanks(currentRank: Int, pageSize: Int, key: String): List<Rank> {
		val end = currentRank + pageSize - 1
		val longestDistanceRunnerIdListWithScore = redisPriorityKeyValueStore.getList(key, currentRank, end)

		val ranks = mutableListOf<Rank>()

		for (i in longestDistanceRunnerIdListWithScore.indices) {
			val typedTuple = longestDistanceRunnerIdListWithScore[i]
			val runner = runnerService.get(typedTuple.value!!.toInt().toLong())
			ranks += Rank(runner, typedTuple.score!!.toInt(), currentRank + i)
		}
		return ranks
	}
}

