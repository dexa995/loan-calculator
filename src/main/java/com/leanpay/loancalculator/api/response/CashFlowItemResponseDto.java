package com.leanpay.loancalculator.api.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CashFlowItemResponseDto(Long id,
									  Integer month,
									  LocalDate paymentDate,
									  BigDecimal installmentAmount,
									  BigDecimal principalAmount,
									  BigDecimal interestAmount,
									  BigDecimal loanBalance
) {
}
