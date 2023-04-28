package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.api.member.domain.entity.MemberEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

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

    var date: LocalDateTime,

    @Enumerated(value = EnumType.STRING)
    var status: LostStatus,

    @Enumerated(value = EnumType.STRING)
    var origin: LostOrigin,

    @Enumerated(value = EnumType.STRING)
    var LostType: LostType,

    var lostLine: String,

    var storage: String?,

    var storageNumber: String?

): BaseEntity() {
}