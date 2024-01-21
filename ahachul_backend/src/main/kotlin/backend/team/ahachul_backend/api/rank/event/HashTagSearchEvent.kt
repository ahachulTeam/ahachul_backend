package backend.team.ahachul_backend.api.rank.event

data class HashTagSearchEvent(
    val hashTagName: String,
    val userId: String
) {
}
