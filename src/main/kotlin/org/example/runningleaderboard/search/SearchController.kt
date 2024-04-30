package org.example.runningleaderboard.search

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/search")
@RestController
class SearchController(
	private val searchService: SearchService
) {

	@GetMapping("/keywords/{runnerId}")
	fun getSearchKeywords(@PathVariable("runnerId") runnerId: String): ResponseEntity<List<String>> {
		return ResponseEntity.ok(searchService.getSearchKeywords(runnerId))
	}
}