package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CommunityCommentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    val id: Long = 0,

    var content: String,

    @OneToOne(fetch = FetchType.LAZY)
    var upperCommunityComment: CommunityCommentEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    var communityPost: CommunityPostEntity,
): BaseEntity() {
}