package io.github.tuanthhtq.swiftbillsbfree.dtos.product;

/**
 * @author io.github.tuanthhtq
 */

public record ImportCreationResponse(
		String creatorName,
		float totalCost,
		float estimatedIncome
) {
}
