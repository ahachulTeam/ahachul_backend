package backend.team.ahachul_backend.api.rank.application.service

import backend.team.ahachul_backend.api.rank.adapter.web.`in`.dto.GetHashTagRankDto
import backend.team.ahachul_backend.common.client.RedisClient
import org.springframework.stereotype.Service

@Service
class HashTagRankService(
    val redisClient: RedisClient
){

    fun increaseCount(hashTagName: String) {
        val setOperations = redisClient.getZSetOps()

        val data = setOperations.score(SET_KEY, hashTagName)?.toInt()
        setOperations.add(SET_KEY, hashTagName, ((data ?: DEFAULT_VALUE) + INCREASE_CNT).toDouble())
    }

    fun getRank(): GetHashTagRankDto.Response {
        val setOperations = redisClient.getZSetOps()

        val responseSet = setOperations.reverseRange(SET_KEY, 0, LIMIT)
        val response = responseSet?.toList() ?: listOf()
        return GetHashTagRankDto.Response(response)
    }

    companion object {
        const val SET_KEY = "hashtags"
        const val DEFAULT_VALUE = 0
        const val INCREASE_CNT = 1
        const val LIMIT = 10L
    }
}
