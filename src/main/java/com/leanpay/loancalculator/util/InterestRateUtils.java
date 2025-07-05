package com.leanpay.loancalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class InterestRateUtils {

	private InterestRateUtils() {
	}

	public static BigDecimal calculateMonthlyInterest(BigDecimal annualRatePercent) {
		return annualRatePercent
			.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
			.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
	}

}
