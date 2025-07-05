package com.leanpay.loancalculator.util;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterestRateUtilsTest {

	@Test
	void calculateMonthlyInterest_shouldReturnCorrectMonthlyRate() {

		BigDecimal annualInterestRate = BigDecimal.valueOf(6.0);
		BigDecimal expectedMonthlyInterestRate = BigDecimal.valueOf(0.005);

		BigDecimal actualMonthlyInterestRate = InterestRateUtils.calculateMonthlyInterest(annualInterestRate);

		assertThat(actualMonthlyInterestRate).isEqualByComparingTo(expectedMonthlyInterestRate);
	}

	@Test
	void calculateMonthlyInterest_shouldHandleZero() {
		BigDecimal annualInterestRate = BigDecimal.ZERO;
		BigDecimal expectedMonthlyInterestRate = BigDecimal.ZERO;

		BigDecimal actualMonthlyInterestRate = InterestRateUtils.calculateMonthlyInterest(annualInterestRate);

		assertThat(actualMonthlyInterestRate).isEqualByComparingTo(expectedMonthlyInterestRate);
	}

	@Test
	void calculateMonthlyInterest_shouldHandleLargeValue() {
		BigDecimal annualInterestRate = BigDecimal.valueOf(1200.0);
		BigDecimal expectedMonthlyInterestRate = BigDecimal.valueOf(1.0);

		BigDecimal actualMonthlyInterestRate = InterestRateUtils.calculateMonthlyInterest(annualInterestRate);

		assertThat(actualMonthlyInterestRate).isEqualByComparingTo(expectedMonthlyInterestRate);
	}

}
