package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * @author io.github.tuanthhtq
 */

public record ReceiptSearchCriteria(
		Long storeId,

		@Size(max = Length.NAME)
		String cashierNameOrPhone,

		@Size(max = Length.NAME)
		String customerNameOrPhone,

		Instant fromDate,
		Instant toDate,
		int pageNo,

		int sortDirection
) {
}
