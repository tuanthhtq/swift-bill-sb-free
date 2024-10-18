package io.github.tuanthhtq.swiftbillsbfree.services;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.AuthResponse;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.ChangePasswordRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.SignInRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.SignUpRequest;
import org.springframework.validation.BindingResult;

/**
 * @author io.github.tuanthhtq
 */

public interface AuthServices {

	/**
	 * Authenticate user with phone and password
	 *
	 * @param request       {@link SignInRequest}
	 * @param bindingResult fields validation result
	 * @return {@link Response} of type {@link AuthResponse}
	 */
	Response<AuthResponse> signIn(SignInRequest request, BindingResult bindingResult);

	/**
	 * Shop owner signup
	 *
	 * @param request       {@link SignUpRequest}
	 * @param bindingResult fields validation result
	 * @return {@link Response} of type {@link AuthResponse}
	 */
	Response<AuthResponse> signUp(SignUpRequest request, BindingResult bindingResult);

	/**
	 * Change password
	 *
	 * @param request       {@link ChangePasswordRequest}
	 * @param bindingResult fields validation result
	 * @return {@link Response} of type {@link AuthResponse}
	 */
	Response<AuthResponse> changePassword(ChangePasswordRequest request, BindingResult bindingResult);
}
