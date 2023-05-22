package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostPostType
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import backend.team.ahachul_backend.common.schedule.LostDataDto
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
class LostPostEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_post_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: MemberEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_line_id")
    var subwayLine: SubwayLineEntity?,

    var title: String,

    var content: String,

    @Enumerated(value = EnumType.STRING)
    var status: LostStatus = LostStatus.PROGRESS,

    @Enumerated(value = EnumType.STRING)
    var origin: LostOrigin = LostOrigin.AHACHUL,

    @Enumerated(value = EnumType.STRING)
    var lostType: LostType,

    @Enumerated(value = EnumType.STRING)
    var type: LostPostType = LostPostType.CREATED,

    var storage: String? = null,

    var storageNumber: String? = null,

    var pageUrl: String? = null,

    var receivedDate: LocalDateTime = LocalDateTime.now()

): BaseEntity() {

    companion object {
        fun of(command: CreateLostPostCommand, member: MemberEntity, subwayLine: SubwayLineEntity): LostPostEntity {
            return LostPostEntity(
                title = command.title,
                content = command.content,
                subwayLine = subwayLine,
                lostType = command.lostType,
                member = member
            )
        }

//        fun ofLost112(it: LostDataDto): LostPostEntity {
////            return LostPostEntity(
////                title = it.title,
////                content = it.context,
////                lostType = LostType.LOST,
////                origin = LostOrigin.LOST112,
////                storageNumber = it.phone,
////                storage = it.storagePlace,
////                subwayLine = extractSubwayLine(it.receiptPlace),
////                pageUrl = it.page,
////                receivedDate = LocalDateTime.parse(it.getDate,
////                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH시경"))
//            )
//        }
    }

    fun update(command: UpdateLostPostCommand, subwayLine: SubwayLineEntity?) {
        command.title?.let { this.title = it }
        command.content?.let { this.content = it }
        command.status?.let { this.status= it }
        subwayLine?.let { this.subwayLine = subwayLine }
    }

    fun delete() {
        type = LostPostType.DELETED
    }

    val date: LocalDateTime
        get() = when (origin) {
            LostOrigin.LOST112 -> receivedDate
            else -> createdAt
        }
}