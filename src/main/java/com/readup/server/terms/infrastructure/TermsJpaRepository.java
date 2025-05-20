package com.readup.server.terms.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsCode;

public interface TermsJpaRepository extends JpaRepository<Terms, Long> {
	Optional<Terms> findByCode(TermsCode code);
}
