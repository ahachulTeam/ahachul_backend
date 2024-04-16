package backend.team.ahachul_backend.api.complaint.adapter.out

import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintFileReader
import backend.team.ahachul_backend.api.complaint.application.port.out.ComplaintFileWriter
import backend.team.ahachul_backend.api.complaint.domain.entity.ComplaintMessageHistoryFileEntity
import org.springframework.stereotype.Component

@Component
class ComplaintFilePersistence(
    private val repository: ComplaintMessageHistoryFileRepository,
): ComplaintFileWriter, ComplaintFileReader {

    override fun save(entity: ComplaintMessageHistoryFileEntity): ComplaintMessageHistoryFileEntity {
        return repository.save(entity)
    }

    override fun findAllByComplaintId(complaintId: Long): List<ComplaintMessageHistoryFileEntity> {
        return repository.findAllByComplaintMessageHistoryIdOrderById(complaintId)
    }
}