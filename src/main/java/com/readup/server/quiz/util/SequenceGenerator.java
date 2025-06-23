package com.readup.server.quiz.util;

import java.util.ArrayList;
import java.util.List;

public final class SequenceGenerator {

	private SequenceGenerator() {
	}

	public static <T, R> List<R> createResponsesWithSequence(List<T> sourceList, SequenceMapper<T, R> mapper) {
		List<R> response = new ArrayList<>(sourceList.size());

		for (int i = 0; i < sourceList.size(); i++) {
			T source = sourceList.get(i);
			int sequence = i + 1;
			response.add(mapper.map(source, sequence));
		}

		return response;
	}

	@FunctionalInterface
	public interface SequenceMapper<T, R> {
		R map(T source, int sequence);
	}
}
