package ssu.groupstudy.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ssu.groupstudy.domain.user.domain.User;
import ssu.groupstudy.domain.user.dto.request.SignUpRequest;
import ssu.groupstudy.domain.user.dto.response.UserResponse;
import ssu.groupstudy.domain.user.exception.EmailExistsException;
import ssu.groupstudy.domain.user.exception.UserNotFoundException;
import ssu.groupstudy.domain.user.repository.UserRepository;
import ssu.groupstudy.global.ResultCode;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private SignUpRequest getSignUpRequest() {
        return SignUpRequest.builder()
                .name("최규현")
                .email("rbgus200@naver.com")
                .nickName("규규")
                .phoneModel("")
                .picture("")
                .build();
    }

    private User getUser() {
        return getSignUpRequest().toEntity();
    }


    @Nested
    class 회원가입{
        @Test
        @DisplayName("중복되는 이메일이 존재하면 회원가입이 불가능하다")
        void 회원가입_실패_이메일중복() {
            // given
            doReturn(true).when(userRepository).existsByEmail(any(String.class));

            // when
            EmailExistsException exception = assertThrows(EmailExistsException.class, () -> userService.signUp(getSignUpRequest()));

            // then
            assertThat(exception.getResultCode()).isEqualTo(ResultCode.DUPLICATE_EMAIL);
        }

        @Test
        @DisplayName("회원가입 성공")
        void 회원가입_성공() {
            // given
            doReturn(false).when(userRepository).existsByEmail(any(String.class));
            doReturn(getUser()).when(userRepository).save(any(User.class));

            // when
            final User newUser = userService.signUp(getSignUpRequest());

            // then
            assertThat(newUser).isNotNull();
            assertThat(newUser.getName()).isEqualTo("최규현");
        }
    }

    @Nested
    class 사용자조회{
        @Test
        @DisplayName("userId가 존재하지 않으면 예외를 던진다")
        void 사용자_조회() {
            // given
            doReturn(Optional.empty()).when(userRepository).findByUserId(any(Long.class));

            // when
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));

            // then
            assertThat(exception.getResultCode()).isEqualTo(ResultCode.USER_NOT_FOUND);
        }

        @Test
        @DisplayName("성공")
        void 사용자조회_성공() {
            // given
            doReturn(Optional.of(getUser())).when(userRepository).findByUserId(any(Long.class));

            // when
            final UserResponse userResponse = userService.getUser(1L);

            // then
            assertThat(userResponse).isNotNull();
            assertThat(userResponse.getNickName()).isEqualTo("규규");
        }
    }
}