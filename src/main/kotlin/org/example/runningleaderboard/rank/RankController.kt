package org.example.runningleaderboard.rank

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/ranks")
@RestController
class RankController(
	private val rankService: RankService
) {
	@GetMapping("/total")
	fun getTotalRank(getTotalRankRequest: GetTotalRankRequest): ResponseEntity<GetRankResponse> {
		return ResponseEntity.ok(rankService.getTotalRanks(getTotalRankRequest))
	}

	@GetMapping("/daily")
	fun getDaily(getDailyRankRequest: GetDailyRankRequest): ResponseEntity<GetRankResponse> {
		return ResponseEntity.ok(rankService.getDailyRanks(getDailyRankRequest))
	}

	@GetMapping("/weekly")
	fun getWeekly(getWeeklyRankRequest: GetWeeklyRankRequest): ResponseEntity<GetRankResponse> {
		return ResponseEntity.ok(rankService.getWeeklyRanks(getWeeklyRankRequest))
	}
}