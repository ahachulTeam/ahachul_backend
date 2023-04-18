package backend.team.ahachul_backend.api.community.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class CommunityCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_category_id")
    val id: Long? = null,

    val name: String
): BaseEntity() {
}