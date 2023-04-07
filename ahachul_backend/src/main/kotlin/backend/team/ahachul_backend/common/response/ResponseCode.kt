package backend.team.ahachul_backend.common.response

import org.springframework.http.HttpStatus

enum class ResponseCode(
        val code: String,
        val message: String,
        val httpStatus: HttpStatus
) {
    SUCCESS("100", "SUCCESS", HttpStatus.OK),
    BAD_REQUEST("101", "BAD_REQUEST", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("102", "INTERNAL_SERVER_ERROR", HttpStatus.BAD_REQUEST),
    INVALID_DOMAIN("103", "유효하지 않은 도메인입니다.", HttpStatus.BAD_REQUEST),

    INVALID_APPLE_ID_TOKEN("200", "유효하지 않은 ID 토큰입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN("201", "유효하지 않은 엑세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("202", "유효기간이 만료된 엑세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("203", "유효하지 않은 리프레쉬 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("204", "유효기간이 만료된 리프레쉬 토큰입니다.", HttpStatus.UNAUTHORIZED),
}