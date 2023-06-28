package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostFileWriter
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostFileEntity
import org.springframework.stereotype.Component

@Component
class LostPostFilePersistence(
    private val repository: LostPostFileRepository
): LostPostFileWriter {

    override fun save(entity: LostPostFileEntity) {
        repository.save(entity)
    }
}