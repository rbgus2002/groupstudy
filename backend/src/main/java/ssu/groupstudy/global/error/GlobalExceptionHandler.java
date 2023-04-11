package ssu.groupstudy.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssu.groupstudy.domain.user.exception.EmailExistsException;
import ssu.groupstudy.global.BusinessException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //MissingServletRequestParameterException.class,

//    @ExceptionHandler({ Exception.class})
//    protected ResponseEntity handleCustomException(CustomException ex) {
//        return new ResponseEntity(new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
//    }

    // TODO : return 형식 수정 및 메소드 내 처리 방식 레퍼런스 참고
    // TODO : Controller에서 return 타입과 연관해서 어떻게 처리해줄 지 생각
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException : {}", ErrorCode.INVALID_METHOD_ARGUMENT_ERROR.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_METHOD_ARGUMENT_ERROR);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("BusinessException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}