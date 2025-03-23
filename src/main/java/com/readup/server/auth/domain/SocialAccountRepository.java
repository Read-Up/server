package com.readup.server.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
}
