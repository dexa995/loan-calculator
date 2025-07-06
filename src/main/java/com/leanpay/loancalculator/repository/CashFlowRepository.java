package com.leanpay.loancalculator.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leanpay.loancalculator.model.CashFlow;

public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {
	Optional<CashFlow> findByLoanAmountAndLoanTermAndInterestRate(BigDecimal loanAmount, Long loanTerm, BigDecimal interestRate);
}
