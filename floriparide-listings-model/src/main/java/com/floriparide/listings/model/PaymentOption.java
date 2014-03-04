package com.floriparide.listings.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public enum PaymentOption {

	CASH, VISA, MASTERCARD, CHEQUE, AMERICAN_EXPRESS;

	public static List<PaymentOption> split(@NotNull List<String> paymentOptions) {
		ArrayList<PaymentOption> result = new ArrayList<PaymentOption>(paymentOptions.size());

		for (String s : paymentOptions)
			result.add(PaymentOption.valueOf(s)); //todo validate

		return result;
	}

}
