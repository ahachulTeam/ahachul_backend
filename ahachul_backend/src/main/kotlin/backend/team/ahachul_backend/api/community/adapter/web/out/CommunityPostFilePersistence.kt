package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostFileReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostFileWriter
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostFileEntity
import org.springframework.stereotype.Component

@Component
class CommunityPostFilePersistence(
    private val repository: CommunityPostFileRepository,
): CommunityPostFileReader, CommunityPostFileWriter {

    override fun save(entity: CommunityPostFileEntity): CommunityPostFileEntity {
        return repository.save(entity)
    }
}