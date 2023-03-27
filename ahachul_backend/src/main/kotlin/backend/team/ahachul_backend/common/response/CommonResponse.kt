package backend.team.ahachul_backend.common.response

data class CommonResponse<T>(
        val code: String,
        val message: String,
        val result: T?
) {
    companion object {
        fun success(): CommonResponse<Unit> {
            return CommonResponse(
                    code = ResponseCode.SUCCESS.code,
                    message = ResponseCode.SUCCESS.message,
                    result = null
            )
        }

        fun <T> success(result: T): CommonResponse<T> {
            return CommonResponse(
                    code = ResponseCode.SUCCESS.code,
                    message = ResponseCode.SUCCESS.message,
                    result = result
            )
        }
    }
}