package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.application.service.command.GetSliceLostPostsCommand
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.entity.QLostPostEntity.lostPostEntity
import backend.team.ahachul_backend.api.lost.domain.entity.SubwayLine
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class CustomLostPostRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun searchLostPostByFilter(pageable: Pageable, command: GetSliceLostPostsCommand): Slice<LostPostEntity> {
        val response = queryFactory.selectFrom(lostPostEntity)
            .where(
                lostOriginEq(command.lostOrigin),
                subwayLineEq(command.subwayLine),
                lostTypeEq(command.lostType)
            )
            .orderBy(lostPostEntity.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return SliceImpl(response, pageable, hasNext(response, pageable))
    }

    private fun hasNext(response: List<LostPostEntity>, pageable: Pageable): Boolean {
        return when {
            response.size > pageable.offset + pageable.pageSize -> true
            else -> false
        }
    }

    private fun lostOriginEq(lostOrigin: LostOrigin?) =
        lostOrigin?.let { lostPostEntity.origin.eq(lostOrigin) }

    private fun subwayLineEq(subwayLine: SubwayLine?) =
        subwayLine?.let { lostPostEntity.subwayLine.eq(subwayLine) }

    private fun lostTypeEq(lostType: LostType?) =
        lostType?.let { lostPostEntity.lostType.eq(lostType) }
}