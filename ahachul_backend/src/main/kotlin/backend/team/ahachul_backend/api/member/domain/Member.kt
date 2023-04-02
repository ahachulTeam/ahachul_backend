package backend.team.ahachul_backend.api.member.domain

data class Member(
        private val id: Long,
        private val nickname: String,
        private val providerUserId: String,
        private val provider: String,
        private val email: String,
        private val gender: String,
        private val age: String
) {
}