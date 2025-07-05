package com.leanpay.loancalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.validation.constraints.NotNull;

public final class CashFlowCalculatorUtil {

	private CashFlowCalculatorUtil() {
	}

	public static BigDecimal calculateInstallmentAmount(@NotNull BigDecimal loanAmount,
		@NotNull BigDecimal interestRate, @NotNull Integer loanTerm) {

		validate(loanAmount, interestRate, loanTerm);

		BigDecimal monthlyInterestRate = calculateMonthlyInterest(interestRate);

		// (1 + monthlyInterestRate)^numberOfInstallments
		BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyInterestRate);
		BigDecimal power = onePlusRate.pow(loanTerm);

		// numerator: r * (1 + r)^n
		BigDecimal numerator = monthlyInterestRate.multiply(power);
		System.out.println(numerator);

		// denominator: (1 + r)^n - 1
		BigDecimal denominator = power.subtract(BigDecimal.ONE);
		System.out.println(denominator);

		// A = P * (numerator / denominator)
		BigDecimal monthlyInstallment = loanAmount.multiply(numerator.divide(denominator, RoundingMode.HALF_UP));
		System.out.println(monthlyInstallment);

		// Final rounded value to 2 decimal places (currency format)
		return monthlyInstallment.setScale(2, RoundingMode.HALF_UP);
	}

	public static BigDecimal calculateMonthlyInterest(BigDecimal annualRatePercent) {
		if (annualRatePercent.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("annualRatePercent must be greater than zero");
		}
		return annualRatePercent
			.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
			.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
	}

	private static void validate(BigDecimal loanAmount, BigDecimal interestRate, Integer loanTerm) {
		if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("loanAmount must be greater than zero");
		}
		if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("interestRate must be greater than or equal zero");
		}
		if (loanTerm <= 0) {
			throw new IllegalArgumentException("loanTerm must be greater than zero");
		}
	}

}
