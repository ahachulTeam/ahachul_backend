package backend.team.ahachul_backend.api.member.adapter.web.out

import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.member.domain.entity.MemberStationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberStationRepository: JpaRepository<MemberStationEntity, Long> {

    fun deleteAllByMember(member: MemberEntity)
}
