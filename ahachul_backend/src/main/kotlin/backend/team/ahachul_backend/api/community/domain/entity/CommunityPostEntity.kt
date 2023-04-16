package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.common.domain.entity.RegionEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CommunityPostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_id")
    val id: Long? = null,

    var title: String,

    var content: String,

    var views: Int,

    @OneToOne(fetch = FetchType.LAZY)
    var region: RegionEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var communityCategory: CommunityCategoryEntity
): BaseEntity() {
}