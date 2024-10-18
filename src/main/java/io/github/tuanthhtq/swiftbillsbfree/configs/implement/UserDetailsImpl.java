package io.github.tuanthhtq.swiftbillsbfree.configs.implement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.tuanthhtq.swiftbillsbfree.entities.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author io.github.tuanthhtq
 */

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private Long id;
	private String phone;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(
			Long id,
			String phone,
			String password,
			Collection<? extends GrantedAuthority> authorities
	) {
		this.id = id;
		this.phone = phone;
		this.password = password;
		this.authorities = authorities;
	}

	@Transactional
	public UserDetailsImpl build(Users user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(),
				user.getPhone(),
				user.getPassword(),
				authorities
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return phone;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
