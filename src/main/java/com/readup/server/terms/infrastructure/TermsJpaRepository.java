package com.readup.server.terms.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.terms.domain.Terms;

public interface TermsJpaRepository extends JpaRepository<Terms, Long> {
}
