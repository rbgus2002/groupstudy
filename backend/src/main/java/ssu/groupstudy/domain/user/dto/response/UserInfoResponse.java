package ssu.groupstudy.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssu.groupstudy.domain.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse {
    private Long userId;
    private String nickName;
    private String statusMessage;
    private String picture;


    private UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.statusMessage = user.getStatusMessage();
        this.picture = user.getPicture();
    }

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(user);
    }
}