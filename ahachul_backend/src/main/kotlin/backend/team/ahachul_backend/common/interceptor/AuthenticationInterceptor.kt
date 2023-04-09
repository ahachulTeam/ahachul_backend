package backend.team.ahachul_backend.common.interceptor

import backend.team.ahachul_backend.common.annotation.Authentication
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.JwtUtils
import backend.team.ahachul_backend.common.utils.RequestUtils
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler

@Component
class AuthenticationInterceptor(
        private val jwtUtils: JwtUtils
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is ResourceHttpRequestHandler) return true

        val handlerMethod = handler as HandlerMethod
        handlerMethod.getMethodAnnotation(Authentication::class.java) ?: return true

        try {
            val jwtToken = request.getHeader("Authorization")
            val verifiedJwtToken = jwtUtils.verify(jwtToken)

            RequestUtils.setAttribute("memberId", verifiedJwtToken.body.subject)

        } catch (e: Exception) {
            when (e) {
                is SignatureException, is UnsupportedJwtException, is IllegalArgumentException, is MalformedJwtException -> {
                    throw CommonException(ResponseCode.INVALID_ACCESS_TOKEN)
                }

                is ExpiredJwtException -> {
                    throw CommonException(ResponseCode.EXPIRED_ACCESS_TOKEN)
                }

                else -> {
                    throw CommonException(ResponseCode.INTERNAL_SERVER_ERROR)
                }
            }
        }

        return true
    }
}