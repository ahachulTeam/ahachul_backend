package backend.team.ahachul_backend.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleUserInfoDto (
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("verifiedEmail") val verifiedEmail: Boolean,
    @JsonProperty("givenName") val givenName: String,
    @JsonProperty("familyName") val familyName: String,
    @JsonProperty("picture") val picture: String,
    @JsonProperty("locale") val locale: String
)