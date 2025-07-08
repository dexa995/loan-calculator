package com.leanpay.loancalculator.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.leanpay.loancalculator.api.response.error.ErrorResponse;
import com.leanpay.loancalculator.exception.BaseException;
import com.leanpay.loancalculator.exception.CashFlowNotFoundException;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden
public class CustomExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException baseException) {
		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.message(baseException.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
		IllegalArgumentException illegalArgumentException) {
		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(HttpStatus.BAD_REQUEST)
			.message(illegalArgumentException.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}


}
