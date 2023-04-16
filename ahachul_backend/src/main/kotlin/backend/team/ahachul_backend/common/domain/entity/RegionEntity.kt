package backend.team.ahachul_backend.common.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class RegionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    val id: Long? = null,

    val name: String
): BaseEntity() {
}