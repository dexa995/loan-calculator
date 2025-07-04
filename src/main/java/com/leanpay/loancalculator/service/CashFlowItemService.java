package com.leanpay.loancalculator.service;

import java.util.List;

import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.model.CashFlowItem;

public interface CashFlowItemService {
	List<CashFlowItemResponseDto> generateInstallmentPlan(Long cashFlowId);
}
