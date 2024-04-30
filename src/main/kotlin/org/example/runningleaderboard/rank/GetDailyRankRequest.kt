package org.example.runningleaderboard.rank

import java.time.LocalDate

data class GetDailyRankRequest(
	val currentRank: Int,
	val size: Int = 10,
	val date: LocalDate
)
