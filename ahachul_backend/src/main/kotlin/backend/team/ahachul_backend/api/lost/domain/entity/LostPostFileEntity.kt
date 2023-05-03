package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.common.domain.entity.FileEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class LostPostFileEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_post_file_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var lostPost: LostPostEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var file: FileEntity

): BaseEntity() {
}