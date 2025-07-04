package com.leanpay.loancalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leanpay.loancalculator.model.CashFlow;

public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {
}
