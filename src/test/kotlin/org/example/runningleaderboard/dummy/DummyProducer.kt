package org.example.runningleaderboard.dummy

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.example.runningleaderboard.runner.Runner
import org.example.runningleaderboard.running.SaveRunningRequest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.YearMonth
import kotlin.math.roundToInt
import kotlin.random.Random

class DummyProducer {
	private val objectMapper =
		jacksonObjectMapper().apply {
			registerModule(JavaTimeModule())
			registerKotlinModule()
			disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		}

	@Test
	fun produceRunner() {
		val okHttpClient = OkHttpClient()

		// runner 생성
		listOf("함승훈", "이명옥", "함영규", "김준일", "김준형", "최진호", "정지원", "김현동", "임영섭", "이정민", "James")
			.map { runnerName ->
				val request = Request.Builder()
					.url("http://localhost:8080/runners")
					.post(
						"""
							{
								"runnerName": "${runnerName}"
							}
						""".trimIndent().toRequestBody("application/json; charset=utf-8".toMediaType())
					).build()

				okHttpClient.newCall(request).execute().use { response -> response.body!!.string() }
			}.map { body ->
				objectMapper.readValue<Runner>(body)
			}.map { runner ->
				randomRunning(runner, 2024, 4)
			}.flatten()
			.map { saveRunningRequest ->
				val request = Request.Builder()
					.url("http://localhost:8080/runnings")
					.post(
						objectMapper.writeValueAsString(saveRunningRequest)
							.toRequestBody("application/json; charset=utf-8".toMediaType())
					).build()

				okHttpClient.newCall(request).execute().use { response -> response.body!!.string() }
			}.forEach { responseBody ->
				println(responseBody)
			}

		/*
		* 4월 1일 - 4월 30일
		* 빈도 하루 0 - 1회
		* 거리 1000m - 20000m
		* 속도 2 - 4 m/s
		* 시간 거리/속도
		* */
	}

	private fun randomRunning(runner: Runner, year: Int, month: Int): List<SaveRunningRequest> {
		val lengthOfMonth = lastDayOfMonth(year, month)

		val result = mutableListOf<SaveRunningRequest>()
		for (i in 1..lengthOfMonth) {
			if (randomInt(0, 10) == 0) {
				continue
			}

			val distance = randomInt(1000, 20001)
			val speed = randomDouble(2.0, 4.1)
			val seconds = distance / speed

			result.add(SaveRunningRequest(runner.id, distance, seconds.toInt(), LocalDateTime.of(year, month, randomInt(1, lastDayOfMonth(year, month) + 1), randomInt(0, 24), randomInt(0, 60), randomInt(0, 60))))
		}

		return result
	}

	private fun lastDayOfMonth(year: Int, month: Int): Int {
		val yearMonth = YearMonth.of(year, month)
		val lengthOfMonth = yearMonth.lengthOfMonth()
		return lengthOfMonth
	}

	private fun randomInt(from: Int, until: Int): Int = Random.nextInt(from, until)

	private fun randomDouble(from: Double, until: Double): Double = (Random.nextDouble(from, until) * 100).roundToInt() / 100.0

	@Test
	fun getRunner() {
		val okHttpClient = OkHttpClient()

		val request = Request.Builder()
			.get()
			.url("http://localhost:8080/runners/0")
			.build()

		okHttpClient.newCall(request).execute().use { response -> println(response.body!!.string()) }
	}
}