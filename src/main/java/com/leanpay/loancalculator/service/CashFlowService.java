package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.model.CashFlow;

public interface CashFlowService {
	CashFlowResponseDto generate(CreateCashFlowDto cashFlowDto);

	CashFlowResponseDto find(Long id);
}
