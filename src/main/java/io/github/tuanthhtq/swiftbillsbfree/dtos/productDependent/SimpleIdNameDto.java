package io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author io.github.tuanthhtq
 */

public record SimpleIdNameDto(
		Long id,

		@Size(max = 50, message = "Maximum 50 characters")
		@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
		String name
) {
}
