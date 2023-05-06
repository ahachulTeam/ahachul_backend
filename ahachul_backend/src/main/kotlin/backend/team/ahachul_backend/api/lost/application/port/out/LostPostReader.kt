package backend.team.ahachul_backend.api.lost.application.port.out

import backend.team.ahachul_backend.api.lost.application.service.command.GetSliceLostPostsCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface LostPostReader {

    fun getLostPost(id: Long): LostPostEntity

    fun getLostPosts(pageable: Pageable, command: GetSliceLostPostsCommand): Slice<LostPostEntity>
}