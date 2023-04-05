package backend.team.ahachul_backend.api.member.domain.entity

import backend.team.ahachul_backend.api.member.application.port.`in`.command.LoginMemberCommand
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.member.domain.model.UserStatus
import backend.team.ahachul_backend.common.dto.KakaoMemberInfoDto
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class MemberEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "member_id")
        val id: Long? = null,

        val nickname: String?,

        val providerUserId: String,

        @Enumerated(EnumType.STRING)
        val provider: ProviderType,

        val email: String?,

        @Enumerated(EnumType.ORDINAL)
        val gender: GenderType?,

        val age: String?, // TODO age -> ageRange

        @Enumerated(EnumType.STRING)
        val status: UserStatus
): BaseEntity() {

        companion object {
                fun of(command: LoginMemberCommand, userInfo: KakaoMemberInfoDto): MemberEntity {
                        return MemberEntity(
                                nickname = userInfo.kakaoAccount.profile?.nickname,
                                providerUserId = userInfo.id,
                                provider = command.providerType,
                                email = userInfo.kakaoAccount.email,
                                gender = userInfo.kakaoAccount.gender?.let { GenderType.of(it) },
                                age = userInfo.kakaoAccount.ageRange?.let { it.split("~")[0] },
                                status = UserStatus.ACTIVE
                        )
                }
        }
}