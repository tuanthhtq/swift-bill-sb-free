package io.github.tuanthhtq.swiftbillsbfree.configs.implement;

import io.github.tuanthhtq.swiftbillsbfree.entities.Users;
import io.github.tuanthhtq.swiftbillsbfree.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author io.github.tuanthhtq
 */

@Service
public class
UserDetailServiceImpl implements UserDetailsService {

	private final UsersRepository userRepo;

	@Autowired
	public UserDetailServiceImpl(UsersRepository ur) {
		this.userRepo = ur;
	}

	@Override
	public UserDetailsImpl loadUserByUsername(String phone) throws UsernameNotFoundException {
		Users u = userRepo.findByPhone(phone).orElse(null);
		return u == null ? null : new UserDetailsImpl().build(u);
	}

}