package backend.team.ahachul_backend.common.exception

import backend.team.ahachul_backend.common.response.ResponseCode

class CommonException(
        val code: ResponseCode
): RuntimeException(code.message) {

//        constructor(code: ResponseCode): this(code)
//
//        constructor(code: ResponseCode, e: Throwable): super(code.message, e)
}