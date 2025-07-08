package com.leanpay.loancalculator.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.exception.CashFlowNotFoundException;
import com.leanpay.loancalculator.mapper.CashFlowItemMapper;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.model.CashFlowItem;
import com.leanpay.loancalculator.repository.CashFlowItemRepository;
import com.leanpay.loancalculator.repository.CashFlowRepository;
import com.leanpay.loancalculator.service.CashFlowItemService;
import com.leanpay.loancalculator.util.CashFlowCalculatorUtil;

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
		CashFlow cashFlow = cashFlowRepository.findById(cashFlowId).orElseThrow(CashFlowNotFoundException::new);

		BigDecimal principal = cashFlow.getLoanAmount();
		int term = cashFlow.getLoanTerm().intValue();
		BigDecimal fixedInstallment = cashFlow.getInstallmentAmount();

		BigDecimal monthlyRate = CashFlowCalculatorUtil.calculateMonthlyInterest(cashFlow.getInterestRate());

		BigDecimal remaining = principal;
		List<CashFlowItem> items = new ArrayList<>();

		for (int i = 1; i <= term; i++) {
			BigDecimal interest = remaining.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principalPart;
			BigDecimal installment;
			BigDecimal newRemaining;

			if (i == term) {
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
			item.setPaymentDate(cashFlow.getCreated().toLocalDate().plusMonths(i));
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
