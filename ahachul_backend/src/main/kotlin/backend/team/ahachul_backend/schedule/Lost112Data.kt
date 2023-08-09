package backend.team.ahachul_backend.schedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Lost112Data(
    val title: String = "",
    val getDate: String = "",
    val getPlace: String = "",
    @field:JsonProperty("type")
    val categoryName: String = "",
    val receiptPlace: String = "",
    val storagePlace: String = "",
    val lostStatus: String = "",
    val phone: String = "",
    val context: String = "",
    @field:JsonProperty("image")
    val imageUrl: String = "",
    @field:JsonProperty("source")
    val origin: String = "",
    val page: String = ""
)
