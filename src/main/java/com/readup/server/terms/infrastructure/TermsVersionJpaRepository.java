package com.readup.server.terms.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.terms.domain.TermsVersion;

public interface TermsVersionJpaRepository extends JpaRepository<TermsVersion, Long> {

	Optional<TermsVersion> findTopByTermsIdOrderByVersionDesc(Long termsId);
}
