package io.github.tuanthhtq.swiftbillsbfree.services.impl;

import io.github.tuanthhtq.swiftbillsbfree.configs.implement.UserDetailsImpl;
import io.github.tuanthhtq.swiftbillsbfree.entities.Stores;
import io.github.tuanthhtq.swiftbillsbfree.entities.Users;
import io.github.tuanthhtq.swiftbillsbfree.repositories.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author io.github.tuanthhtq
 */

abstract class CommonConcrete {

	/**
	 * Get this session user
	 *
	 * @param userRepo {@link UsersRepository}
	 * @return {@link Users}
	 */
	public Users getSessionUser(UsersRepository userRepo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		if (userDetails == null) {
			return null;
		} else {
			return userRepo.findByPhone(userDetails.getPhone()).orElse(null);
		}
	}

	/**
	 * Get current using store
	 *
	 * @param userRepo {@link UsersRepository}
	 * @param storeId  store id
	 * @return {@link Stores}
	 */
	public Stores getSessionStore(UsersRepository userRepo, Long storeId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		if (userDetails == null) {
			return null;
		} else {
			Users user = userRepo.findByPhone(userDetails.getPhone()).orElse(null);
			if (user == null) {
				return null;
			} else {
				return user.getStores().stream().filter(s -> s.getId().equals(storeId)).findFirst().orElse(null);
			}
		}

	}
}
