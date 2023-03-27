package backend.team.ahachul_backend.common.response

class CommonException(
        val code: ResponseCode,
        val errorMessage: String
): RuntimeException(errorMessage) {
}