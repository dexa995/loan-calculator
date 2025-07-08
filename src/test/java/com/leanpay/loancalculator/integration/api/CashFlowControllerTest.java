package com.leanpay.loancalculator.integration.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


import java.math.BigDecimal;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CashFlowControllerTest {
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldReturn200AndCashFlowResponse_whenValidDtoPosted() {
		CreateCashFlowDto cashFlowDto = new CreateCashFlowDto(1000.0, 6.6, 24L);

		ResponseEntity<CashFlowResponseDto> response = restTemplate.postForEntity(
			"/cash-flows", cashFlowDto, CashFlowResponseDto.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().loanAmount()).isEqualByComparingTo("1000");
		assertThat(response.getBody().cashFlowItems()).hasSize(24);
		assertThat(response.getBody()
			.cashFlowItems()).allSatisfy((item) -> assertThat(item.installmentAmount())
			.isBetween(new BigDecimal("44.58"), new BigDecimal("44.64")));
	}

}
