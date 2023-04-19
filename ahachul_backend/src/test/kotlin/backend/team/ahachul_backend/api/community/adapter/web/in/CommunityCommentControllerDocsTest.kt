package backend.team.ahachul_backend.api.community.adapter.web.`in`

import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.CreateCommunityCommentDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.DeleteCommunityCommentDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.GetCommunityCommentsDto
import backend.team.ahachul_backend.api.community.adapter.web.`in`.dto.UpdateCommunityCommentDto
import backend.team.ahachul_backend.api.community.application.port.`in`.CommunityCommentUseCase
import backend.team.ahachul_backend.common.interceptor.AuthenticationInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CommunityCommentController::class)
@AutoConfigureRestDocs
class CommunityCommentControllerDocsTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
) {

    @MockBean lateinit var communityCommentUseCase: CommunityCommentUseCase
    @MockBean lateinit var authenticationInterceptor: AuthenticationInterceptor
    @MockBean lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @BeforeEach
    fun setup() {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true)
    }

    @Test
    fun getCommunityCommentsTest() {
        // given
        val response = GetCommunityCommentsDto.Response(
            listOf(
                GetCommunityCommentsDto.CommunityComment(
                    id = 2,
                    upperCommentId = 1,
                    content = "내용"
                )
            )
        )

        given(communityCommentUseCase.getCommunityComments())
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            get("/v1/community-comments")
                .param("postId", "1")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("get-community-comments",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("postId").description("코멘트 조회할 게시글 아이디")
                ),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    PayloadDocumentation.fieldWithPath("result.comments[0].id").type(JsonFieldType.NUMBER).description("코멘트 아이디"),
                    PayloadDocumentation.fieldWithPath("result.comments[0].upperCommentId").type(JsonFieldType.NUMBER).description("상위 코멘트 아이디").optional(),
                    PayloadDocumentation.fieldWithPath("result.comments[0].content").type(JsonFieldType.STRING).description("코멘트 내용"),
                )
            ))
    }

    @Test
    fun createCommunityCommentTest() {
        // given
        val response = CreateCommunityCommentDto.Response(
            id = 2,
            upperCommentId = 1,
            content = "생성된 코멘트 내용"
        )

        given(communityCommentUseCase.createCommunityComment())
            .willReturn(response)

        val request = CreateCommunityCommentDto.Request(
            postId = 1,
            upperCommentId = 1,
            content = "생성할 코멘트 내용"
        )

        // when
        val result = mockMvc.perform(
            post("/v1/community-comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("create-community-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("postId").description("코멘트 생성할 게시글 아이디"),
                    PayloadDocumentation.fieldWithPath("upperCommentId").description("상위 코멘트 아이디").optional(),
                    PayloadDocumentation.fieldWithPath("content").description("생성할 내용")
                ),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    PayloadDocumentation.fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("생성된 코멘트 아이디"),
                    PayloadDocumentation.fieldWithPath("result.upperCommentId").type(JsonFieldType.NUMBER).description("연결된 상위 코멘트 아이디").optional(),
                    PayloadDocumentation.fieldWithPath("result.content").type(JsonFieldType.STRING).description("생성된 내용"),
                )
            ))
    }

    @Test
    fun updateCommunityCommentTest() {
        // given
        val response = UpdateCommunityCommentDto.Response(
            id = 1,
            content = "변경된 코멘트 내용"
        )

        given(communityCommentUseCase.updateCommunityComment())
            .willReturn(response)

        val request = UpdateCommunityCommentDto.Request(
            content = "변경할 코멘트 내용"
        )

        // when
        val result = mockMvc.perform(
            patch("/v1/community-comments/{commentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("update-community-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                RequestDocumentation.pathParameters(
                    parameterWithName("commentId").description("변경할 코멘트 아이디")
                ),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("content").description("변경할 내용"),
                ),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    PayloadDocumentation.fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("변경된 코멘트 아이디"),
                    PayloadDocumentation.fieldWithPath("result.content").type(JsonFieldType.STRING).description("변경된 내용"),
                )
            ))
    }

    @Test
    fun deleteCommunityCommentTest() {
        // given
        val response = DeleteCommunityCommentDto.Response(
            id = 1
        )

        given(communityCommentUseCase.deleteCommunityComment())
            .willReturn(response)

        // when
        val result = mockMvc.perform(
            delete("/v1/community-comments/{commentId}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("delete-community-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                RequestDocumentation.pathParameters(
                    parameterWithName("commentId").description("삭제할 코멘트 아이디")
                ),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
                    PayloadDocumentation.fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("삭제된 코멘트 아이디"),
                )
            ))
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}
