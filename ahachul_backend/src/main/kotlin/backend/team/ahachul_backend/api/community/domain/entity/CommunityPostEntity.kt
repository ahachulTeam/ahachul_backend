package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.CreateCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.UpdateCommunityPostCommand
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityPostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.model.RegionType
import jakarta.persistence.*

@Entity
class CommunityPostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_id")
    val id: Long = 0,

    var title: String,

    var content: String,

    @Enumerated(EnumType.STRING)
    var categoryType: CommunityCategoryType,

    var views: Int = 0,

    @Enumerated(EnumType.STRING)
    var status: CommunityPostType = CommunityPostType.CREATED,

    @Enumerated(EnumType.STRING)
    var regionType: RegionType = RegionType.METROPOLITAN,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: MemberEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_line_id")
    var subwayLineEntity: SubwayLineEntity

    ): BaseEntity() {

    companion object {
        fun of(command: CreateCommunityPostCommand, memberEntity: MemberEntity, subwayLineEntity: SubwayLineEntity): CommunityPostEntity {
            return CommunityPostEntity(
                title = command.title,
                content = command.content,
                categoryType = command.categoryType,
                member = memberEntity,
                subwayLineEntity = subwayLineEntity
            )
        }
    }

    fun update(command: UpdateCommunityPostCommand) {
        title = command.title
        content = command.content
        categoryType = command.categoryType
    }

    fun delete() {
        status = CommunityPostType.DELETED
    }
}