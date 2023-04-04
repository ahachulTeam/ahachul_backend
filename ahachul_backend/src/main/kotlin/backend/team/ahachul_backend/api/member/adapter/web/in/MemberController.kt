package backend.team.ahachul_backend.api.member.adapter.web.`in`

import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.common.response.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
        private val memberUseCase: MemberUseCase
) {

    @GetMapping("/v1/members/{memberId}/is-add-profiles")
    fun isAddProfiles(@PathVariable memberId: Long): CommonResponse<*>? {
        return null
    }

    @PatchMapping("/v1/members/{memberId}")
    fun updateMember(@PathVariable memberId: Long): CommonResponse<*>? {
        return null
    }
}