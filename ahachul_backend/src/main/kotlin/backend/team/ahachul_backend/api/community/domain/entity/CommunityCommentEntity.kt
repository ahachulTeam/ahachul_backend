package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.comment.CreateCommunityCommentCommand
import backend.team.ahachul_backend.api.community.domain.model.CommunityCommentType
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CommunityCommentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    val id: Long = 0,

    var content: String,

    var status: CommunityCommentType = CommunityCommentType.CREATED,

    @OneToOne(fetch = FetchType.LAZY)
    var upperCommunityComment: CommunityCommentEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    var communityPost: CommunityPostEntity,
): BaseEntity() {

    companion object {

        fun of(command: CreateCommunityCommentCommand, communityCommentEntity: CommunityCommentEntity?,communityPostEntity: CommunityPostEntity): CommunityCommentEntity {
            return CommunityCommentEntity(
                content = command.content,
                upperCommunityComment = communityCommentEntity,
                communityPost = communityPostEntity
            )
        }
    }

    fun update(content: String) {
        this.content = content
    }

    fun delete() {
        status = CommunityCommentType.DELETED
    }
}