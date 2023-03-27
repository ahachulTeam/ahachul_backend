package backend.team.ahachul_backend.common.exception

import backend.team.ahachul_backend.common.logging.Logger
import backend.team.ahachul_backend.common.response.CommonException
import backend.team.ahachul_backend.common.response.CommonResponse
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonExceptionHandler {

    val logger = Logger(javaClass)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun exception(e: Exception): CommonResponse<Unit> {
        logger.error(
                message = e.message,
                code = ResponseCode.INTERNAL_SERVER_ERROR,
                stackTrace = e.stackTrace.contentToString()
        )
        return CommonResponse.fail()
    }

    @ExceptionHandler(CommonException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun commonException(e: CommonException): CommonResponse<Unit> {
        logger.error(
                message = e.message,
                code = e.code,
                stackTrace = e.stackTrace.contentToString()
        )
        return CommonResponse.fail(e.code)
    }
}