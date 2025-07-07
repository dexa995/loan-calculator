package com.leanpay.loancalculator.api.response.error;

import org.springframework.http.HttpStatus;

import lombok.Builder;

@Builder
public record ErrorResponse(HttpStatus status, String message) {
}
