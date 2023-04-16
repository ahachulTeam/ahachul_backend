package backend.team.ahachul_backend.common.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class FileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    val id: Long? = null,

    var fileName: String,

    var filePath: String
): BaseEntity() {
}