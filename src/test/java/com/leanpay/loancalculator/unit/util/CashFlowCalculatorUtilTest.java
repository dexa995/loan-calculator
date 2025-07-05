package com.leanpay.loancalculator.unit.util;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.leanpay.loancalculator.util.CashFlowCalculatorUtil;

public class CashFlowCalculatorUtilTest {

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
