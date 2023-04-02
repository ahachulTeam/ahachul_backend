package backend.team.ahachul_backend.api.member.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class MemberEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "member_id")
        private val id: Long,
        private val nickname: String,
        private val providerUserId: String,
        private val provider: String,
        private val email: String,
        private val gender: String,
        private val age: String
): BaseEntity() {
}