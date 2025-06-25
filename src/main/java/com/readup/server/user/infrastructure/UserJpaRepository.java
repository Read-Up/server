package com.readup.server.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findBySocialAccountId(Long socialAccountId);
}
