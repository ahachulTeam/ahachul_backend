package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.CreateCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.UpdateCommunityPostCommand
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import backend.team.ahachul_backend.common.model.CommunityPostType
import backend.team.ahachul_backend.common.model.RegionType
import jakarta.persistence.*

@Entity
class CommunityPostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_id")
    val id: Long? = null,

    var title: String,

    var content: String,

    var categoryType: CommunityCategoryType,

    var views: Int = 0,

    var status: CommunityPostType = CommunityPostType.CREATED,

    @Enumerated(EnumType.STRING)
    var regionType: RegionType = RegionType.METROPOLITAN,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: MemberEntity? = null,

): BaseEntity() {

    companion object {
        fun of(command: CreateCommunityPostCommand, memberEntity: MemberEntity): CommunityPostEntity {
            return CommunityPostEntity(
                title = command.title,
                content = command.content,
                categoryType = command.categoryType,
                member = memberEntity
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