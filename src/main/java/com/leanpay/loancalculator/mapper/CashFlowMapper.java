package com.leanpay.loancalculator.mapper;

import org.mapstruct.Mapper;

import com.leanpay.loancalculator.api.response.CashFlowResponseDto;
import com.leanpay.loancalculator.model.CashFlow;

@Mapper(componentModel = "spring")
public interface CashFlowMapper {

	CashFlowResponseDto toDto(CashFlow cashFlow);

}
