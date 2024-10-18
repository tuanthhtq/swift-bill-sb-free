package io.github.tuanthhtq.swiftbillsbfree.services;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.user.UserDetailResponse;

/**
 * @author io.github.tuanthhtq
 */

public interface UserServices {

	Response<UserDetailResponse> getUserDetail(Long userId);


}
