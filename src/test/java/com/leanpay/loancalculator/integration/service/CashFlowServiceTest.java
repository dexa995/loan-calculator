package com.leanpay.loancalculator.integration.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.service.CashFlowService;

@SpringBootTest
@Testcontainers
public class CashFlowServiceTest {
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

	@Autowired
	private CashFlowService cashFlowService;

	@Test
	void generateShouldReturnCashFlowResponseDto_whenDtoIsValid() {
		CreateCashFlowDto dto = new CreateCashFlowDto(15000.0, 6.0, 8L);
		CashFlowResponseDto response = cashFlowService.generate(dto);
		assertThat(response).isNotNull();
		assertThat(response.loanAmount()).isEqualByComparingTo(BigDecimal.valueOf(15000));
		assertThat(response.loanTerm()).isEqualTo(8);
		assertThat(response.interestRate()).isEqualByComparingTo(BigDecimal.valueOf(6));
		assertThat(response.installmentAmount()).isPositive();
		assertThat(response.cashFlowItems())
			.isNotNull()
			.hasSize(8);
	}

}
