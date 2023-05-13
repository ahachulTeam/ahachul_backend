package backend.team.ahachul_backend.api.community.adapter.web.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.post.*
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityPostUseCase
import backend.team.ahachul_backend.api.community.domain.model.CommunityCategoryType
import backend.team.ahachul_backend.common.model.RegionType
import backend.team.ahachul_backend.config.controller.CommonDocsConfig
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(CommunityPostController::class)
class CommunityPostControllerDocsTest : CommonDocsConfig() {

    @MockBean
    lateinit var communityPostUseCase: CommunityPostUseCase

    @Test
    fun searchCommunityPostsTest() {
        // given
        val response = SearchCommunityPostDto.Response(
            true,
            listOf(
                SearchCommunityPostDto.CommunityPost(
                    1,
                    "제목",
                    CommunityCategoryType.ISSUE,
                    0,
                    0,
                    RegionType.METROPOLITAN,
                    LocalDateTime.now(),
                    "작성자 ID",
                    "작성자 닉네임"
                )
            )
        )

        given(communityPostUseCase.searchCommunityPosts(any()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/community-posts")
                .param("categoryType", "ISSUE")
                .param("subwayLineId", "1")
                .param("content", "내용")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "createdAt,desc")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "search-community-posts",
                    getDocsRequest(),
                    getDocsResponse(),
                    queryParameters(
                        parameterWithName("categoryType").description("카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")).optional(),
                        parameterWithName("subwayLineId").description("노선 ID").optional(),
                        parameterWithName("content").description("탐색하고자 하는 내용").optional(),
                        parameterWithName("page").description("현재 페이지"),
                        parameterWithName("size").description("페이지 노출 데이터 수"),
                        parameterWithName("sort").description("정렬 조건").attributes(getFormatAttribute("(likes|createdAt|views),(asc|desc)")),
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                        fieldWithPath("result.posts[].id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                        fieldWithPath("result.posts[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                        fieldWithPath("result.posts[].categoryType").type("CategoryType").description("카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")),
                        fieldWithPath("result.posts[].views").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("result.posts[].likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("result.posts[].region").type("RegionType").description("지역").attributes(getFormatAttribute("METROPOLITAN")),
                        fieldWithPath("result.posts[].createdAt").type("LocalDateTime").description("작성일자"),
                        fieldWithPath("result.posts[].createdBy").type(JsonFieldType.STRING).description("작성자 ID"),
                        fieldWithPath("result.posts[].writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    )
                )
            )
    }

    @Test
    fun getCommunityPostTest() {
        // given
        val response = GetCommunityPostDto.Response(
            1,
            "제목",
            "내용",
            CommunityCategoryType.ISSUE,
            0,
            0,
            RegionType.METROPOLITAN,
            LocalDateTime.now(),
            "작성자 ID",
            "작성자 닉네임"
        )

        given(communityPostUseCase.getCommunityPost(any()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/community-posts/{postId}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "get-community-post",
                    getDocsRequest(),
                    getDocsResponse(),
                    pathParameters(
                        parameterWithName("postId").description("게시물 아이디")
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                        fieldWithPath("result.title").type(JsonFieldType.STRING).description("게시글 제목"),
                        fieldWithPath("result.content").type(JsonFieldType.STRING).description("게시글 내용"),
                        fieldWithPath("result.categoryType").type("CategoryType").description("카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")),
                        fieldWithPath("result.views").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("result.likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("result.region").type("RegionType").description("지역").attributes(getFormatAttribute("METROPOLITAN")),
                        fieldWithPath("result.createdAt").type("LocalDateTime").description("작성일자"),
                        fieldWithPath("result.createdBy").type(JsonFieldType.STRING).description("작성자 ID"),
                        fieldWithPath("result.writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    )
                )
            )
    }

    @Test
    fun createCommunityPostTest() {
        // given
        val response = CreateCommunityPostDto.Response(
            id = 1,
            title = "생성된 제목",
            content = "생성된 내용",
            categoryType = CommunityCategoryType.ISSUE,
            region = RegionType.METROPOLITAN,
            subwayLineId = 1
        )

        given(communityPostUseCase.createCommunityPost(any()))
            .willReturn(response)

        val request = CreateCommunityPostDto.Request(
            title = "생성할 제목",
            content = "생성할 내용",
            categoryType = CommunityCategoryType.ISSUE,
            subwayLineId = 1
        )

        // when
        val result = mockMvc.perform(
            post("/v1/community-posts")
                .header("Authorization", "Bearer <Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "create-community-post",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("title").description("생성할 제목"),
                        fieldWithPath("content").description("생성할 내용"),
                        fieldWithPath("categoryType").description("카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")),
                        fieldWithPath("subwayLineId").description("지하철 노선 ID"),
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("생성된 게시글 아이디"),
                        fieldWithPath("result.title").type(JsonFieldType.STRING).description("생성된 게시글 제목"),
                        fieldWithPath("result.content").type(JsonFieldType.STRING).description("생성된 게시글 내용"),
                        fieldWithPath("result.categoryType").type("CategoryType").description("카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")),
                        fieldWithPath("result.region").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("result.subwayLineId").type(JsonFieldType.NUMBER).description("지하철 노선 ID"),
                    )
                )
            )
    }

    @Test
    fun updateCommunityPostTest() {
        // given
        val response = UpdateCommunityPostDto.Response(
            id = 1,
            title = "변경된 제목",
            content = "변경된 내용",
            categoryType = CommunityCategoryType.ISSUE
        )

        given(communityPostUseCase.updateCommunityPost(any()))
            .willReturn(response)

        val request = UpdateCommunityPostDto.Request(
            title = "변경할 제목",
            content = "변경할 내용",
            categoryType = CommunityCategoryType.ISSUE
        )

        // when
        val result = mockMvc.perform(
            patch("/v1/community-posts/{postId}", 1)
                .header("Authorization", "Bearer <Access Token>")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "update-community-post",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("postId").description("게시물 아이디")
                    ),
                    requestFields(
                        fieldWithPath("title").description("변경할 제목"),
                        fieldWithPath("content").description("변경할 내용"),
                        fieldWithPath("categoryType").description("변경할 카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")),
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                        fieldWithPath("result.title").type(JsonFieldType.STRING).description("변경된 게시글 제목"),
                        fieldWithPath("result.content").type(JsonFieldType.STRING).description("변경된 게시글 내용"),
                        fieldWithPath("result.categoryType").type("CategoryType").description("변경된 카테고리 타입").attributes(getFormatAttribute("FREE, INSIGHT, ISSUE, HUMOR")),
                    )
                )
            )
    }

    @Test
    fun deleteCommunityPostTest() {
        // given
        val response = DeleteCommunityPostDto.Response(
            id = 1
        )

        given(communityPostUseCase.deleteCommunityPost(any()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            delete("/v1/community-posts/{postId}", 1)
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "delete-community-post",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("postId").description("삭제할 게시물 아이디")
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("삭제된 게시글 아이디"),
                    )
                )
            )
    }
}