package backend.team.ahachul_backend.api.community.application.port.out

interface CommunityPostLikeReader {

    fun exist(postId: Long, memberId: Long): Boolean
}