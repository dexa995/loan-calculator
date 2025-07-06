package com.leanpay.loancalculator.unit.util;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.leanpay.loancalculator.util.CashFlowCalculatorUtil;

public class CashFlowCalculatorUtilTest {

	@Test
	void calculateInstallmentAmount_shouldReturnCorrectAmount() {
		BigDecimal loanAmount = BigDecimal.valueOf(10000);
		BigDecimal interestRate = BigDecimal.valueOf(6.0);
		Integer loanTerm = 12;

		BigDecimal result = CashFlowCalculatorUtil.calculateInstallmentAmount(loanAmount, interestRate, loanTerm);

		assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(860.66));
	}

	@Test
	void calculateInstallmentAmount_shouldHandleZeroInterestRate() {
		BigDecimal loanAmount = BigDecimal.valueOf(1200);
		BigDecimal interestRate = BigDecimal.ZERO;
		Integer loanTerm = 12;

		BigDecimal result = CashFlowCalculatorUtil.calculateInstallmentAmount(loanAmount, interestRate, loanTerm);

		assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(100.00));
	}

	@Test
	void calculateInstallmentAmount_shouldThrowException_whenInterestRateNegative() {
		BigDecimal loanAmount = BigDecimal.valueOf(1000);
		BigDecimal interestRate = BigDecimal.valueOf(-1);
		Integer loanTerm = 12;

		assertThatThrownBy(() ->
			CashFlowCalculatorUtil.calculateInstallmentAmount(loanAmount, interestRate, loanTerm)
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("interestRate must be greater than or equal zero");
	}

	@Test
	void calculateInstallmentAmount_shouldThrowException_whenLoanAmountIsZero() {
		BigDecimal loanAmount = BigDecimal.ZERO;
		BigDecimal interestRate = BigDecimal.valueOf(5);
		Integer loanTerm = 12;

		assertThatThrownBy(() ->
			CashFlowCalculatorUtil.calculateInstallmentAmount(loanAmount, interestRate, loanTerm)
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("loanAmount must be greater than zero");
	}

	@Test
	void calculateInstallmentAmount_shouldThrowException_whenLoanTermIsZero() {
		BigDecimal loanAmount = BigDecimal.valueOf(1000);
		BigDecimal interestRate = BigDecimal.valueOf(5);
		Integer loanTerm = 0;

		assertThatThrownBy(() ->
			CashFlowCalculatorUtil.calculateInstallmentAmount(loanAmount, interestRate, loanTerm)
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("loanTerm must be greater than zero");
	}

	@Test
	void calculateMonthlyInterest_shouldReturnCorrectMonthlyRate() {

		BigDecimal annualInterestRate = BigDecimal.valueOf(6.0);
		BigDecimal expectedMonthlyInterestRate = BigDecimal.valueOf(0.005);

		BigDecimal actualMonthlyInterestRate = CashFlowCalculatorUtil.calculateMonthlyInterest(annualInterestRate);

		assertThat(actualMonthlyInterestRate).isEqualByComparingTo(expectedMonthlyInterestRate);
	}

	@Test
	void calculateMonthlyInterest_shouldHandleZero() {
		BigDecimal annualInterestRate = BigDecimal.ZERO;
		BigDecimal expectedMonthlyInterestRate = BigDecimal.ZERO;

		BigDecimal actualMonthlyInterestRate = CashFlowCalculatorUtil.calculateMonthlyInterest(annualInterestRate);

		assertThat(actualMonthlyInterestRate).isEqualByComparingTo(expectedMonthlyInterestRate);
	}

	@Test
	void calculateMonthlyInterest_shouldThrowException_whenMonthlyInterestRateIsLessThanZero() {
		BigDecimal annualInterestRate = BigDecimal.valueOf(-6.0);

		assertThatThrownBy(() -> CashFlowCalculatorUtil.calculateMonthlyInterest(annualInterestRate))
		.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void calculateMonthlyInterest_shouldHandleLargeValue() {
		BigDecimal annualInterestRate = BigDecimal.valueOf(1200.0);
		BigDecimal expectedMonthlyInterestRate = BigDecimal.valueOf(1.0);

		BigDecimal actualMonthlyInterestRate = CashFlowCalculatorUtil.calculateMonthlyInterest(annualInterestRate);

		assertThat(actualMonthlyInterestRate).isEqualByComparingTo(expectedMonthlyInterestRate);
	}

}
