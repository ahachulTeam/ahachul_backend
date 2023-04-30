package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostWriter
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import org.springframework.stereotype.Component

@Component
class CommunityPostPersistence(
    private val repository: CommunityPostRepository
): CommunityPostReader, CommunityPostWriter {

    override fun save(entity: CommunityPostEntity): CommunityPostEntity {
        return repository.save(entity)
    }
}