package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * @author io.github.tuanthhtq
 */

public record PaymentRequest(
		@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
		String customerName,

		@Pattern(regexp = "^0[1-9][0-9]{8,12}$", message = "Invalid phone number")
		String customerPhone,

		Long bankAccountId,

		float discount,

		List<PaymentItem> items,

		Long storeId
) {
}
