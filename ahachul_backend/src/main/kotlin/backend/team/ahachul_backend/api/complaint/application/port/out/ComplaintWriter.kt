package backend.team.ahachul_backend.api.complaint.application.port.out

import backend.team.ahachul_backend.api.complaint.domain.entity.ComplaintMessageHistoryEntity

interface ComplaintWriter {

    fun save(complaintMessageHistory: ComplaintMessageHistoryEntity): ComplaintMessageHistoryEntity
}