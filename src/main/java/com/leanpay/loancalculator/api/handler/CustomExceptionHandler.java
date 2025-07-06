package com.leanpay.loancalculator.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.leanpay.loancalculator.api.response.error.ErrorResponse;

@RestControllerAdvice
public class ExceptionHandler {
	public ResponseEntity<ErrorResponse> handleBaseException(Exception e) {}
}
