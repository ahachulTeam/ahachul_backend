package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityCommentUseCase
import backend.team.ahachul_backend.api.community.application.port.out.CommunityCommentReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityCommentWriter
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.domain.entity.CommunityCommentEntity
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommunityCommentService(
    private val communityCommentWriter: CommunityCommentWriter,
    private val communityCommentReader: CommunityCommentReader,
    private val communityPostReader: CommunityPostReader,
    private val memberReader: MemberReader,
): CommunityCommentUseCase {

    override fun getCommunityComments(command: GetCommunityCommentsCommand): GetCommunityCommentsDto.Response {
        val comments = communityCommentReader.findAllByPostId(command.postId)
            .map {
                GetCommunityCommentsDto.CommunityComment(
                    it.id,
                    it.upperCommunityComment?.id,
                    it.content,
                    it.createdAt,
                    it.createdBy,
                    it.member.nickname!!
                )
            }
            .toList()
        return GetCommunityCommentsDto.Response(comments)
    }

    @Transactional
    override fun createCommunityComment(command: CreateCommunityCommentCommand): CreateCommunityCommentDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val upperCommunityComment = command.upperCommentId?.let { communityCommentReader.findById(it) }
        val communityPost = communityPostReader.getCommunityPost(command.postId)
        val member = memberReader.getMember(memberId.toLong())
        val entity = communityCommentWriter.save(CommunityCommentEntity.of(command, upperCommunityComment, communityPost, member))
        return CreateCommunityCommentDto.Response.from(entity)
    }

    @Transactional
    override fun updateCommunityComment(command: UpdateCommunityCommentCommand): UpdateCommunityCommentDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val communityComment = communityCommentReader.getById(command.id)
        communityComment.checkMe(memberId)
        communityComment.update(command.content)
        return UpdateCommunityCommentDto.Response.from(communityComment)
    }

    @Transactional
    override fun deleteCommunityComment(command: DeleteCommunityCommentCommand): DeleteCommunityCommentDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val communityComment = communityCommentReader.getById(command.id)
        communityComment.checkMe(memberId)
        communityComment.delete()
        return DeleteCommunityCommentDto.Response(communityComment.id)
    }
}