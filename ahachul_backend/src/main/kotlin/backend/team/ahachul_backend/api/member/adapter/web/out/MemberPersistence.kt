package backend.team.ahachul_backend.api.member.adapter.web.out

import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberWriter
import org.springframework.stereotype.Component

@Component
class MemberPersistence: MemberReader, MemberWriter {
}