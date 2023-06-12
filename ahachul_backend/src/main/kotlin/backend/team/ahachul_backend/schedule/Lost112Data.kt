package backend.team.ahachul_backend.schedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Lost112Data(
    val title: String = "",
    val getDate: String = "",
    val getPlace: String = "",
    val type: String = "",
    val receiptPlace: String = "",
    val storagePlace: String = "",
    val lostStatus: String = "",
    val phone: String = "",
    val context: String = "",
    @JsonProperty("image")
    val imageUrl: String = "",
    @JsonProperty("source")
    val origin: String = "",
    val page: String = ""
)