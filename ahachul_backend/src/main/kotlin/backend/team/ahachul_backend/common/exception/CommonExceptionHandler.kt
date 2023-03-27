package backend.team.ahachul_backend.common.exception

import backend.team.ahachul_backend.common.response.CommonException
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun exception(e: Exception): CommonResponse<Unit> {
        return CommonResponse.fail()
    }

    @ExceptionHandler(CommonException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun commonException(e: CommonException): CommonResponse<Unit> {
        return CommonResponse.fail(e.code)
    }
}