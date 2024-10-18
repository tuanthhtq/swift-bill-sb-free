package io.github.tuanthhtq.swiftbillsbfree.dtos.auth;

import io.github.tuanthhtq.swiftbillsbfree.dtos.user.AddressRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author io.github.tuanthhtq
 */

public record SignUpRequest(
		@Pattern(regexp = "^0[1-9][0-9]{8,12}$", message = "Invalid phone number")
		@NotEmpty(message = "Phone number cannot be empty")
		String phone,

		@NotEmpty(message = "Password cannot be empty")
		@Size(min = 4, message = "Password minimum length 4 characters")
		String password,

		@NotEmpty(message = "Confirm password cannot be empty")
		String confirmPassword,

		@NotEmpty(message = "ID number cannot be empty")
		@Pattern(regexp = "[0-9]{9,15}$", message = "Invalid ID number")
		String idNumber,

		@NotEmpty(message = "Name cannot be empty")
		@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
		String fullName,

		@Valid
		AddressRequest address
) {
}
