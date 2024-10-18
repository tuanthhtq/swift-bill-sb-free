package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleSupplierDto;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author io.github.tuanthhtq
 */

public record ImportCreationRequest(
		@Valid
		SimpleSupplierDto supplier,

		@Valid
		List<ImportCreationProduct> products
) {
}
