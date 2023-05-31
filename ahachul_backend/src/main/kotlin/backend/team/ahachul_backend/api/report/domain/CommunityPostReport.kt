package backend.team.ahachul_backend.api.report.domain

import backend.team.ahachul_backend.api.community.domain.entity.CommunityPostEntity
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*


@Entity
class CommunityPostReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_report_id")
    var id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_member_id")
    val sourceMember: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    val targetMember: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id")
    val communityPost: CommunityPostEntity

): BaseEntity() {

}