package org.orange.oie.internship2025.conferenceroombooking.repository;

import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User getUsersByEmail(String email);

    Optional<User> findByEmail(String email);

}
