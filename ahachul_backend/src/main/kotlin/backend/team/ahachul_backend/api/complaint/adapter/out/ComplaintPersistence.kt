package backend.team.ahachul_backend.api.complaint.adapter.out

import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintWriter
import backend.team.ahachul_backend.api.complaint.domain.entity.ComplaintMessageHistoryEntity
import org.springframework.stereotype.Component

@Component
class ComplaintPersistence(
    private val repository: ComplaintMessageHistoryRepository,
): ComplaintWriter {

    override fun save(entity: ComplaintMessageHistoryEntity): ComplaintMessageHistoryEntity {
        return repository.save(entity)
    }
}