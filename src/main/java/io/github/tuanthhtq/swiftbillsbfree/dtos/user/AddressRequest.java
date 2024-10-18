package io.github.tuanthhtq.swiftbillsbfree.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * @author io.github.tuanthhtq
 */

public record AddressRequest(

		@NotEmpty(message = "City/Province cannot be empty")
		@Size(max = 30, message = "Max city/province length 30 characters")
		String cityOrProvince,

		@NotEmpty(message = "District cannot be empty")
		@Size(max = 30, message = "Max district length 30 characters")
		String district,

		@NotEmpty(message = "Ward cannot be empty")
		@Size(max = 30, message = "Max ward length 30 characters")
		String ward,

		@NotEmpty(message = "Address detail cannot be empty")
		@Size(max = 80, message = "Max address detail length 80 characters")
		String detailedAddress
) {
}
