package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostWriter
import org.springframework.stereotype.Component

@Component
class CommunityPostPersistence: CommunityPostReader, CommunityPostWriter {
}