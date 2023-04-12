package ssu.groupstudy.domain.user.exception;

import ssu.groupstudy.global.exception.BusinessException;
import ssu.groupstudy.global.error.ResultCode;

public class EmailExistsException extends BusinessException {
    public EmailExistsException(ResultCode resultCode){
        super(resultCode);
    }
}
