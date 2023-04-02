package backend.team.ahachul_backend.api.member.domain

import backend.team.ahachul_backend.api.member.domain.model.GenderType
import backend.team.ahachul_backend.api.member.domain.model.ProviderType
import backend.team.ahachul_backend.api.member.domain.model.UserStatus

data class Member(
        private val id: Long,
        private val nickname: String,
        private val providerUserId: String,
        private val provider: ProviderType,
        private val email: String,
        private val gender: GenderType,
        private val age: String,
        private val status: UserStatus
) {
}