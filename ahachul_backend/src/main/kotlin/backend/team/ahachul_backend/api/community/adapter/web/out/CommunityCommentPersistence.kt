package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.application.port.out.CommunityCommentReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityCommentWriter
import backend.team.ahachul_backend.api.community.domain.entity.CommunityCommentEntity
import org.springframework.stereotype.Component

@Component
class CommunityCommentPersistence(
    private val repository: CommunityCommentRepository
): CommunityCommentWriter, CommunityCommentReader {

    override fun save(entity: CommunityCommentEntity): CommunityCommentEntity {
        return repository.save(entity)
    }

    override fun findById(id: Long): CommunityCommentEntity? {
        return repository.findById(id).orElse(null)
    }
}