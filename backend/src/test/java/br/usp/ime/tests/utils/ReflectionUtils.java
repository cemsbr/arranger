package br.usp.ime.tests.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {
	private ReflectionUtils() {
	}

	public static <T> void setField(final Class<T> clazz,
			final String fieldName, final Object newValue)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		final Field field = clazz.getDeclaredField(fieldName);
		try {
			field.set(null, newValue);
		} catch (IllegalAccessException e) {
			ReflectionUtils.setFieldPublic(field);
			field.set(null, newValue);
		}
	}

	public static <T> Field setFieldPublic(final Class<T> clazz,
			final String fieldName) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		final Field field = clazz.getDeclaredField(fieldName);
		ReflectionUtils.setFieldPublic(field);
		return field;
	}

	public static void setFieldPublic(final Field field)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		final Field mods = Field.class.getDeclaredField("modifiers");
		mods.setAccessible(true);

		field.setAccessible(true);
		mods.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	}

	public static <T> Method setMethodPublic(final Class<T> clazz,
			final String methodName, final Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		final Method method = clazz.getDeclaredMethod(methodName,
				parameterTypes);
		method.setAccessible(true);
		return method;
	}
}