package backend.team.ahachul_backend.api.member.domain

import backend.team.ahachul_backend.api.member.application.port.`in`.command.LoginMemberCommand
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.member.domain.model.UserStatus
import backend.team.ahachul_backend.common.dto.GoogleUserInfoDto
import backend.team.ahachul_backend.common.dto.KakaoMemberInfoDto

data class Member(
        var id: Long? = null,
        val nickname: String?,
        val providerUserId: String,
        val provider: ProviderType,
        val email: String?,
        val gender: GenderType?,
        val age: String?,
        val status: UserStatus
) {
    companion object {
        fun of(command: LoginMemberCommand, userInfo: KakaoMemberInfoDto): Member {
            return Member(
                    nickname = userInfo.kakaoAccount.profile?.nickname,
                    providerUserId = userInfo.id,
                    provider = command.providerType,
                    email = "", // TODO
                    gender = null, // TODO
                    age = userInfo.kakaoAccount.ageRange,
                    status = UserStatus.ACTIVE
            )
        }

//        fun of(command: LoginMemberCommand, userInfo: GoogleUserInfoDto): Member {
//            return Member(
//                nickname =
//                providerUserId =
//                provider = command.providerType,
//                email = "", // TODO
//                gender = null, // TODO
//                age = userInfo.kakaoAccount.ageRange,
//                status = UserStatus.ACTIVE
//            )
//        }
    }
}