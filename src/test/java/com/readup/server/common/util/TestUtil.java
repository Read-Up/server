package com.readup.server.common.util;

import java.lang.reflect.Field;

public class TestUtil {
    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Reflection field 주입 실패: " + fieldName, e);
        }
    }
}
