package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.validation.constraints.Size;

/**
 * @author io.github.tuanthhtq
 */

public record ReceiptSearchCriteria(
		@Size(max = Length.NAME)
		String cashierName,

		@Size(max = Length.PHONE)
		String cashierPhone,

		@Size(max = Length.NAME)
		String customerName,

		@Size(max = Length.PHONE)
		String customerPhone,
		String fromDate,
		String toDate
) {
}
