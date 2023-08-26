package backend.team.ahachul_backend.api.common.adapter.domain.entity

import backend.team.ahachul_backend.common.domain.entity.SubwayLineEntity
import backend.team.ahachul_backend.common.entity.BaseEntity
import jakarta.persistence.*


@Entity
class StationEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    var id: Long = 0,

    var name: String,

    var identity: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_line_id")
    var subwayLineEntity: SubwayLineEntity

): BaseEntity() {

}
