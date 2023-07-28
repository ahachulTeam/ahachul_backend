package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostPostType
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.api.report.domain.ReportEntity
import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import backend.team.ahachul_backend.schedule.Lost112Data
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

    @OneToMany(mappedBy = "lostPost")
    var lostPostReports: MutableList<ReportEntity> = mutableListOf(),

    @OneToOne
    var category: CategoryEntity,

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

    var receivedDate: LocalDateTime? = null

): BaseEntity() {

    companion object {
        const val MIN_BLOCK_REPORT_COUNT = 5

        fun of(command: CreateLostPostCommand, member: MemberEntity,
               subwayLine: SubwayLineEntity, category: CategoryEntity
        ): LostPostEntity {
            return LostPostEntity(
                title = command.title,
                content = command.content,
                subwayLine = subwayLine,
                lostType = command.lostType,
                member = member,
                category = category
            )
        }

        fun ofLost112(data: Lost112Data, subwayLine: SubwayLineEntity?, category: CategoryEntity): LostPostEntity {
            return LostPostEntity(
                title = data.title,
                content = data.context,
                lostType = LostType.LOST,
                origin = LostOrigin.LOST112,
                storageNumber = data.phone,
                storage = data.storagePlace,
                subwayLine = subwayLine,
                pageUrl = data.page,
                receivedDate = LocalDateTime.parse(data.getDate,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH시경")),
                category = category
            )
        }
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

    fun hasDuplicateReportByMember(member: MemberEntity): Boolean{
        return lostPostReports.stream()
            .anyMatch {x -> x.sourceMember.id == member.id}
    }

    fun exceedMinReportCount(): Boolean {
        return lostPostReports.size >= MIN_BLOCK_REPORT_COUNT
    }

    fun block() {
        type = LostPostType.BLOCKED
    }

    val date: LocalDateTime
        get() = when (origin) {
            LostOrigin.LOST112 -> receivedDate!!
            else -> createdAt
        }
}
