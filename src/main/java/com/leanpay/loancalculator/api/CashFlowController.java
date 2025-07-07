package com.leanpay.loancalculator.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leanpay.loancalculator.api.request.CreateCashFlowDto;
import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.model.CashFlow;
import com.leanpay.loancalculator.service.CashFlowService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cash-flows")
@RequiredArgsConstructor
public class CashFlowController {
	private final CashFlowService cashFlowService;

	@PostMapping
	public CashFlowResponseDto generate(@Valid @RequestBody CreateCashFlowDto cashFlowDto) {
		return cashFlowService.generate(cashFlowDto);
	}

	@GetMapping("{id}")
	public CashFlowResponseDto get(@PathVariable Long id) {
		return cashFlowService.find(id);
	}

}
