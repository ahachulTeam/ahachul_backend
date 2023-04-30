package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.CreateCommunityPostCommand
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.posts.UpdateCommunityPostCommand
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.entity.BaseEntity
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

    @Enumerated(EnumType.STRING)
    var regionType: RegionType = RegionType.METROPOLITAN

): BaseEntity() {

    companion object {
        fun from(command: CreateCommunityPostCommand): CommunityPostEntity {
            return CommunityPostEntity(
                title = command.title,
                content = command.content,
                categoryType = command.categoryType,
            )
        }
    }

    fun update(command: UpdateCommunityPostCommand) {
        title = command.title
        content = command.content
        categoryType = command.categoryType
    }
}