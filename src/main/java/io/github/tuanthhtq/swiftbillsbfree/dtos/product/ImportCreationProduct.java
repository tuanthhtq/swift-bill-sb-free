package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleIdNameDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author io.github.tuanthhtq
 */

public record ImportCreationProduct(
		@NotNull(message = "Product name cannot be empty")
		@Size(max = 50, message = "Product name cannot exceed 50 characters")
		String productName,

		@Size(max = 150, message = "Product description cannot exceed 150 characters")
		String productDescription,

		@DecimalMin(value = "1.0", message = "Price must be greater than 0")
		float price,

		@DecimalMin(value = "1.0", message = "Cost price must be greater than 0")
		float costPrice,

		@DecimalMin(value = "1.0", message = "Amount must be greater than 0")
		float amount,

		@Valid
		SimpleIdNameDto measureUnit,

		@NotNull(message = "Barcode cannot be empty")
		String barcode,

		@Valid
		SimpleIdNameDto brand,

		@NotNull(message = "Images are required")
		List<String> imageUrls,

		@Valid
		List<SimpleIdNameDto> categories

) {
}
