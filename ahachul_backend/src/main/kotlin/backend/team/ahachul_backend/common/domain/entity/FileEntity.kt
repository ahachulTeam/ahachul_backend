package backend.team.ahachul_backend.common.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class FileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    val id: Long = 0,

    var fileName: String,

    var filePath: String
): BaseEntity() {

    companion object {
        fun of(fileName: String, filePath: String): FileEntity {
            return FileEntity(
                fileName = fileName,
                filePath = filePath
            )
        }
    }
}