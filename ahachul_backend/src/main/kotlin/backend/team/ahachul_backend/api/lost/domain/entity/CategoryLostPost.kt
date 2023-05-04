package backend.team.ahachul_backend.api.lost.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CategoryLostPost(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_lost_post_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var lostPost: LostPostEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var category: Category
): BaseEntity() {
}