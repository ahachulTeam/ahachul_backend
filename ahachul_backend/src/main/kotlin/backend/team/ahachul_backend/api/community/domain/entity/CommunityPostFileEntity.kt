package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.common.domain.entity.FileEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CommunityPostFileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_category_file_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var commentPost: CommunityPostEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var file: FileEntity
): BaseEntity() {
}