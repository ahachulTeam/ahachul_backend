package backend.team.ahachul_backend.api.rank.application.service

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.SearchCommunityPostCommand
import backend.team.ahachul_backend.api.community.application.port.out.CommunityPostReader
import backend.team.ahachul_backend.api.community.application.service.CommunityPostService
import backend.team.ahachul_backend.api.community.domain.SearchCommunityPost
import backend.team.ahachul_backend.common.client.RedisClient
import backend.team.ahachul_backend.config.controller.CommonServiceTestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HashTagRankServiceTest(
    @Autowired val hashTagRankService: HashTagRankService,
    @Autowired val communityPostService: CommunityPostService,
    @Autowired val redisClient: RedisClient
): CommonServiceTestConfig() {

    @MockBean(name = "communityPostReader")
    lateinit var communityPostReader: CommunityPostReader

    @BeforeEach
    fun init() {
        val key = HashTagRankService.KEY
        redisClient.delete(key)
    }

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
        assertThat(result.ranks.size).isEqualTo(2)
        assertThat(result.ranks).isEqualTo(listOf("2호선", "1호선"))
    }

    @Test
    fun 해시태그_검색시_비동기로_조회수가_증가한다() {
        // given
        val hashTagName = "1호선"
        val response: Slice<SearchCommunityPost> = SliceImpl(listOf())
        val searchCommand = SearchCommunityPostCommand(
            hashTag = hashTagName,
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
        val result = hashTagRankService.get(hashTagName)
        assertThat(result).isEqualTo(2.0)
    }

    @Test
    fun 조회수_증가_동시성_테스트() {
        val hashTagName = "1호선"
        val executorService: ExecutorService = Executors.newFixedThreadPool(100)
        val countDownLatch = CountDownLatch(100)

        for (i in 1..100) {
            executorService.execute {
                hashTagRankService.increaseCount(hashTagName)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()
        val result = hashTagRankService.get(hashTagName)
        assertThat(result).isEqualTo(100.0)
    }
}
