package io.github.tuanthhtq.swiftbillsbfree.dtos.store;

import java.time.Instant;
import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record StoreDetailResponse(
		String storeName,
		String storeAddress,
		Set<String> images,
		Instant open,
		Instant close,
		String phone,
		String ownerName
) {
}
