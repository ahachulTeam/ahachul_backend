package backend.team.ahachul_backend.api.member.domain.entity

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
        private val id: Long,

        private val nickname: String,

        private val providerUserId: String,

        @Enumerated(EnumType.STRING)
        private val provider: ProviderType,

        private val email: String,

        @Enumerated(EnumType.ORDINAL)
        private val gender: GenderType,

        private val age: String,

        @Enumerated(EnumType.STRING)
        private val status: UserStatus
): BaseEntity() {
}