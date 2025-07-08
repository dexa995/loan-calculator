package com.leanpay.loancalculator.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cash_flow")
@Data
public class CashFlow extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_flow_id_seq")
	@SequenceGenerator(name = "cash_flow_id_seq", sequenceName = "cash_flow_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "loan_amount", nullable = false)
	private BigDecimal loanAmount;

	@Column(name = "loan_term")
	private Long loanTerm;

	@Column(name = "interest_rate")
	private BigDecimal interestRate;

	@Column(name = "interest_amount")
	private BigDecimal interestAmount;

	@Column(name = "total_client_due")
	private BigDecimal totalClientDue;

	@Column(name = "installment_amount")
	private BigDecimal installmentAmount;

	@OneToMany(mappedBy = "cashFlow", fetch = FetchType.EAGER)
	private List<CashFlowItem> cashFlowItems = new ArrayList<>();

}
