package org.example.runningleaderboard.running

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/runnings")
@RestController
class RunningController(
	private val runningService: RunningService
) {
	@PostMapping
	fun saveRunning(@RequestBody saveRunningRequest: SaveRunningRequest): ResponseEntity<Running> {
		val running = runningService.save(saveRunningRequest)

		return ResponseEntity.created(URI.create("/runnings/${running.id}"))
			.body(running)
	}
}