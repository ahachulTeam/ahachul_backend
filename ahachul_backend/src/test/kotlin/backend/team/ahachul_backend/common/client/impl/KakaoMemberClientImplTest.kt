package backend.team.ahachul_backend.common.client.impl

import backend.team.ahachul_backend.common.client.KakaoMemberClient
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KakaoMemberClientImplTest(
        @Autowired val kakaoMemberClient: KakaoMemberClient
) {
    @Test
    @DisplayName("엑세스토큰_발급")
    fun 엑세스토큰_발급() {
        // given
        val code = "dye-i1t3Xy0TnryuPqetC8yvXSycfAkohGA7yZGRrv8VqNlA9vT6Dqeo6TfSxV3PQQnNgwoqJREAAAGHR3JO4w"

        // when
        val result = kakaoMemberClient.getAccessTokenByCode(code)

        // then
        println("dddd $result")
    }

    @Test
    @DisplayName("유저정보_가져오기")
    fun 유저정보_가져오기() {
        // given

        // when

        // then
    }
}