package com.leanpay.loancalculator.unit.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.exception.CashFlowNotFoundException;
import com.leanpay.loancalculator.mapper.CashFlowItemMapper;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.repository.CashFlowItemRepository;
import com.leanpay.loancalculator.repository.CashFlowRepository;
import com.leanpay.loancalculator.service.impl.CashFlowItemServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CashFlowItemServiceTest {

	@Mock
	CashFlowRepository cashFlowRepository;
	@Mock
	CashFlowItemRepository cashFlowItemRepository;
	@Mock
	CashFlowItemMapper cashFlowItemMapper;

	@InjectMocks
	CashFlowItemServiceImpl cashFlowItemServiceImpl;

	@Test
	void shouldGenerateInstallmentPlanCorrectly() {
		CashFlow cashFlow = new CashFlow();
		cashFlow.setId(1L);
		cashFlow.setLoanAmount(BigDecimal.valueOf(10000));
		cashFlow.setLoanTerm(3L);
		cashFlow.setInterestRate(BigDecimal.valueOf(10));
		cashFlow.setInstallmentAmount(BigDecimal.valueOf(3300));
		cashFlow.setCreated(LocalDateTime.of(2024, 1, 31, 0, 0)); // rubni datum

		when(cashFlowRepository.findById(1L)).thenReturn(Optional.of(cashFlow));
		when(cashFlowItemRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
		//testing mapper
		when(cashFlowItemMapper.toDtoList(anyList())).thenReturn(
			List.of(
				mock(CashFlowItemResponseDto.class),
				mock(CashFlowItemResponseDto.class),
				mock(CashFlowItemResponseDto.class)
			)
		);

		List<CashFlowItemResponseDto> result = cashFlowItemServiceImpl.generateInstallmentPlan(1L);

		assertEquals(3, result.size());
		verify(cashFlowItemRepository).saveAll(anyList());
		verify(cashFlowRepository).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenCashFlowNotFound() {
		when(cashFlowRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(CashFlowNotFoundException.class,
			() -> {
				cashFlowItemServiceImpl.generateInstallmentPlan(999L);
			});
	}

}
