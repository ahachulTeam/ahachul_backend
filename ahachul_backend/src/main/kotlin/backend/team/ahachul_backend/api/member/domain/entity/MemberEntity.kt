package backend.team.ahachul_backend.api.member.domain.entity

import backend.team.ahachul_backend.api.member.domain.Member
import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.member.domain.model.UserStatus
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

}