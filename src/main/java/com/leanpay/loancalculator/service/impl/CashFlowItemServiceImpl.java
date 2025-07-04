package com.leanpay.loancalculator.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.mapper.CashFlowItemMapper;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.model.CashFlowItem;
import com.leanpay.loancalculator.repository.CashFlowItemRepository;
import com.leanpay.loancalculator.repository.CashFlowRepository;
import com.leanpay.loancalculator.service.CashFlowItemService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CashFlowItemServiceImpl implements CashFlowItemService {
	private final CashFlowRepository cashFlowRepository;
	private final CashFlowItemRepository cashFlowItemRepository;

	private final CashFlowItemMapper cashFlowItemMapper;
	@Override
	public List<CashFlowItemResponseDto> generateInstallmentPlan(Long cashFlowId) {
		//Exception handling
		CashFlow cashFlow = cashFlowRepository.findById(cashFlowId).orElseThrow();

		BigDecimal principal = cashFlow.getLoanAmount();
		int term = cashFlow.getLoanTerm().intValue();
		BigDecimal annualRate = cashFlow.getInterestRate();
		BigDecimal fixedInstallment = cashFlow.getInstallmentAmount();

		// Calculate monthly interest rate
		BigDecimal monthlyRate = annualRate
			.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
			.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

		BigDecimal remaining = principal;
		List<CashFlowItem> items = new ArrayList<>();

		for (int i = 1; i <= term; i++) {
			BigDecimal interest = remaining.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principalPart = fixedInstallment.subtract(interest).setScale(2, RoundingMode.HALF_UP);

			// Adjust last installment if rounding caused underpayment
			if (i == term && remaining.compareTo(principalPart) < 0) {
				principalPart = remaining;
				fixedInstallment = principalPart.add(interest).setScale(2, RoundingMode.HALF_UP);
			}

			BigDecimal newRemaining = remaining.subtract(principalPart).setScale(2, RoundingMode.HALF_UP);

			CashFlowItem item = new CashFlowItem();
			item.setCashFlow(cashFlow);
			item.setMonth(i);
			item.setPrincipalAmount(principalPart);
			item.setInterestAmount(interest);
			item.setInstallmentAmount(fixedInstallment);
			item.setLoanBalance(newRemaining);

			items.add(item);
			remaining = newRemaining;
		}

		return cashFlowItemMapper.toDtoList(cashFlowItemRepository.saveAll(items));
	}
}
