package io.github.tuanthhtq.swiftbillsbfree.entities;

import io.github.tuanthhtq.swiftbillsbfree.constants.Length;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

@Getter
@Setter
@Entity(name = "roles")
@NoArgsConstructor
public class Roles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;

	@Column(name = "role_name", nullable = false, length = Length.NAME)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "rel_user_roles",
			inverseJoinColumns = @JoinColumn(name = "user_id"),
			joinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Users> users;

	public Roles(String name) {
		this.name = name;
	}

}
