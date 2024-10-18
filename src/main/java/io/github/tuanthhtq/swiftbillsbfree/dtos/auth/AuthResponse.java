package io.github.tuanthhtq.swiftbillsbfree.dtos.auth;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

public record AuthResponse(
		String phone,
		String accessToken,
		Set<String> roles
) {
}
