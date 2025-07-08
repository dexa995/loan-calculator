package com.leanpay.loancalculator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cash_flow_item")
@Data
public class CashFlowItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_flow_item_id_seq")
	@SequenceGenerator(name = "cash_flow_item_id_seq", sequenceName = "cash_flow_item_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "month")
	private Integer month;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "installment_amount")
	private BigDecimal installmentAmount;

	@Column(name = "principal_amount")
	private BigDecimal principalAmount;

	@Column(name = "interest_amount")
	private BigDecimal interestAmount;

	@Column(name = "loan_balance")
	private BigDecimal loanBalance;

	@ManyToOne
	@JoinColumn(name = "cash_flow_id", referencedColumnName = "id")
	private CashFlow cashFlow;

}
