package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.application.port.out.LostPostReader
import backend.team.ahachul_backend.api.lost.application.port.out.LostPostWriter
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Repository

@Repository
class LostPostPersistence(
    private val lostPostRepository: LostPostRepository
): LostPostWriter, LostPostReader {

    override fun save(lostPostEntity: LostPostEntity): LostPostEntity {
        return lostPostRepository.save(lostPostEntity)
    }

    override fun getLostPost(id: Long): LostPostEntity {
        return lostPostRepository.findById(id)
            .orElseThrow { throw AdapterException(ResponseCode.INVALID_DOMAIN) }
    }
}