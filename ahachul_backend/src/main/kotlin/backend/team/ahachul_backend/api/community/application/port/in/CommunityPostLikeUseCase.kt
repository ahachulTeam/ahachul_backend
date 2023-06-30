package backend.team.ahachul_backend.api.community.application.port.`in`

interface CommunityPostLikeUseCase {

    fun createCommunityPostLike(postId: Long)

    fun deleteCommunityPostLike(postId: Long)
}