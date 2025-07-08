package com.leanpay.loancalculator.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.exception.CashFlowNotFoundException;
import com.leanpay.loancalculator.mapper.CashFlowMapper;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.repository.CashFlowRepository;
import com.leanpay.loancalculator.service.CashFlowItemService;
import com.leanpay.loancalculator.service.CashFlowService;
import com.leanpay.loancalculator.util.CashFlowCalculatorUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CashFlowServiceImpl implements CashFlowService {

	private final CashFlowRepository cashFlowRepository;
	private final CashFlowItemService cashFlowItemService;
	private final CashFlowMapper cashFlowMapper;

	@Override
	public CashFlowResponseDto generate(CreateCashFlowDto cashFlowDto) {

		return cashFlowRepository.findByLoanAmountAndLoanTermAndInterestRate(
				BigDecimal.valueOf(cashFlowDto.loanAmount()), cashFlowDto.loanTerm(), BigDecimal.valueOf(cashFlowDto.interestRate())
			)
			.map(cashFlowMapper::toDto)
			.orElseGet(() -> {
				BigDecimal installmentAmount = CashFlowCalculatorUtil.calculateInstallmentAmount(new BigDecimal(cashFlowDto.loanAmount()),
					new BigDecimal(cashFlowDto.interestRate()), cashFlowDto.loanTerm().intValue());

				BigDecimal totalClientDue = installmentAmount.multiply(BigDecimal.valueOf(cashFlowDto.loanTerm()));

				CashFlow cashFlow = new CashFlow();
				cashFlow.setInstallmentAmount(installmentAmount);
				cashFlow.setLoanTerm(cashFlowDto.loanTerm());
				cashFlow.setInterestRate(BigDecimal.valueOf(cashFlowDto.interestRate()));
				cashFlow.setLoanAmount(BigDecimal.valueOf(cashFlowDto.loanAmount()));
				cashFlow.setTotalClientDue(totalClientDue);
				cashFlow.setInterestAmount(totalClientDue.subtract(BigDecimal.valueOf(cashFlowDto.loanAmount())));

				cashFlow = cashFlowRepository.save(cashFlow);

				List<CashFlowItemResponseDto> cashFlowItems = cashFlowItemService.generateInstallmentPlan(cashFlow.getId());

				return CashFlowResponseDto.builder()
					.id(cashFlow.getId())
					.loanAmount(cashFlow.getLoanAmount())
					.loanTerm(cashFlow.getLoanTerm())
					.interestRate(cashFlow.getInterestRate())
					.interestAmount(cashFlow.getInterestAmount())
					.totalClientDue(cashFlow.getTotalClientDue())
					.installmentAmount(cashFlow.getInstallmentAmount())
					.cashFlowItems(cashFlowItems)
					.created(cashFlow.getCreated())
					.build();
			});

	}

	@Override
	public CashFlowResponseDto find(Long id) {
		return cashFlowRepository.findById(id)
			.map(cashFlowMapper::toDto)
			.orElseThrow(CashFlowNotFoundException::new);
	}

}
