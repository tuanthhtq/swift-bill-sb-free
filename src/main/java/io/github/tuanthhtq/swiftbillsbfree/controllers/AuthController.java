package io.github.tuanthhtq.swiftbillsbfree.controllers;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.AuthResponse;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.ChangePasswordRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.SignInRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.SignUpRequest;
import io.github.tuanthhtq.swiftbillsbfree.services.AuthServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author io.github.tuanthhtq
 */

@RestController
@RequestMapping("${app.request-prefix}/auth")
public class AuthController {

	private final AuthServices authServices;

	@Autowired
	public AuthController(AuthServices authServices) {
		this.authServices = authServices;
	}

	@PostMapping("/sign-in")
	public ResponseEntity<Response<AuthResponse>> login(
			@Valid @RequestBody SignInRequest request,
			BindingResult bindingResult
	) {
		Response<AuthResponse> response = authServices.signIn(request, bindingResult);
		return new ResponseEntity<>(
				response,
				HttpStatusCode.valueOf(response.getStatusCode())
		);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Response<AuthResponse>> signUp(
			@Valid @RequestBody SignUpRequest request,
			BindingResult bindingResult
	) {
		Response<AuthResponse> response = authServices.signUp(request, bindingResult);
		return new ResponseEntity<>(
				response,
				HttpStatusCode.valueOf(response.getStatusCode())
		);
	}

	@PutMapping("/change-password")
	public ResponseEntity<Response<AuthResponse>> changePassword(
			@Valid @RequestBody ChangePasswordRequest request,
			BindingResult bindingResult
	) {
		Response<AuthResponse> response = authServices.changePassword(request, bindingResult);
		return new ResponseEntity<>(
				response,
				HttpStatusCode.valueOf(response.getStatusCode())
		);
	}
}
