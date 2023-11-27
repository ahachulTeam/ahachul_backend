package backend.team.ahachul_backend.api.member.application.service

import backend.team.ahachul_backend.api.common.application.port.out.StationReader
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.BookmarkStationDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.CheckNicknameDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.GetMemberDto
import backend.team.ahachul_backend.api.member.adapter.web.`in`.dto.UpdateMemberDto
import backend.team.ahachul_backend.api.member.application.port.`in`.MemberUseCase
import backend.team.ahachul_backend.api.member.application.port.`in`.command.BookmarkStationCommand
import backend.team.ahachul_backend.api.member.application.port.`in`.command.CheckNicknameCommand
import backend.team.ahachul_backend.api.member.application.port.`in`.command.UpdateMemberCommand
import backend.team.ahachul_backend.api.member.application.port.out.MemberReader
import backend.team.ahachul_backend.api.member.application.port.out.MemberStationWriter
import backend.team.ahachul_backend.api.member.domain.entity.MemberStationEntity
import backend.team.ahachul_backend.common.exception.BusinessException
import backend.team.ahachul_backend.common.response.ResponseCode
import backend.team.ahachul_backend.common.utils.RequestUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberReader: MemberReader,
    private val stationReader: StationReader,
    private val memberStationWriter: MemberStationWriter
) : MemberUseCase {

    override fun getMember(): GetMemberDto.Response {
        val member = memberReader.getMember(RequestUtils.getAttribute("memberId")!!.toLong())
        return GetMemberDto.Response.of(member)
    }

    @Transactional
    override fun updateMember(command: UpdateMemberCommand): UpdateMemberDto.Response {
        val member = memberReader.getMember(RequestUtils.getAttribute("memberId")!!.toLong())
        command.nickname?.let { member.changeNickname(it) }
        command.gender?.let { member.changeGender(it) }
        command.ageRange?.let { member.changeAgeRange(it) }
        return UpdateMemberDto.Response.of(
                nickname = member.nickname,
                gender = member.gender,
                ageRange = member.ageRange
        )
    }

    override fun checkNickname(command: CheckNicknameCommand): CheckNicknameDto.Response {
        return CheckNicknameDto.Response.of(
            available = !memberReader.existMember(command.nickname)
        )
    }

    override fun bookmarkStation(command: BookmarkStationCommand): BookmarkStationDto.Response {
        val member = memberReader.getMember(RequestUtils.getAttribute("memberId")!!.toLong())
        if (command.stationNames.size > 3) {
            throw BusinessException(ResponseCode.EXCEED_MAXIMUM_STATION_COUNT)
        }

        memberStationWriter.deleteByMember(member)
        val bookmarkStationIds = command.stationNames
                .map { stationReader.getByName(it) }
                .map {station ->
                    val memberStation = MemberStationEntity(member = member, station = station)
                    memberStationWriter.save(memberStation).id
                }

        return BookmarkStationDto.Response(bookmarkStationIds)
    }
}

