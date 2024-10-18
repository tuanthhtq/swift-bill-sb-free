package io.github.tuanthhtq.swiftbillsbfree.services.impl;

import io.github.tuanthhtq.swiftbillsbfree.configs.implement.UserDetailServiceImpl;
import io.github.tuanthhtq.swiftbillsbfree.configs.implement.UserDetailsImpl;
import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.AuthResponse;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.ChangePasswordRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.SignInRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.auth.SignUpRequest;
import io.github.tuanthhtq.swiftbillsbfree.entities.Addresses;
import io.github.tuanthhtq.swiftbillsbfree.entities.Roles;
import io.github.tuanthhtq.swiftbillsbfree.entities.Users;
import io.github.tuanthhtq.swiftbillsbfree.repositories.RolesRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.UsersRepository;
import io.github.tuanthhtq.swiftbillsbfree.services.AuthServices;
import io.github.tuanthhtq.swiftbillsbfree.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author io.github.tuanthhtq
 */

@Service
public class AuthServiceImpl extends CommonConcrete implements AuthServices {

	private final UsersRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailServiceImpl userDetailService;
	private final AuthenticationManager authenticationManager;
	private final RolesRepository rolesRepo;

	@Autowired
	public AuthServiceImpl(
			UsersRepository userRepo,
			PasswordEncoder passwordEncoder,
			UserDetailServiceImpl userDetailService,
			AuthenticationManager authenticationManager,
			RolesRepository rolesRepository
	) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.userDetailService = userDetailService;
		this.authenticationManager = authenticationManager;
		this.rolesRepo = rolesRepository;
	}


	/**
	 * Authenticate user with phone and password
	 *
	 * @param request       {@link SignInRequest}
	 * @param bindingResult fields validation result
	 * @return {@link Response} of type {@link AuthResponse}
	 */
	@Override
	public Response<AuthResponse> signIn(SignInRequest request, BindingResult bindingResult) {
		Response<AuthResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setMessage("Sign in failed!");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());

		//fields validation result
		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(error.getDefaultMessage());
			}
			response.setErrors(errors);
		} else {
			//check
			Users user = userRepo.findByPhone(request.phone()).orElse(null);
			if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
				errors.add("Phone number or password is incorrect!");
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setErrors(errors);
			} else {

				//authorize this session
				UserDetailsImpl userDetails = userDetailService.loadUserByUsername(request.phone());
				Authentication auth = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(request.phone(), request.password())
				);
				SecurityContextHolder.getContext().setAuthentication(auth);

				//return
				AuthResponse responseData = new AuthResponse(
						user.getPhone(),
						JwtProvider.create(userDetails),
						user.getRoles().stream().map(Roles::getName).collect(Collectors.toSet())
				);

				response.setStatusCode(HttpStatus.OK.value());
				response.setData(responseData);
				response.setMessage("Sign in successful!");
			}
		}
		return response;
	}

	/**
	 * Shop owner signup
	 *
	 * @param request       {@link SignUpRequest}
	 * @param bindingResult fields validation result
	 * @return {@link Response} of type {@link AuthResponse}
	 */
	@Override
	@Transactional
	public Response<AuthResponse> signUp(SignUpRequest request, BindingResult bindingResult) {
		Response<AuthResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setMessage("Sign up failed!");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());

		//fields validation result
		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(error.getDefaultMessage());
			}
			response.setErrors(errors);
		} else {
			//check
			boolean passed = true;
			Users checkUsername = userRepo.findByPhone(request.phone()).orElse(null);
			Users checkIdNumber = userRepo.findByIdNumber(request.idNumber()).orElse(null);

			if (checkUsername != null) {
				errors.add("Phone number already in use!");
				passed = false;
			}
			if (checkIdNumber != null) {
				errors.add("ID number already in use!");
				passed = false;
			}
			if (!request.password().equals(request.confirmPassword())) {
				errors.add("Passwords do not match!");
				passed = false;
			}

			//create
			if (passed) {
				Set<Roles> roles = rolesRepo.findDefaultRoles();
				Addresses address = new Addresses(
						request.address().cityOrProvince(),
						request.address().district(),
						request.address().ward(),
						request.address().detailedAddress()
				);
				Users user = new Users(
						request.phone(),
						passwordEncoder.encode(request.password()),
						request.idNumber(),
						request.fullName(),
						address,
						roles
				);

				//persist user
				userRepo.save(user);

				//response
				AuthResponse responseData = new AuthResponse(
						user.getPhone(),
						null,
						roles.stream().map(Roles::getName).collect(Collectors.toSet())
				);

				response.setStatusCode(HttpStatus.CREATED.value());
				response.setData(responseData);
				response.setMessage("Sign up successful!");
			} else {
				response.setErrors(errors);
			}
		}
		return response;
	}

	/**
	 * Change password
	 *
	 * @param request       {@link ChangePasswordRequest}
	 * @param bindingResult fields validation result
	 * @return {@link Response} of type {@link AuthResponse}
	 */
	@Override
	public Response<AuthResponse> changePassword(ChangePasswordRequest request, BindingResult bindingResult) {
		Response<AuthResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setMessage("Change password failed!");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());

		//fields validation result
		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(error.getDefaultMessage());
			}
			response.setErrors(errors);
		} else {

			//validate session
			Users user = getSessionUser(userRepo);
			if (user == null) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
				response.setMessage("Invalid session!");

			} else {
				//check password
				boolean passed = true;
				if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
					errors.add("Old password is incorrect!");
					passed = false;
				}
				if (!request.newPassword().equals(request.confirmPassword())) {
					errors.add("New password do not match!");
					passed = false;
				}

				if (passed) {
					//update user
					user.setPassword(passwordEncoder.encode(request.newPassword()));
					userRepo.save(user);

					AuthResponse responseData = new AuthResponse(
							user.getPhone(),
							null,
							user.getRoles().stream().map(Roles::getName).collect(Collectors.toSet())
					);
					response.setStatusCode(HttpStatus.OK.value());
					response.setData(responseData);
					response.setMessage("Change password successful!");
				} else {
					response.setErrors(errors);
				}

			}
		}

		return response;
	}
}
