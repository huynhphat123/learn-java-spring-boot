package com.phatdev.expection;

import com.phatdev.dto.request.ApiReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Đánh dấu lớp này là nơi xử lý các exception toàn cục
public class GlobalExceptionHandler {

    // Xử lý các exception thuộc loại RuntimeException
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiReponse> runtimeExceptionHandler(RuntimeException exception) {
       ApiReponse apiReponse = new ApiReponse();

       apiReponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
       apiReponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

       return ResponseEntity.badRequest().body(apiReponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiReponse> runtimeAppHandler(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiReponse apiReponse = new ApiReponse();

        apiReponse.setCode(errorCode.getCode());
        apiReponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiReponse);
    }

    // Xử lý các exception do lỗi validation đầu vào (MethodArgumentNotValidException)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiReponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.KEY_INVALID;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        }
        catch (IllegalArgumentException e) {

        }
        ApiReponse apiReponse = new ApiReponse();

        apiReponse.setCode(errorCode.getCode());
        apiReponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiReponse);
    }
}
