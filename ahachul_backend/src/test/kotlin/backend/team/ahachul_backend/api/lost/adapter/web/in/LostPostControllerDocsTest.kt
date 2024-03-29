package backend.team.ahachul_backend.api.lost.adapter.web.`in`

import backend.team.ahachul_backend.api.lost.adapter.web.`in`.dto.*
import backend.team.ahachul_backend.api.lost.application.port.`in`.LostPostUseCase
import backend.team.ahachul_backend.api.lost.domain.model.LostOrigin
import backend.team.ahachul_backend.api.lost.domain.model.LostStatus
import backend.team.ahachul_backend.api.lost.domain.model.LostType
import backend.team.ahachul_backend.common.dto.ImageDto
import backend.team.ahachul_backend.config.controller.CommonDocsTestConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(LostPostController::class)
class LostPostControllerDocsTest: CommonDocsTestConfig() {

    @MockBean lateinit var lostPostUseCase: LostPostUseCase

    @Test
    fun getLostPost() {
        // given
        val recommendPost = GetLostPostDto.RecommendResponse(
            id = 2,
            title = "title",
            writer = "writer",
            date = "2023/01/23",
            imageUrl = "https://img.png"
        )

        val response = GetLostPostDto.Response(
            id = 1,
            title = "title",
            content = "content",
            writer = "writer",
            createdBy = "1",
            date = "2023/01/23",
            subwayLine = 1,
            chats = 1,
            status = LostStatus.PROGRESS,
            storage = "우리집",
            storageNumber = "02-2222-3333",
            pageUrl = "http://lost112",
            images = listOf(ImageDto(1, "https://img.png")),
            categoryName = "휴대폰",
            externalSourceImageUrl = "http://lost112/image.png",
            recommendPosts = listOf(recommendPost)
        )

        given(lostPostUseCase.getLostPost(anyLong()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/lost-posts/{lostId}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("get-lost-post",
                getDocsRequest(),
                getDocsResponse(),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                responseFields(
                    *commonResponseFields(),
                    fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("유실물 아이디"),
                    fieldWithPath("result.title").type(JsonFieldType.STRING).description("유실물 제목"),
                    fieldWithPath("result.content").type(JsonFieldType.STRING).description("유실물 내용"),
                    fieldWithPath("result.writer").type(JsonFieldType.STRING).description("유실물 작성자 닉네임"),
                    fieldWithPath("result.createdBy").type(JsonFieldType.STRING).description("작성자 ID"),
                    fieldWithPath("result.date").type(JsonFieldType.STRING).description("유실물 작성 날짜"),
                    fieldWithPath("result.subwayLine").type(JsonFieldType.NUMBER).description("유실 호선"),
                    fieldWithPath("result.chats").type(JsonFieldType.NUMBER).description("유실물 쪽지 개수"),
                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("유실물 찾기 완료 여부").attributes(getFormatAttribute( "PROGRESS / COMPLETE")),
                    fieldWithPath("result.storage" ).type(JsonFieldType.STRING).description("보관 장소").attributes(getFormatAttribute("Lost112 데이터")),
                    fieldWithPath("result.storageNumber").type(JsonFieldType.STRING).description("보관 장소 전화번호").attributes(getFormatAttribute("Lost112 데이터")),
                    fieldWithPath("result.pageUrl" ).type(JsonFieldType.STRING).description("외부 유실물 데이터 페이지 링크"),
                    fieldWithPath("result.categoryName" ).type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                    fieldWithPath("result.externalSourceImageUrl").type(JsonFieldType.STRING).description("Lost112 이미지 링크").optional(),
                    fieldWithPath("result.images[]").type(JsonFieldType.ARRAY).description("등록된 이미지 목록"),
                    fieldWithPath("result.images[].imageId").type(JsonFieldType.NUMBER).description("등록된 이미지 ID"),
                    fieldWithPath("result.images[].imageUrl").type(JsonFieldType.STRING).description("등록된 이미지 URI"),
                    fieldWithPath("result.recommendPosts[].id").type(JsonFieldType.NUMBER).description("추천 유실물 아이디"),
                    fieldWithPath("result.recommendPosts[].title").type(JsonFieldType.STRING).description("추천 유실물 제목"),
                    fieldWithPath("result.recommendPosts[].writer").type(JsonFieldType.STRING).description("추천 유실물 작성자"),
                    fieldWithPath("result.recommendPosts[].date").type(JsonFieldType.STRING).description("추천 유실물 생성 일자"),
                    fieldWithPath("result.recommendPosts[].imageUrl").type(JsonFieldType.STRING).description("추천 유실물 썸네일 경로"),
                )
            ))
    }

    @Test
    fun searchLostPosts() {
        // given
        val response = SearchLostPostsDto.Response(
            hasNext = true,
            listOf(
                SearchLostPostsDto.SearchLost(
                    id = 1,
                    title = "title",
                    content = "content",
                    writer = "writer",
                    createdBy = "1",
                    date = "2023/01/23",
                    subwayLine = 1,
                    chats = 1,
                    status = LostStatus.PROGRESS,
                    imageUrl = "https://img.png",
                    categoryName = "휴대폰"
                )
            )
        )

        given(lostPostUseCase.searchLostPosts(any()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/lost-posts")
                .queryParam("lostType", LostType.LOST.name)
                .queryParam("subwayLineId", "1")
                .queryParam("origin", LostOrigin.LOST112.name)
                .queryParam("keyword", "검색 키워드")
                .queryParam("lostPostId", "100")
                .queryParam("pageSize", "10" )
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("search-lost-posts",
                getDocsRequest(),
                getDocsResponse(),
                queryParameters(
                    parameterWithName("lostType").description("유실물 카테고리").attributes(getFormatAttribute("LOST(유실물) / ACQUIRE(습득물 + Lost112)")),
                    parameterWithName("subwayLineId").description("유실물 호선").optional(),
                    parameterWithName("origin").description("유실물 출처").attributes(getFormatAttribute( "LOST112 / APP")).optional(),
                    parameterWithName("keyword").description("검색 키워드 명칭").optional(),
                    parameterWithName("lostPostId").description("마지막으로 조회한 유실물 데이터의 ID"),
                    parameterWithName("pageSize").description("한번에 가져올 데이터 개수")
                ),
                responseFields(
                    *commonResponseFields(),
                    fieldWithPath("result.hasNext").type(JsonFieldType.BOOLEAN).description("다음 유실물 포스트 존재 여부"),
                    fieldWithPath("result.posts[].id").type(JsonFieldType.NUMBER).description("유실물 아이디"),
                    fieldWithPath("result.posts[].title").type(JsonFieldType.STRING).description("유실물 제목"),
                    fieldWithPath("result.posts[].content").type(JsonFieldType.STRING).description("유실물 내용"),
                    fieldWithPath("result.posts[].writer").type(JsonFieldType.STRING).description("유실물 작성자 닉네임"),
                    fieldWithPath("result.posts[].createdBy").type(JsonFieldType.STRING).description("작성자 ID"),
                    fieldWithPath("result.posts[].date").type(JsonFieldType.STRING).description("유실물 작성 날짜"),
                    fieldWithPath("result.posts[].subwayLine").type(JsonFieldType.NUMBER).description("유실 호선 ID"),
                    fieldWithPath("result.posts[].chats").type(JsonFieldType.NUMBER).description("유실물 쪽지 개수"),
                    fieldWithPath("result.posts[].status").type(JsonFieldType.STRING).description("유실물 찾기 완료 여부").attributes(getFormatAttribute( "PROGRESS / COMPLETE")),
                    fieldWithPath("result.posts[].categoryName" ).type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                    fieldWithPath("result.posts[].imageUrl").type(JsonFieldType.STRING).description("등록된 첫 번째 이미지 URI"),
                )
            ))
    }

    @Test
    fun createLostPost() {
        // given
        val response = CreateLostPostDto.Response(
            id = 1,
            images = listOf(ImageDto.of(1L, "url1")))

        given(lostPostUseCase.createLostPost(any()))
            .willReturn(response)

        val request = CreateLostPostDto.Request(
            title = "title",
            content = "content",
            subwayLine = 1,
            lostType = LostType.LOST,
            categoryName = "휴대폰"
        )

        val mapper = ObjectMapper()
        val requestFile = MockMultipartFile(
            "content",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            mapper.writeValueAsString(request).toByteArray())

        val imageFile = MockMultipartFile(
            "files",
            "image.png",
            MediaType.IMAGE_PNG_VALUE,
            "<< png data >>".toByteArray())

        // when
        val result = mockMvc.perform(
            multipart("/v1/lost-posts")
                .file(requestFile)
                .file(imageFile)
                .header("Authorization", "Bearer <Access Token>")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
                .andDo(document("create-lost-post",
                    getDocsRequest(),
                    getDocsResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("엑세스 토큰")
                    ),
                    requestParts(
                        partWithName("files").description("업로드할 이미지"),
                        partWithName("content").description("request dto")
                    ),
                    requestPartFields(
                        "content",
                        fieldWithPath("title").type(JsonFieldType.STRING).description("유실물 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("유실물 내용"),
                        fieldWithPath("subwayLine").type(JsonFieldType.NUMBER).description("유실 호선 ID"),
                        fieldWithPath("lostType").type(JsonFieldType.STRING).description("유실물 타입").attributes(getFormatAttribute("LOST(유실) / ACQUIRE(습득)")),
                        fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                    ),
                    responseFields(
                        *commonResponseFields(),
                        fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("저장한 유실물 아이디"),
                        fieldWithPath("result.images[].imageId").type(JsonFieldType.NUMBER).description("유실물 이미지 번호").optional(),
                        fieldWithPath("result.images[].imageUrl").type(JsonFieldType.STRING).description("유실물 이미지 경로").optional(),
                    )
                ))
    }

    @Test
    fun updateLostPost() {
        // given
        val response = UpdateLostPostDto.Response(
            id = 1,
            title = "title",
            content = "content",
            subwayLine = 1,
            status = LostStatus.COMPLETE,
            categoryName = "지갑"
        )

        given(lostPostUseCase.updateLostPost(any()))
            .willReturn(response)

        val request = UpdateLostPostDto.Request(
            id = 1,
            title = "title",
            content = "content",
            subwayLine = 1,
            status = LostStatus.COMPLETE,
            removeFileIds = arrayListOf(1, 2, 3),
            categoryName = "휴대폰"
        )

        val mapper = ObjectMapper()
        val requestFile = MockMultipartFile(
            "content",
            "dto",
            MediaType.APPLICATION_JSON_VALUE,
            mapper.writeValueAsString(request).toByteArray())

        val imageFile = MockMultipartFile(
            "files",
            "file.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "File Content".toByteArray())

        // when
        val result = mockMvc.perform(
            multipart("/v1/lost-posts/{lostId}", 1)
                .file(requestFile)
                .file(imageFile)
                .header("Authorization", "Bearer <Access Token>")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("update-lost-post",
                getDocsRequest(),
                getDocsResponse(),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                requestParts(
                    partWithName("files").description("업로드할 이미지"),
                    partWithName("content").description("요청 DTO")
                ),
                requestPartFields(
                    "content",
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("유실물 아이디").optional().attributes(getFormatAttribute("사용 X 필드")),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("유실물 제목").optional(),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("유실물 내용").optional(),
                    fieldWithPath("imgUrls").type(JsonFieldType.ARRAY).description("유실물 이미지 리스트").optional(),
                    fieldWithPath("subwayLine").type(JsonFieldType.NUMBER).description("유실 호선 ID").optional(),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("유실물 찾기 완료 상태")
                        .attributes(getFormatAttribute( "PROGRESS / COMPLETE")).optional(),
                    fieldWithPath("removeFileIds").type(JsonFieldType.ARRAY).description("삭제할 유실물 이미지 번호 리스트"),
                    fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름").optional()
                ),
                responseFields(
                    *commonResponseFields(),
                    fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("수정한 유실물 아이디"),
                    fieldWithPath("result.title").type(JsonFieldType.STRING).description("유실물 제목"),
                    fieldWithPath("result.content").type(JsonFieldType.STRING).description("유실물 내용"),
                    fieldWithPath("result.subwayLine").type(JsonFieldType.NUMBER).description("유실 호선 ID"),
                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("유실물 찾기 완료 상태")
                        .attributes(getFormatAttribute( "PROGRESS / COMPLETE")),
                    fieldWithPath("result.categoryName").type(JsonFieldType.STRING).description("카테고리 이름")
                )
            ))
    }

    @Test
    fun deleteLostPost() {
        // given
        val response = DeleteLostPostDto.Response(
            id = 1
        )

        given(lostPostUseCase.deleteLostPost(anyLong()))
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            delete("/v1/lost-posts/{lostId}", 1)
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("delete-lost-post",
                getDocsRequest(),
                getDocsResponse(),
                pathParameters(
                    parameterWithName("lostId").description("유실물 아이디")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("엑세스 토큰")
                ),
                responseFields(
                    *commonResponseFields(),
                    fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("삭제한 유실물 아이디")
                )
            ))
    }
}
