package backend.team.ahachul_backend.api.report.domain

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.lost.domain.entity.LostPostEntity
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*


@Entity
class ReportEntity (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_report_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_member_id")
    val sourceMember: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    val targetMember: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id")
    val communityPost: CommunityPostEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    val lostPost: LostPostEntity? = null

): BaseEntity() {

    companion object {
        fun from(sourceMember: MemberEntity, targetMember: MemberEntity, target: Any): ReportEntity {
            return ReportEntity(
                sourceMember = sourceMember,
                targetMember = targetMember,
                communityPost = if (target is CommunityPostEntity) target else null,
                lostPost = if (target is LostPostEntity) target else null
            )
        }
    }
}