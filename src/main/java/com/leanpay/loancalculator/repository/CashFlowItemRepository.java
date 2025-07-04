package com.leanpay.loancalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leanpay.loancalculator.model.CashFlowItem;

public interface CashFlowItemRepository extends JpaRepository<CashFlowItem, Long> {
}
