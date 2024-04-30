package org.example.runningleaderboard.rank

import java.time.YearMonth

data class GetWeeklyRankRequest(
	val currentRank: Int,
	val size: Int = 10,
	val yearMonth: YearMonth,
	val weekOfMonth: Int
)
