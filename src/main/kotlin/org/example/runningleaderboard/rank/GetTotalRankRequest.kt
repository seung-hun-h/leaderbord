package org.example.runningleaderboard.rank

data class GetTotalRankRequest(
	val currentRank: Int,
	val size: Int = 10
)
