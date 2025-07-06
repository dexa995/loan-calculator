package com.leanpay.loancalculator.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.mapper.CashFlowMapper;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.repository.CashFlowRepository;
import com.leanpay.loancalculator.service.CashFlowItemService;
import com.leanpay.loancalculator.service.impl.CashFlowServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CashFlowServiceTest {

	//which we are testing is anotated wit injectMocks
	@InjectMocks
	private CashFlowServiceImpl cashFlowServiceImpl;

	@Mock
	private CashFlowRepository cashFlowRepository;
	@Mock
	private CashFlowItemService cashFlowItemService;
	@Mock
	private CashFlowMapper  cashFlowMapper;


	@Test
	void generate_shouldReturnCashFlowResponseDto_whenDtoIsValid() {

		when(cashFlowRepository.save(any(CashFlow.class))).thenReturn(mockCashFlow());
		when(cashFlowItemService.generateInstallmentPlan(anyLong())).thenReturn(new ArrayList<>());

		CreateCashFlowDto dto = new CreateCashFlowDto(15000.0, 6.0, 8L);

		//when(cashFlowMapper.toDto(any(CashFlow.class))).thenReturn(mockCashFlowResponseDto());
		CashFlowResponseDto response = cashFlowServiceImpl.generate(dto);
		assertThat(response).isNotNull();
		assertThat(response.loanAmount()).isEqualByComparingTo(BigDecimal.valueOf(15000));
		assertThat(response.loanTerm()).isEqualTo(8);
		assertThat(response.interestRate()).isEqualByComparingTo(BigDecimal.valueOf(6));
		assertThat(response.installmentAmount()).isPositive();
	}

	private CashFlow mockCashFlow() {
		CashFlow cashFlow = new CashFlow();

		cashFlow.setId(1L);
		cashFlow.setLoanAmount(BigDecimal.valueOf(15000));
		cashFlow.setInterestRate(BigDecimal.valueOf(6));
		cashFlow.setInstallmentAmount(BigDecimal.valueOf(150));
		cashFlow.setCashFlowItems(new ArrayList<>());
		cashFlow.setLoanTerm(8L);

		return cashFlow;
	}

	private CashFlowResponseDto mockCashFlowResponseDto() {
		return new CashFlowResponseDto(
			1L,
			new BigDecimal("10000.00"),
			12L,
			LocalDateTime.of(2025, 1, 1, 10, 0),
			new BigDecimal("5.5"),
			new BigDecimal("600.00"),
			new BigDecimal("10600.00"),
			new BigDecimal("883.33"),
			new ArrayList<>()
			);
	}
}
