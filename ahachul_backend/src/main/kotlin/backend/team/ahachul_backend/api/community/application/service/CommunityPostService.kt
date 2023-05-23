package backend.team.ahachul_backend.api.community.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostWriter
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.common.persistence.SubwayLineReader
import backend.team.ahachul_backend.common.support.ViewsSupport
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommunityPostService(
    private val communityPostWriter: CommunityPostWriter,
    private val communityPostReader: CommunityPostReader,

    private val memberReader: MemberReader,
    private val subwayLineReader: SubwayLineReader,

    private val communityPostHashTagService: CommunityPostHashTagService,

    private val viewsSupport: ViewsSupport,
): CommunityPostUseCase {

    override fun searchCommunityPosts(command: SearchCommunityPostCommand): SearchCommunityPostDto.Response {
        val searchCommunityPosts = communityPostReader.searchCommunityPosts(command)
        val views = searchCommunityPosts.map {
            viewsSupport.get(it.id)
        }.toList()
        return SearchCommunityPostDto.Response.of(searchCommunityPosts, views)
    }

    override fun getCommunityPost(command: GetCommunityPostCommand): GetCommunityPostDto.Response {
        val communityPost = communityPostReader.getCommunityPost(command.id)
        val views = viewsSupport.increase(command.id)
        return GetCommunityPostDto.Response.of(communityPost, views)
    }

    @Transactional
    override fun createCommunityPost(command: CreateCommunityPostCommand): CreateCommunityPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val member = memberReader.getMember(memberId.toLong())
        val subwayLine = subwayLineReader.getSubwayLine(command.subwayLineId)
        val communityPost = communityPostWriter.save(CommunityPostEntity.of(command, member, subwayLine))
        communityPostHashTagService.createCommunityPostHashTag(communityPost, command.hashTags)
        return CreateCommunityPostDto.Response.from(communityPost)
    }

    @Transactional
    override fun updateCommunityPost(command: UpdateCommunityPostCommand): UpdateCommunityPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val communityPost = communityPostReader.getCommunityPost(command.id)
        communityPost.checkMe(memberId)
        communityPost.update(command)
        communityPostHashTagService.createCommunityPostHashTag(communityPost, command.hashTags)
        return UpdateCommunityPostDto.Response.from(communityPost)
    }

    @Transactional
    override fun deleteCommunityPost(command: DeleteCommunityPostCommand): DeleteCommunityPostDto.Response {
        val memberId = RequestUtils.getAttribute("memberId")!!
        val entity = communityPostReader.getCommunityPost(command.id)
        entity.checkMe(memberId)
        entity.delete()
        return DeleteCommunityPostDto.Response(entity.id)
    }
}