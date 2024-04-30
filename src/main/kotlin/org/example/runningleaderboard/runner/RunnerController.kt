package org.example.runningleaderboard.runner

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RequestMapping("/runners")
@RestController
class RunnerController(
	private val runnerService: RunnerService
) {

	@PostMapping
	fun saveRunner(@RequestBody saveRunnerRequest: SaveRunnerRequest): ResponseEntity<Runner> {
		val runner = runnerService.save(saveRunnerRequest)

		return ResponseEntity.created(URI("/${runner.id}"))
			.body(runner)
	}

	@GetMapping("/{id}")
	fun getRunner(@PathVariable("id") id: Long): ResponseEntity<Runner> {
		return ResponseEntity.ok(runnerService.get(id))
	}

	@PatchMapping("/{id}")
	fun updateRunner(@PathVariable("id") id: Long, @RequestBody updateRunnerRequest: UpdateRunnerRequest): ResponseEntity<Void> {
		runnerService.update(id, updateRunnerRequest)
		return ResponseEntity.ok().build()

	}

	@GetMapping
	fun getRunners(searchRunnerRequest: SearchRunnerRequest): ResponseEntity<List<Runner>> {
		return ResponseEntity.ok(runnerService.getRunners(searchRunnerRequest))
	}
}