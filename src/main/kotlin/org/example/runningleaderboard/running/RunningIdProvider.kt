package org.example.runningleaderboard.running

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class RunningIdProvider {
	private val id = AtomicLong(0)

	fun provide(): Long {
		return id.getAndIncrement()
	}
}