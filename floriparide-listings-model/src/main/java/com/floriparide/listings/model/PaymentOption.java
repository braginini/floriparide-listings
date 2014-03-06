package com.floriparide.listings.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public enum PaymentOption {

	CASH("cash"), VISA("visa"), MASTERCARD("mastercard"), CHEQUE("cheque"), AMERICAN_EXPRESS("american_express");

	String type;

	PaymentOption(String type) {
		this.type = type;
	}

	static final HashMap<String, PaymentOption> map = new HashMap<>();

	static {
		for (PaymentOption p : PaymentOption.values())
			map.put(p.getType(), p);
	}

	public String getType() {
		return type;
	}

	public static PaymentOption lookup(String type) {
		return (type != null) ? map.get(type.toLowerCase()) : null;
	}

	public static List<PaymentOption> split(@NotNull List<String> paymentOptions) {
		ArrayList<PaymentOption> result = new ArrayList<PaymentOption>(paymentOptions.size());

		for (String s : paymentOptions)
			result.add(PaymentOption.lookup(s)); //todo validate

		return result;
	}

}
