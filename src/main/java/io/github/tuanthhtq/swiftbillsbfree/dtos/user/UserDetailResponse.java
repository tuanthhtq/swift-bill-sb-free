package io.github.tuanthhtq.swiftbillsbfree.dtos.user;

import java.time.Instant;
import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record UserDetailResponse(
		String name,
		String phone,
		String phone2,
		String idNumber,
		Set<String> roles,
		String status,
		String address,
		Long storeId,
		Instant createdDate
) {
}
