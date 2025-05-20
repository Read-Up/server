package com.readup.server.terms.application;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsCode;
import com.readup.server.terms.infrastructure.TermsJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsService {

	private final TermsJpaRepository termsJpaRepository;

	@Transactional(readOnly = true)
	public List<Terms> findAll() {
		List<Terms> termsList = termsJpaRepository.findAll();

		if (termsList.isEmpty()) {
			throw new ServiceException(TERMS_NOT_FOUND);
		}

		return termsList;
	}

	@Transactional(readOnly = true)
	public Terms findByCode(TermsCode code) {
		return termsJpaRepository.findByCode(code).orElseThrow(() -> new ServiceException(TERMS_NOT_FOUND));
	}
}
