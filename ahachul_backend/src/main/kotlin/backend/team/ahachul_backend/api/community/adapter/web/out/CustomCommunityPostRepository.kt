package backend.team.ahachul_backend.api.community.adapter.web.out

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.SearchCommunityPostCommand
import backend.team.ahachul_backend.api.community.domain.CommunityPost
import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.community.domain.entity.QCommunityPostEntity.communityPostEntity
import backend.team.ahachul_backend.api.community.domain.entity.QCommunityPostHashTagEntity.communityPostHashTagEntity
import backend.team.ahachul_backend.api.community.domain.entity.QCommunityPostLikeEntity.communityPostLikeEntity
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.api.member.domain.entity.QMemberEntity.memberEntity
import backend.team.ahachul_backend.common.domain.entity.QHashTagEntity.hashTagEntity
import backend.team.ahachul_backend.common.domain.entity.QSubwayLineEntity.subwayLineEntity
import backend.team.ahachul_backend.common.model.YNType
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.ExpressionUtils.count
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class CustomCommunityPostRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun getByCustom(postId: Long, memberId: String?): CommunityPost? {
        return queryFactory.select(
            Projections.constructor(
                CommunityPost::class.java,
                communityPostEntity.id,
                communityPostEntity.title,
                communityPostEntity.content,
                communityPostEntity.categoryType,
                ExpressionUtils.`as`(
                    JPAExpressions.select(count(communityPostLikeEntity.id))
                        .from(communityPostLikeEntity)
                        .where(
                            communityPostLikeEntity.communityPost.id.eq(postId)
                                .and(communityPostLikeEntity.likeYn.eq(YNType.Y))
                        ),
                    "likeCnt"
                ),
                ExpressionUtils.`as`(
                    JPAExpressions.select(count(communityPostLikeEntity.id))
                        .from(communityPostLikeEntity)
                        .where(
                            communityPostLikeEntity.communityPost.id.eq(postId)
                                .and(communityPostLikeEntity.likeYn.eq(YNType.N))
                        ),
                    "hateCnt"
                ),
                if (memberId != null) {
                    ExpressionUtils.`as`(
                        JPAExpressions.select(JPAExpressions.selectOne())
                            .from(communityPostLikeEntity)
                            .where(
                                communityPostLikeEntity.communityPost.id.eq(postId)
                                    .and(communityPostLikeEntity.likeYn.eq(YNType.N))
                                    .and(communityPostLikeEntity.member.id.eq(memberId.toLong()))
                            )
                            .exists(),
                        "likeYn"
                    )
                } else {
                    Expressions.constant(false)
                },
                if (memberId != null) {
                    ExpressionUtils.`as`(
                        JPAExpressions.select(JPAExpressions.selectOne())
                            .from(communityPostLikeEntity)
                            .where(
                                communityPostLikeEntity.communityPost.id.eq(postId)
                                    .and(communityPostLikeEntity.likeYn.eq(YNType.N))
                                    .and(communityPostLikeEntity.member.id.eq(memberId.toLong()))
                            )
                            .exists(),
                        "hateYn"
                    )
                } else {
                    Expressions.constant(false)
                },
                communityPostEntity.regionType,
                communityPostEntity.createdAt,
                communityPostEntity.createdBy,
                communityPostEntity.member.nickname.`as`("writer"),
            )
        )
            .from(communityPostEntity)
            .join(communityPostEntity.member, memberEntity)
            .where(communityPostEntity.id.eq(postId))
            .fetchOne()
    }

    fun searchCommunityPosts(command: SearchCommunityPostCommand): Slice<CommunityPostEntity> {
        val pageable = command.pageable
        var result = queryFactory.selectFrom(communityPostEntity)
            .join(communityPostEntity.member, memberEntity).fetchJoin()
            .join(communityPostEntity.subwayLineEntity, subwayLineEntity).fetchJoin()
            .where(
                categoryTypeEq(command.categoryType),
                subwayLineIdEq(command.subwayLineId),
                hashTagEqWithSubQuery(command.hashTag),
                titleOrContentContains(command.content),
            )
            .orderBy(getOrder(pageable))
            .offset(getOffset(pageable).toLong())
            .limit(pageable.pageSize + 1L)
            .fetch()

        val hasNext = hasNext(result, pageable)
        result = if (hasNext) result.dropLast(1) else result

        return SliceImpl(result, pageable, hasNext)
    }

    private fun getOrder(pageable: Pageable): OrderSpecifier<*>? {
        if (pageable.sort.isUnsorted) return communityPostEntity.createdAt.desc()

        val property = pageable.sort.toList()[0].property
        val direction = pageable.sort.toList()[0].direction
        val path = when (property) {
//            "likes" -> TODO
            "createdAt" -> communityPostEntity.createdAt
            "views" -> communityPostEntity.views
            else -> communityPostEntity.createdAt
        }
        return if (direction.isAscending) path.asc() else path.desc()
    }

    private fun getOffset(pageable: Pageable): Int {
        return when {
            pageable.pageNumber != 0 -> (pageable.pageNumber * pageable.pageSize) + 1
            else -> pageable.pageNumber
        }
    }

    private fun hasNext(result: MutableList<CommunityPostEntity>, pageable: Pageable): Boolean {
        return result.size > pageable.pageSize
    }

    private fun categoryTypeEq(categoryType: CommunityCategoryType?) =
        categoryType?.let { communityPostEntity.categoryType.eq(categoryType) }

    private fun subwayLineIdEq(subwayLineId: Long?) =
        subwayLineId?.let { communityPostEntity.subwayLineEntity.id.eq(subwayLineId) }

    private fun hashTagEqWithSubQuery(hashTag: String?) =
        hashTag?.let {
            communityPostEntity.`in`(
                JPAExpressions.select(communityPostHashTagEntity.communityPost)
                    .from(communityPostHashTagEntity)
                    .join(communityPostHashTagEntity.hashTag, hashTagEntity)
                    .where(hashTagEntity.name.eq(hashTag))
            )
        }

    private fun titleOrContentContains(content: String?) =
        content?.let { communityPostEntity.title.contains(content).or(communityPostEntity.content.contains(content)) }
}