package io.github.tuanthhtq.swiftbillsbfree.repositories;

import io.github.tuanthhtq.swiftbillsbfree.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author io.github.tuanthhtq
 */

public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByPhone(String phone);

	Optional<Users> findByIdNumber(String idNumber);

	Optional<Users> findByPhoneAndPassword(String phone, String password);


}
