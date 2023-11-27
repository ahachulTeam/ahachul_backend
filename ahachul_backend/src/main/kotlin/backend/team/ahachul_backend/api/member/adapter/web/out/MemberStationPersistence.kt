package backend.team.ahachul_backend.api.member.adapter.web.out

import backend.team.ahachul_backend.api.member.application.port.out.MemberStationWriter
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.entity.MemberStationEntity
import org.springframework.stereotype.Component

@Component
class MemberStationPersistence (
    private val memberStationRepository: MemberStationRepository
): MemberStationWriter {

    override fun deleteByMember(member: MemberEntity) {
        memberStationRepository.deleteAllByMember(member)
    }

    override fun save(entity: MemberStationEntity): MemberStationEntity {
        return memberStationRepository.save(entity)
    }

}
