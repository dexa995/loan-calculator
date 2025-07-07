package com.leanpay.loancalculator.service;

import java.util.List;

import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;

public interface CashFlowItemService {
	List<CashFlowItemResponseDto> generateInstallmentPlan(Long cashFlowId);
}
