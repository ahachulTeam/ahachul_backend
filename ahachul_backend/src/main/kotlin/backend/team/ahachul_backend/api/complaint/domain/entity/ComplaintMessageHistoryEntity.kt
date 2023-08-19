package backend.team.ahachul_backend.api.complaint.domain.entity

import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*
@Entity
class ComplaintMessageHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_message_history_id")
    var id: Long = 0,

    val sentContent: String,

    val sentPhoneNumber: String,

    val sentTrainNo: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_member_id")
    val member: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_subway_line_id")
    val subwayLine: SubwayLineEntity,
): BaseEntity() {
}