package com.tcdt.qlnvhang.contraints;

import java.lang.reflect.Field;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompareDateValidator implements ConstraintValidator<CompareDate, Object> {

	private String beforeFieldName;
	private String afterFieldName;

	@Override
	public void initialize(final CompareDate constraintAnnotation) {
		beforeFieldName = constraintAnnotation.before();
		afterFieldName = constraintAnnotation.after();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		try {
			final Field beforeDateField = value.getClass().getDeclaredField(beforeFieldName);
			beforeDateField.setAccessible(true);

			final Field afterDateField = value.getClass().getDeclaredField(afterFieldName);
			afterDateField.setAccessible(true);

			final Date beforeDate = (Date) beforeDateField.get(value);
			final Date afterDate = (Date) afterDateField.get(value);
			System.out.println(beforeDate.compareTo(afterDate) >= 0);
			return beforeDate == null && afterDate == null || beforeDate != null && afterDate.compareTo(beforeDate) >= 0;
		} catch (final Exception e) {
			log.error(e.getMessage());

			return false;
		}
	}
}
