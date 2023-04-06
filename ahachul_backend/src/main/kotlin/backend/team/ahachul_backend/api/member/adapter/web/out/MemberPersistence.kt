package backend.team.ahachul_backend.api.member.adapter.web.out

import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberWriter
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.exception.AdapterException
import backend.team.ahachul_backend.common.response.ResponseCode
import org.springframework.stereotype.Component

@Component
class MemberPersistence(
        private val memberRepository: MemberRepository
): MemberReader, MemberWriter {

    override fun save(memberEntity: MemberEntity): Long? {
        return memberRepository.save(memberEntity).id
    }

    override fun getMember(memberId: Long): MemberEntity{
        return memberRepository.findById(memberId)
                .orElseThrow { throw AdapterException(ResponseCode.INVALID_DOMAIN) }
    }
}