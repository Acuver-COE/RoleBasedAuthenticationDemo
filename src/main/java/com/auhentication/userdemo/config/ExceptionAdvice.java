package com.auhentication.userdemo.config;

import com.auhentication.userdemo.exception.ConflictException;
import com.auhentication.userdemo.exception.ForbiddenException;
import com.auhentication.userdemo.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ApiResponse> handleGenericBadRequestException(ConflictException e) {
        ApiResponse error = new ApiResponse (HttpStatus.BAD_REQUEST.value(),e.getMessage ());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
        ApiResponse error = new ApiResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage ());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ApiResponse> handleForbiddenException(ForbiddenException e) {
        ApiResponse error = new ApiResponse(HttpStatus.FORBIDDEN.value(),e.getMessage ());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = WebExchangeBindException.class)
    public ResponseEntity<ApiResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        List<String> errorList = new ArrayList<> ();
        e.getFieldErrors().forEach(fieldError -> errorList.add(fieldError.getDefaultMessage()));
        ApiResponse error = new ApiResponse(HttpStatus.BAD_REQUEST.value(),errorList.toString ());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
