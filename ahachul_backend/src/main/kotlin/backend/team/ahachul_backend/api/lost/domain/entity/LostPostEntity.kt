package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.application.service.command.UpdateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostPostType
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostCategory
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class LostPostEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_post_entity_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var subwayLine: SubwayLine,

    var title: String,

    var content: String,

    @Enumerated(value = EnumType.STRING)
    var status: LostStatus = LostStatus.PROGRESS,

    @Enumerated(value = EnumType.STRING)
    var origin: LostOrigin = LostOrigin.APP,

    @Enumerated(value = EnumType.STRING)
    var lostCategory: LostCategory,

    @Enumerated(value = EnumType.STRING)
    var type: LostPostType,

    var storage: String? = null,

    var storageNumber: String? = null

): BaseEntity() {

    companion object {
        fun of(command: CreateLostPostCommand, member: MemberEntity, subwayLine: SubwayLine): LostPostEntity {
            return LostPostEntity(
                title = command.title,
                content = command.content,
                subwayLine = subwayLine,
                lostCategory = command.lostCategory,
                member = member,
                type = LostPostType.ACTIVE
            )
        }
    }

    fun update(command: UpdateLostPostCommand, subwayLine: SubwayLine?) {
        command.title?.let { this.title = it }
        command.content?.let { this.content = it }
        command.status?.let { this.status= it }
        subwayLine?.let { this.subwayLine = subwayLine }
    }

    fun delete() {
        type = LostPostType.DELETED
    }
}