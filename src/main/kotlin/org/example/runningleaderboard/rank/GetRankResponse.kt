package org.example.runningleaderboard.rank

data class GetRankResponse(
	val ranks: List<Rank>,
	val nextRank: Int
)
