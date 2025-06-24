package com.readup.server.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.user.domain.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findBySocialAccountId(Long socialAccountId);
}
