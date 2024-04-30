package org.example.runningleaderboard.rank

import org.example.runningleaderboard.runner.Runner

data class Rank(
	val runner: Runner,
	val distance: Int,
	val rank: Int
)
