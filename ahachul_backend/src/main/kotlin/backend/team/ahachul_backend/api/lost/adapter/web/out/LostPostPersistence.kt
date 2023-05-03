package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import org.springframework.stereotype.Repository

@Repository
class LostPostPersistence(
    private val lostPostRepository: LostPostRepository
): LostPostWriter {

    override fun save(lostPostEntity: LostPostEntity): LostPostEntity {
        return lostPostRepository.save(lostPostEntity)
    }
}