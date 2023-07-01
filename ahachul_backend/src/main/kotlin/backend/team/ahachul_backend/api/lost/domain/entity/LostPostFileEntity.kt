package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.common.domain.entity.FileEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class LostPostFileEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_post_file_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    var lostPost: LostPostEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    var file: FileEntity? = null

): BaseEntity() {

    companion object {
        fun from(lostPost: LostPostEntity, file: FileEntity): LostPostFileEntity {
            val entity = LostPostFileEntity(lostPost = lostPost)
            entity.addFile(file)
            return entity
        }
    }

    fun addFile(file: FileEntity) {
        this.file = file
        file.lostPostFiles.add(this)
    }
}