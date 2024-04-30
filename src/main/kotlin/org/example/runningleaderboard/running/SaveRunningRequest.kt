package org.example.runningleaderboard.running

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SaveRunningRequest(
	val runnerId: Long,
	val distance: Int,
	val seconds: Int,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	val ranAt: LocalDateTime
)
