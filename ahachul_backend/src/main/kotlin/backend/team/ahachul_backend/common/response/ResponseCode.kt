package backend.team.ahachul_backend.common.response

enum class ResponseCode(
        val code: String,
        val message: String
) {
    SUCCESS("100", "SUCCESS"),
    BAD_REQUEST("101", "BAD_REQUEST"),
    INTERNAL_SERVER_ERROR("102", "INTERNAL_SERVER_ERROR")
}