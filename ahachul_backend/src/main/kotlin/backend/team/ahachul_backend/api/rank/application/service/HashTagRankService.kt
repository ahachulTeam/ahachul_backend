package backend.team.ahachul_backend.api.rank.application.service

import backend.team.ahachul_backend.common.utils.FileUtils
import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.common.logging.Logger
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class HashTagRankService(
    val redisClient: RedisClient,
){
    var buffer = mutableListOf<JsonObject>()
    val logger = Logger(javaClass)

    /**
     * 비동기로 해시태그 로그 파일에 정보를 저장한다.
     */
    fun saveLog(name: String, userId: String, localDateTime: LocalDateTime) {
        val hashtagFileUrl = BASE_DIR + "${localDateTime.year}_${localDateTime.monthValue}_hashtag_log"

        // 저장 날짜에 해당하는 날짜 파일이 없다면 생성
        FileUtils.createFile(hashtagFileUrl)

        // 임시 버퍼에 들어오는 데이터 저장
        val jsonObject = createJsonObject(name, userId, localDateTime)
        if (buffer.size >= MAX_BUFFER_SIZE) {
            FileUtils.saveFile(hashtagFileUrl, buffer)
            buffer = mutableListOf()
        }
        buffer.add(jsonObject)
    }

    private fun createJsonObject(name: String, userId: String, localDateTime: LocalDateTime): JsonObject {
        val currentTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("userId", userId)
        jsonObject.addProperty("timestamp", currentTime)
        return jsonObject
    }

    /**
     * 캐시해둔 순위를 조회한다.
     */
    fun getRank(): List<String> {
        val mapper = ObjectMapper()
        val typeRef = object : TypeReference<List<String>>() {}
        return mapper.readValue(redisClient.get("hashtag_rank"), typeRef)
    }

    companion object {
        const val MAX_BUFFER_SIZE = 100
        // TODO S3에 파일 업로드
        const val BASE_DIR = "/Users/kangsemi/Desktop/git/ahachul_backend/ahachul_backend/src/main/resources/"
    }
}
