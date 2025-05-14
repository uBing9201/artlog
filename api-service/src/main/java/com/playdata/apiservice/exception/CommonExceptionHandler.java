package com.playdata.apiservice.exception;

import com.playdata.apiservice.dto.common.CommonErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class CommonExceptionHandler {

    // Controller 단에서 발생하는 모든 예외를 일괄 처리하는 클래스
    // 실제 예외는 Service 계층에서 발생하지만, 따로 예외 처리가 없는 경우
    // 메서드를 호출한 상위 계층으로 전파됩니다.
    @ExceptionHandler(PublicApiException.class)
    public ResponseEntity<?> PublicApiHandler(PublicApiException e) {
        e.printStackTrace();
        CommonErrorDto errorDto
                = new CommonErrorDto(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // 옳지 않은 입력값 전달 시 호출되는 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalHandler(IllegalArgumentException e) {
        e.printStackTrace();
        CommonErrorDto errorDto
                = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


//    // 특정 권한을 가지지 못한 사용자가 요청을 보냈을 때 내쫓는 메서드
//    @ExceptionHandler(AuthorizationDeniedException.class)
//    public ResponseEntity<?> authDeniedHandler(AuthorizationDeniedException e) {
//        e.printStackTrace();
//        CommonErrorDto errorDto
//                = new CommonErrorDto(HttpStatus.FORBIDDEN, e.getMessage());
//        return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
//    }

    // 미처 준비하지 못한 타입의 예외가 발생했을 시 처리할 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        e.printStackTrace();
        CommonErrorDto errorDto
                = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "server error");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR); // 500 에러
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioExceptionHandler(IOException e) {
        e.printStackTrace();
        CommonErrorDto errorDto
                = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST); // 500 에러
    }

}








