package io.github.tuanthhtq.swiftbillsbfree.dtos.bill;

import jakarta.validation.constraints.Pattern;

/**
 * @author io.github.tuanthhtq
 */

public record BillSearchRequest(
		@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
		String cashierName,

		@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
		String customerName,

		@Pattern(regexp = "^0[1-9][0-9]{8,12}$", message = "Invalid phone number")
		String cashierPhone,

		@Pattern(regexp = "^0[1-9][0-9]{8,12}$", message = "Invalid phone number")
		String customerPhone
) {
}
