package com.leanpay.loancalculator.api.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateCashFlowDto(
	@NotNull(message = "Loan amount is required")
	@Positive(message = "Loan amount must be higher than 0")
	Double loanAmount,
	@NotNull(message = "Interest rate is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
	@DecimalMax(value = "100.00", inclusive = true, message = "Interest rate must be realistic (max 100%)")
	Double interestRate,
	@NotNull(message = "Loan term is required")
	@Min(value = 1, message = "Loan term must be at least 1 month")
	@Max(value = 360, message = "Loan term must be less than or equal to 360 months")
	Long loanTerm) {
}
