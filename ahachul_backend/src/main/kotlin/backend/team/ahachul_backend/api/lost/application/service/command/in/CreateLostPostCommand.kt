package backend.team.ahachul_backend.api.lost.application.service.command.`in`

import backend.team.ahachul_backend.api.lost.domain.model.LostType
import org.springframework.web.multipart.MultipartFile

class CreateLostPostCommand(
    val title: String,
    val content: String,
    val subwayLine: Long,
    val lostType: LostType,
    val categoryName: String,
    var imageFiles: List<MultipartFile>? = listOf()
) {
}
