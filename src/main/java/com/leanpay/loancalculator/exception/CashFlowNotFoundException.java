package com.leanpay.loancalculator.exception;

public class CashFlowNotFoundException extends BaseException{

	private static final String message = "Cash flow not found";

	public CashFlowNotFoundException(String message) {
		super(message);
	}

	public CashFlowNotFoundException() {
		super(message);
	}

}
