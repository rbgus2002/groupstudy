package ssu.groupstudy.domain.study.api;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ssu.groupstudy.domain.study.dto.reuqest.InviteUserRequest;
import ssu.groupstudy.domain.study.dto.reuqest.CreateStudyRequest;
import ssu.groupstudy.domain.study.exception.StudyNotFoundException;
import ssu.groupstudy.domain.study.service.StudyCreateService;
import ssu.groupstudy.domain.study.service.StudyInviteService;
import ssu.groupstudy.domain.user.dto.request.SignUpRequest;
import ssu.groupstudy.domain.user.exception.UserNotFoundException;
import ssu.groupstudy.global.ResultCode;
import ssu.groupstudy.global.dto.DataResponseDto;
import ssu.groupstudy.global.handler.GlobalExceptionHandler;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudyApiTest {
    @InjectMocks
    private StudyApi studyApi;

    @Mock
    private StudyInviteService studyInviteService;

    @Mock
    private StudyCreateService studyCreateService;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(studyApi)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        gson = new Gson();
    }

    private CreateStudyRequest getRegisterStudyRequest() {
        return CreateStudyRequest.builder()
                .studyName("AlgorithmSSU")
                .hostUserId(-1L)
                .picture("")
                .detail("알고문풀")
                .build();
    }

    private SignUpRequest getSignUpRequest() {
        return SignUpRequest.builder()
                .name("최규현")
                .email("rbgus2002@naver.com")
                .nickName("규규")
                .phoneModel("")
                .picture("")
                .build();
    }

    @Nested
    class 스터디생성{
        @Test
        @DisplayName("스터디 생성 시 반드시 스터디의 이름을 반드시 정해주어야 한다")
        void 실패_이름존재X() throws Exception {
            // given
            final String url = "/study";

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(CreateStudyRequest.builder()
                            .studyName("")
                            .hostUserId(-1L)
                            .picture("")
                            .detail("알고문풀")
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 스터디를 생성할 경우 예외를 던진다")
        void 실패_사용자존재X() throws Exception {
            // given
            final String url = "/study";
            doThrow(new UserNotFoundException(ResultCode.USER_NOT_FOUND)).when(studyCreateService).createStudy(any(CreateStudyRequest.class));

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(getRegisterStudyRequest()))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("성공")
        void 성공() throws Exception {
            // given
            final String url = "/study";
            doReturn(getRegisterStudyRequest().toEntity(getSignUpRequest().toEntity())).when(studyCreateService).createStudy(any(CreateStudyRequest.class));

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(getRegisterStudyRequest()))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isOk());

            DataResponseDto response = gson.fromJson(resultActions.andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8), DataResponseDto.class);

            assertThat(response.getData().get("study")).isNotNull();
        }
    }

    @Nested
    class 스터디초대{
        @Test
        @DisplayName("존재하지 않는 사용자를 스터디에 초대하면 예외를 던진다")
        void 실패_회원존재X() throws Exception {
            // given
            final String url = "/study/invite";
            doThrow(new UserNotFoundException(ResultCode.USER_NOT_FOUND)).when(studyInviteService).inviteUserToStudy(any(InviteUserRequest.class));

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(InviteUserRequest.builder()
                            .studyId(-1L)
                            .userId(-1L)
                            .build()
                    ))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("존재하지 않는 스터디에 사용자를 초대하는 경우 예외를 던진다")
        void 스터디초대_실패_스터디존재X() throws Exception {
            // given
            final String url = "/study/invite";
            doThrow(new StudyNotFoundException(ResultCode.STUDY_NOT_FOUND)).when(studyInviteService).inviteUserToStudy(any(InviteUserRequest.class));

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(InviteUserRequest.builder()
                            .studyId(-1L)
                            .userId(-1L)
                            .build()
                    ))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isNotFound());
        }


        @Test
        @DisplayName("해당 스터디에 회원이 이미 존재하는 경우 다시 초대를 하면 예외를 던진다")
        void 스터디초대_실패_회원이미존재() throws Exception {
            // given
            final String url = "/study/invite";

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(InviteUserRequest.builder()
                            .studyId(-1L)
                            .build()
                    ))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("성공")
        void 스터디초대_성공() throws Exception {
            // given
            final String url = "/study/invite";
            doReturn(getSignUpRequest().toEntity()).when(studyInviteService).inviteUserToStudy(any(InviteUserRequest.class));

            // when
            final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(gson.toJson(InviteUserRequest.builder()
                            .studyId(-1L)
                            .userId(-1L)
                            .build()
                    ))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isOk());

            DataResponseDto response = gson.fromJson(resultActions.andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8), DataResponseDto.class);

            assertThat(response.getData().get("invitedUser")).isNotNull();
        }
    }
}