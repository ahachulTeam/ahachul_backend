package backend.team.ahachul_backend.api.member.adapter.web.out

import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberWriter
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import org.springframework.stereotype.Component

@Component
class MemberPersistence(
        private val memberRepository: MemberRepository
): MemberReader, MemberWriter {

    override fun save(memberEntity: MemberEntity): Long? {
        return memberRepository.save(memberEntity).id
    }
}