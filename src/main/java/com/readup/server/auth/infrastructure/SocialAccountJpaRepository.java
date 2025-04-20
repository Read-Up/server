package com.readup.server.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readup.server.auth.domain.SocialAccount;

@Repository
public interface SocialAccountJpaRepository extends JpaRepository<SocialAccount, Long> {
}
