package org.example.runningleaderboard.runner

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class RunnerIdProvider {
	private val id = AtomicLong(0)

	fun provide(): Long {
		return id.getAndIncrement()
	}
}