package com.readup.server.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
