package org.example.runningleaderboard.running

import org.example.runningleaderboard.runner.Runner
import java.time.Duration
import java.time.LocalDateTime

class Running(
	val id: Long,
	val runnerId: Long,
	val distance: Int,
	val startAt: LocalDateTime,
	val seconds: Int,
) {
}