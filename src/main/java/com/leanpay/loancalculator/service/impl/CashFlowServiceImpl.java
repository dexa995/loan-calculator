package com.leanpay.loancalculator.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.mapper.CashFlowItemMapper;
import com.leanpay.loancalculator.mapper.CashFlowMapper;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.model.CashFlowItem;
import com.leanpay.loancalculator.repository.CashFlowRepository;
import com.leanpay.loancalculator.service.CashFlowItemService;
import com.leanpay.loancalculator.service.CashFlowService;
import com.leanpay.loancalculator.util.InterestRateUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CashFlowServiceImpl implements CashFlowService {

	private final CashFlowRepository cashFlowRepository;
	private final CashFlowItemService cashFlowItemService;
	@Override
	public CashFlowResponseDto generate(CreateCashFlowDto cashFlowDto) {
		BigDecimal installmentAmount = calculateInstallmentAmount(cashFlowDto);
		BigDecimal totalClientDue = installmentAmount.multiply(BigDecimal.valueOf(cashFlowDto.loanTerm()));

		CashFlow cashFlow = new CashFlow();
		cashFlow.setInstallmentAmount(installmentAmount);
		cashFlow.setLoanTerm(cashFlowDto.loanTerm());
		cashFlow.setInterestRate(BigDecimal.valueOf(cashFlowDto.interestRate()));
		cashFlow.setLoanAmount(BigDecimal.valueOf(cashFlowDto.loanAmount()));
		cashFlow.setTotalClientDue (totalClientDue);
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

		//not supported with records
//		return cashFlowMapper.toDto(cashFlow)
//			.toBuilder()
//			.cashFlowItems(cashFlowItems)
//			.build();

	}

	private BigDecimal calculateInstallmentAmount(CreateCashFlowDto cashFlowDto){
		BigDecimal loanAmount = new BigDecimal(cashFlowDto.loanAmount());
		BigDecimal monthlyInterestRate = InterestRateUtils.calculateMonthlyInterest(new BigDecimal(cashFlowDto.interestRate()));

		// (1 + monthlyInterestRate)^numberOfInstallments
		BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyInterestRate);
		BigDecimal power = onePlusRate.pow(cashFlowDto.loanTerm().intValue());

		System.out.println(onePlusRate);
		System.out.println(power);

		// numerator: r * (1 + r)^n
		BigDecimal numerator = monthlyInterestRate.multiply(power);
		System.out.println(numerator);

		// denominator: (1 + r)^n - 1
		BigDecimal denominator = power.subtract(BigDecimal.ONE);
		System.out.println(denominator);

		// A = P * (numerator / denominator)
		BigDecimal monthlyInstallment = loanAmount.multiply(numerator.divide(denominator, RoundingMode.HALF_UP));
		System.out.println(monthlyInstallment);

		// Final rounded value to 2 decimal places (currency format)
		return monthlyInstallment.setScale(2, RoundingMode.HALF_UP);
	}

}
