package backend.team.ahachul_backend.api.rank.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.SearchCommunityPostCommand
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.service.CommunityPostService
import backend.team.ahachul_backend.api.community.domain.SearchCommunityPost
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

class HashTagRankServiceTest(
    @Autowired val hashTagRankService: HashTagRankService,
    @Autowired val communityPostService: CommunityPostService
): CommonServiceTestConfig() {

     @MockBean(name = "communityPostReader")
     lateinit var communityPostReader: CommunityPostReader

    @Test
    fun 해시태그_내림차순_랭킹을_반환한다() {
        // given
        for (i in 1..3) {
            hashTagRankService.increaseCount("1호선")
        }

        for (i in 1..5) {
            hashTagRankService.increaseCount("2호선")
        }

        // when
        val result = hashTagRankService.getRank()

        // then
        Assertions.assertThat(result.ranks.size).isEqualTo(2)
        Assertions.assertThat(result.ranks).isEqualTo(listOf("2호선", "1호선"))
    }

    @Test
    fun 해시태그_검색시_비동기로_조회수가_증가한다() {
        // given
        val response: Slice<SearchCommunityPost> = SliceImpl(listOf())
        val searchCommand = SearchCommunityPostCommand(
            hashTag = "1호선",
            pageable = PageRequest.of(0, 3)
        )

        given(communityPostReader.searchCommunityPosts(searchCommand))
            .willReturn(response)

        // when
        for (i in 1 .. 2) {
            communityPostService.searchCommunityPosts(searchCommand)
        }

        Thread.sleep(1000)

        // then
        val result = hashTagRankService.get("1호선")
        Assertions.assertThat(result).isEqualTo(2.0)
    }
}
