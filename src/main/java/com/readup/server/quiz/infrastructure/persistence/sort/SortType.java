package com.readup.server.quiz.infrastructure.persistence.sort;

import java.util.Arrays;

import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.readup.server.common.exception.DomainException;

public interface SortType {

    String getFieldName();
    ComparableExpressionBase<?> getExpression();
    
    static <T extends Enum<T> & SortType> ComparableExpressionBase<?>
        getExpressionByFieldName(Class<T> enumClass, String fieldName, DomainException exception) {
        return Arrays.stream(enumClass.getEnumConstants())
            .filter(v -> v.getFieldName().equalsIgnoreCase(fieldName))
            .map(SortType::getExpression)
            .findFirst()
            .orElseThrow(() -> exception);
    }
}
