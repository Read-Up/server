package com.readup.server.terms.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.terms.domain.UserTermsConsent;

public interface UserTermsConsentJpaRepository extends JpaRepository<UserTermsConsent, Long> {
}
