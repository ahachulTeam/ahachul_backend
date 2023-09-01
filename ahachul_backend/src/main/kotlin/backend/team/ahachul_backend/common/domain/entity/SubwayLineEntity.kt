package backend.team.ahachul_backend.common.domain.entity

import backend.team.ahachul_backend.common.entity.BaseEntity
import backend.team.ahachul_backend.common.model.RegionType
import jakarta.persistence.*

@Entity
class SubwayLineEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_line_id")
    var id: Long = 0,

    var name: String,

    var phoneNumber: String = "",

    @Enumerated(EnumType.STRING)
    var regionType: RegionType,

    var identity: Long = 0

): BaseEntity() {

    fun processCorrectTrainNo(trainNo: String): String {
        val idx = 0
        if (trainNo[idx] != id.toString()[idx]) {
            return id.toString() + trainNo.substring(idx + 1, trainNo.length)
        }
        return trainNo
    }
}
