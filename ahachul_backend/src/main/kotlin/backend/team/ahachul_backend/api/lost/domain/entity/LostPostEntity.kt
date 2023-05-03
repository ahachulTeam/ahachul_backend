package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.api.lost.application.service.command.CreateLostPostCommand
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class LostPostEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_post_entity_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: MemberEntity,

    var title: String,

    var content: String,

    @Enumerated(value = EnumType.STRING)
    var status: LostStatus = LostStatus.PROGRESS,

    @Enumerated(value = EnumType.STRING)
    var origin: LostOrigin = LostOrigin.APP,

    @Enumerated(value = EnumType.STRING)
    var lostType: LostType,

    var lostLine: String,

    var storage: String? = null,

    var storageNumber: String? = null

): BaseEntity() {

    companion object {
        fun of(command: CreateLostPostCommand, member: MemberEntity): LostPostEntity {
            return LostPostEntity(
                title = command.title,
                content = command.content,
                lostLine = command.lostLine,
                lostType = command.lostType,
                member = member
            )
        }
    }
}