package backend.team.ahachul_backend.api.rank.application.service

import backend.team.ahachul_backend.api.rank.adapter.web.`in`.dto.GetHashTagRankDto
import backend.team.ahachul_backend.common.client.RedisClient
import org.springframework.stereotype.Service

@Service
class HashTagRankService(
    val redisClient: RedisClient
){
    
    fun increaseCount(hashTagName: String) {
        val key: String = KEY + hashTagName
        val setOperations = redisClient.getZSetOps()
        setOperations.incrementScore(KEY, hashTagName, INCREASE_CNT.toDouble())
    }

    fun getRank(): GetHashTagRankDto.Response {
        val setOperations = redisClient.getZSetOps()
        val responseSet = setOperations.reverseRange(KEY, 0, LIMIT)
        val response = responseSet?.toList() ?: listOf()
        return GetHashTagRankDto.Response(response)
    }

    fun get(hashTagName: String): Double? {
        val setOperations = redisClient.getZSetOps()
        return setOperations.score(KEY, hashTagName)
    }

    companion object {
        const val KEY = "hashtags"
        const val INCREASE_CNT = 1
        const val LIMIT = 10L
    }
}
