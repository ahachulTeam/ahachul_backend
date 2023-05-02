package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.application.port.out.CommunityCommentReader
import backend.team.ahachul_backend.api.community.application.port.out.CommunityCommentWriter
import org.springframework.stereotype.Component

@Component
class CommunityCommentPersistence(
    repository: CommunityCommentRepository
): CommunityCommentWriter, CommunityCommentReader {
}