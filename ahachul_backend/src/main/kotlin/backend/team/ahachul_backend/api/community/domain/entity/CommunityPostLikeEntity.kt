package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CommunityPostLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_like_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id")
    var communityPost: CommunityPostEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: MemberEntity,

    ): BaseEntity() {

        companion object {

            fun of(communityPost: CommunityPostEntity, member: MemberEntity): CommunityPostLikeEntity {
                return CommunityPostLikeEntity(
                    communityPost = communityPost,
                    member = member,
                )
            }
        }
}