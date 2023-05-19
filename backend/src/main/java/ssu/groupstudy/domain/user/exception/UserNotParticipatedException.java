package ssu.groupstudy.domain.user.exception;

import ssu.groupstudy.global.ResultCode;
import ssu.groupstudy.global.exception.BusinessException;

public class UserNotParticipatedException extends BusinessException {
    public UserNotParticipatedException(ResultCode resultCode){
        super(resultCode);
    }
}
