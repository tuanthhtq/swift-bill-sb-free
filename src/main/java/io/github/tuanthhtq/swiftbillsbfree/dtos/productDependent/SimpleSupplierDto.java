package io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent;

import jakarta.validation.constraints.Pattern;

/**
 * @author io.github.tuanthhtq
 */

public record SimpleSupplierDto(
		Long id,

		@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
		String name,

		String address,

		@Pattern(regexp = "^0[1-9][0-9]{8,12}$", message = "Invalid phone number")
		String phone
) {
}
