package com.leanpay.loancalculator.unit.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leanpay.loancalculator.api.CashFlowController;
import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.exception.CashFlowNotFoundException;
import com.leanpay.loancalculator.service.CashFlowService;

@WebMvcTest(CashFlowController.class)
public class CashFlowControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("removal")
	@MockBean
	private CashFlowService cashFlowService;

	@Test
	void shouldReturn200AndCashFlow_whenPostValidDto() throws Exception {
		CreateCashFlowDto dto = new CreateCashFlowDto(10000.0, 5.5, 12L);

		List<CashFlowItemResponseDto> items = List.of(
			new CashFlowItemResponseDto(
				1L,
				1,
				LocalDate.of(2025, 8, 1),
				BigDecimal.valueOf(850),
				BigDecimal.valueOf(800),
				BigDecimal.valueOf(50),
				BigDecimal.valueOf(9200)
			)
		);

		CashFlowResponseDto response = new CashFlowResponseDto(
			1L,
			BigDecimal.valueOf(10000),
			12L,
			LocalDateTime.now(),
			BigDecimal.valueOf(5.5),
			BigDecimal.valueOf(200),
			BigDecimal.valueOf(10500),
			BigDecimal.valueOf(850),
			items
		);

		Mockito.when(cashFlowService.generate(any())).thenReturn(response);

		mockMvc.perform(post("/cash-flows")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.loanAmount").value(10000))
			.andExpect(jsonPath("$.cashFlowItems").isArray())
			.andExpect(jsonPath("$.cashFlowItems[0].month").value(1));
	}

	@Test
	void shouldReturnBadRequest_whenPostInvalidDto() throws Exception {
		CreateCashFlowDto invalidDto = new CreateCashFlowDto(null, -1.0, 0L);

		mockMvc.perform(post("/cash-flows")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidDto)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturn200_whenGetExistingCashFlow() throws Exception {
		CashFlowResponseDto response = new CashFlowResponseDto(
			1L,
			BigDecimal.valueOf(5000),
			6L,
			LocalDateTime.now(),
			BigDecimal.valueOf(4.5),
			BigDecimal.valueOf(100),
			BigDecimal.ZERO,
			BigDecimal.valueOf(860),
			Collections.emptyList()
		);

		Mockito.when(cashFlowService.find(1L)).thenReturn(response);

		mockMvc.perform(get("/cash-flows/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.loanAmount").value(5000))
			.andExpect(jsonPath("$.loanTerm").value(6));
	}

	@Test
	void shouldReturnException_whenCashFlowNotFound() throws Exception {
		Mockito.when(cashFlowService.find(eq(99L)))
			.thenThrow(new CashFlowNotFoundException());

		mockMvc.perform(get("/cash-flows/99"))
			.andExpect(status().isInternalServerError());
	}

}
