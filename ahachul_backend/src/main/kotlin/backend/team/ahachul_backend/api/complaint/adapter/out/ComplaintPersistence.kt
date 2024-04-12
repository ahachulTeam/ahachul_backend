package backend.team.ahachul_backend.api.complaint.adapter.out

import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintReader
import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintWriter
import backend.team.ahachul_backend.api.complaint.domain.entity.ComplaintMessageHistoryEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class ComplaintPersistence(
    private val repository: ComplaintMessageHistoryRepository,
): ComplaintWriter, ComplaintReader {

    override fun save(entity: ComplaintMessageHistoryEntity): ComplaintMessageHistoryEntity {
        return repository.save(entity)
    }

    override fun findAll(pageable: Pageable): Page<ComplaintMessageHistoryEntity> {
        return repository.findAll(pageable)
    }
}