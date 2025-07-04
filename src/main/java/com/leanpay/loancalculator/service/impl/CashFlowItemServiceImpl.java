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
		CashFlow cashFlow = cashFlowRepository.findById(cashFlowId).orElseThrow();

		BigDecimal principal = cashFlow.getLoanAmount();
		int term = cashFlow.getLoanTerm().intValue();
		BigDecimal annualRate = cashFlow.getInterestRate();
		BigDecimal fixedInstallment = cashFlow.getInstallmentAmount();

		BigDecimal monthlyRate = annualRate
			.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
			.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP); // koristi više decimala za internu preciznost

		BigDecimal remaining = principal;
		List<CashFlowItem> items = new ArrayList<>();

		for (int i = 1; i <= term; i++) {
			BigDecimal interest = remaining.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principalPart;
			BigDecimal installment;
			BigDecimal newRemaining;

			if (i == term) {
				// Last installment: clean remaining
				principalPart = remaining.setScale(2, RoundingMode.HALF_UP);
				installment = principalPart.add(interest).setScale(2, RoundingMode.HALF_UP);
				newRemaining = BigDecimal.ZERO;
			} else {
				principalPart = fixedInstallment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
				installment = fixedInstallment;
				newRemaining = remaining.subtract(principalPart).setScale(2, RoundingMode.HALF_UP);
			}

			CashFlowItem item = new CashFlowItem();
			item.setCashFlow(cashFlow);
			item.setMonth(i);
			item.setPrincipalAmount(principalPart);
			item.setInterestAmount(interest);
			item.setInstallmentAmount(installment);
			item.setLoanBalance(newRemaining);

			items.add(item);
			remaining = newRemaining;
		}

		return cashFlowItemMapper.toDtoList(cashFlowItemRepository.saveAll(items));
	}

}
