package com.leanpay.loancalculator.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class Auditable {
	@CreatedDate
	@Column(name = "created", updatable = false)
	public LocalDateTime created;
}
