package io.github.tuanthhtq.swiftbillsbfree.dtos.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * @author io.github.tuanthhtq
 */

public record SignInRequest(
		@Pattern(regexp = "^0[1-9][0-9]{8,}$", message = "Invalid phone number")
		@NotEmpty(message = "Phone number cannot be empty")
		String phone,

		@NotEmpty(message = "Password cannot be empty")
		String password
) {
}
