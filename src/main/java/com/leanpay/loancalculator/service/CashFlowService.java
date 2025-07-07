package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;

public interface CashFlowService {
	CashFlowResponseDto generate(CreateCashFlowDto cashFlowDto);

	CashFlowResponseDto find(Long id);
}
