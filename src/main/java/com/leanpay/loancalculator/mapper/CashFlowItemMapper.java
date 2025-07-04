package com.leanpay.loancalculator.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.leanpay.loancalculator.api.response.CashFlowItemResponseDto;
import com.leanpay.loancalculator.model.CashFlowItem;

@Mapper(componentModel = "spring")
public interface CashFlowItemMapper {

	CashFlowItemResponseDto toDto(CashFlowItem item);

	List<CashFlowItemResponseDto> toDtoList(List<CashFlowItem> items);

	List<CashFlowItem> toEntityList(List<CashFlowItemResponseDto> items);
}
