package backend.team.ahachul_backend.api.lost.adapter.web.out

import backend.team.ahachul_backend.api.lost.application.service.command.out.GetRecommendLostPostsCommand
import backend.team.ahachul_backend.api.lost.application.service.command.out.GetSliceLostPostsCommand
import backend.team.ahachul_backend.api.lost.domain.entity.CategoryEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.lost.domain.entity.QLostPostEntity.lostPostEntity
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostPostType
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.sql.Types
import java.time.LocalDateTime

@Repository
class CustomLostPostRepository(
    private val queryFactory: JPAQueryFactory,
    private val jdbcTemplate: JdbcTemplate
) {

    @Transactional
    fun saveAll(lostPosts: List<LostPostEntity>) {
        val currentTime = Timestamp.valueOf(LocalDateTime.now())

        val sql = "INSERT INTO tb_lost_post (member_id, subway_line_id, title, content, status, origin, type,\n" +
                "                          lost_type, storage, storage_number, created_at, created_by, updated_at, updated_by, page_url,\n" +
                "                          received_date, category_id, external_source_file_url)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"

        jdbcTemplate.batchUpdate(sql, lostPosts, lostPosts.size) { ps, lostPost ->
                lostPost.member?.let { ps.setLong(1, it.id) } ?: ps.setNull(1, Types.BIGINT)
                lostPost.subwayLine?.let { ps.setLong(2, it.id) } ?: ps.setNull(2, Types.BIGINT)
                ps.setString(3, lostPost.title)
                ps.setString(4, lostPost.content)
                ps.setString(5, LostStatus.PROGRESS.name)
                ps.setString(6, LostOrigin.LOST112.name)
                ps.setString(7, LostPostType.CREATED.name)
                ps.setString(8, LostType.ACQUIRE.name)
                ps.setString(9, lostPost.storage)
                ps.setString(10, lostPost.storageNumber)
                ps.setTimestamp(11, currentTime)
                ps.setString(12, "SYSTEM")
                ps.setTimestamp(13, currentTime)
                ps.setString(14, "SYSTEM")
                ps.setString(15, lostPost.pageUrl)
                lostPost.receivedDate?.let { ps.setTimestamp(16, Timestamp.valueOf(it)) } ?: ps.setNull(16, Types.VARCHAR)
                lostPost.category?.let { ps.setLong(17, it.id) } ?: ps.setNull(17, Types.BIGINT)
                lostPost.externalSourceFileUrl?.let { ps.setString(18, it) } ?: ps.setNull(18, Types.VARCHAR)
        }
    }

    fun searchLostPosts(command: GetSliceLostPostsCommand): Slice<LostPostEntity> {
        val pageable = PageRequest.of(0, command.pageSize)
        val response = queryFactory.selectFrom(lostPostEntity)
            .where(
                ltPostId(command.lostPostId),
                lostOriginEq(command.lostOrigin),
                subwayLineEq(command.subwayLine),
                lostTypeEq(command.lostType),
                titleAndContentLike(command.keyword),
            )
            .orderBy(lostPostEntity.id.desc())
            .limit((command.pageSize + 1).toLong())
            .fetch()

        return SliceImpl(response, pageable, hasNext(response, command.pageSize))
    }

    private fun hasNext(response: MutableList<LostPostEntity>, pageSize: Int): Boolean {
        return when {
            response.size > pageSize -> {
                response.removeAt(response.size - 1)
                true
            }
            else -> false
        }
    }

    fun searchRecommendPost(command: GetRecommendLostPostsCommand): List<LostPostEntity> {
        return queryFactory.selectFrom(lostPostEntity)
            .where(
                lostTypeEq(command.lostType),
                subwayLineEq(command.subwayLine),
                categoryEq(command.category)
            )
            .orderBy(Expressions.numberTemplate(Double::class.java, "function('rand')").asc())
            .limit(command.size)
            .fetch()
    }

    fun searchRandomPostNotEqualCategory(command: GetRecommendLostPostsCommand): List<LostPostEntity> {
        return queryFactory.selectFrom(lostPostEntity)
            .where(
                lostTypeEq(command.lostType),
                subwayLineEq(command.subwayLine),
                categoryNotEq(command.category)
            )
            .orderBy(Expressions.numberTemplate(Double::class.java, "function('rand')").asc())
            .limit(command.size)
            .fetch()
    }

    private fun ltPostId(postId: Long?) =
        postId?.let { lostPostEntity.id.lt(postId) }

    private fun lostOriginEq(lostOrigin: LostOrigin?) =
        lostOrigin?.let { lostPostEntity.origin.eq(lostOrigin) }

    private fun subwayLineEq(subwayLine: SubwayLineEntity?) =
        subwayLine?.let { lostPostEntity.subwayLine.eq(subwayLine) }

    private fun lostTypeEq(lostType: LostType?) =
        lostType?.let { lostPostEntity.lostType.eq(lostType) }

    private fun categoryEq(category: CategoryEntity?) =
        category?.let { lostPostEntity.category.eq(category) }

    private fun categoryNotEq(category: CategoryEntity?) =
        category?.let { lostPostEntity.category.ne(category) }

    private fun titleAndContentLike(keyword: String?) =
        keyword?.let {
            lostPostEntity.title.contains(keyword)
                .or(lostPostEntity.content.contains(keyword))
        }
}
