package com.leanpay.loancalculator.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record CashFlowResponseDto(Long id,
								  BigDecimal loanAmount,
								  Long loanTerm,
								  LocalDateTime created,
								  BigDecimal interestRate,
								  BigDecimal interestAmount,
								  BigDecimal totalClientDue,
								  BigDecimal installmentAmount,
								  List<CashFlowItemResponseDto> cashFlowItems) {
}
