package com.leanpay.loancalculator.api.request;

import lombok.Builder;

@Builder
public record CreateCashFlowDto(Double loanAmount,
								Double interestRate,
								Long loanTerm) {
}
