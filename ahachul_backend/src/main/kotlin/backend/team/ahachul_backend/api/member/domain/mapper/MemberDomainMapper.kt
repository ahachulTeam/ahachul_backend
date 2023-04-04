package backend.team.ahachul_backend.api.member.domain.mapper

import backend.team.ahachul_backend.api.member.domain.Member
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity

class MemberDomainMapper {

    companion object {
        fun toEntity(member: Member): MemberEntity {
            return MemberEntity(
                    nickname = member.nickname,
                    providerUserId = member.providerUserId,
                    provider = member.provider,
                    email = member.email,
                    gender = member.gender,
                    age = member.age,
                    status = member.status
            )
        }
    }
}