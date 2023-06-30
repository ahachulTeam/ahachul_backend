package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostLikeUseCase
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostLikeReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostLikeWriter
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostLikeEntity
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.exception.CommonException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service

@Service
class CommunityPostLikeService (
    private val communityPostLikeWriter: CommunityPostLikeWriter,
    private val communityPostLikeReader: CommunityPostLikeReader,

    private val communityPostReader: CommunityPostReader,
    private val memberReader: MemberReader,

): CommunityPostLikeUseCase {

    override fun createCommunityPostLike(postId: Long) {
        val memberId = RequestUtils.getAttribute("memberId")!!.toLong()
        if (communityPostLikeReader.exist(postId, memberId)) {
            throw CommonException(ResponseCode.ALREADY_LIKED_POST)
        }
        communityPostLikeWriter.save(
            CommunityPostLikeEntity.of(
                communityPost = communityPostReader.getCommunityPost(postId),
                member = memberReader.getMember(memberId),
            )
        )
    }

    override fun deleteCommunityPostLike(postId: Long) {
        val memberId = RequestUtils.getAttribute("memberId")!!
        communityPostLikeWriter.delete(
            postId = postId,
            memberId = memberId.toLong()
        )
    }
}