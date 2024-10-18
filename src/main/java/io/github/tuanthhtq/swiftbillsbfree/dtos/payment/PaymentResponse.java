package io.github.tuanthhtq.swiftbillsbfree.dtos.payment;

import java.time.Instant;
import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record PaymentResponse(
		String qrCode,
		String cardNumber,
		String cardHolderName,
		Instant creationDate,

		Set<PaymentItem> items
) {
}
