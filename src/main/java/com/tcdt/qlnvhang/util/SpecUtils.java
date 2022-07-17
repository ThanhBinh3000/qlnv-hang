package com.tcdt.qlnvhang.util;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;


public final class SpecUtils {

	private SpecUtils() {
	}


	public static <T> Specification<T> build(Class<T> entityClass) {
		return (root, query, builder) -> null;
	}


	public static <T> Specification<T> ge(final String field, final Integer value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.ge(root.get(field), value);
		};
	}

	public static <T> Specification<T> greaterThanOrEqualTo(final String field, final LocalDate value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.greaterThanOrEqualTo(root.get(field), Date.valueOf(value));
		};
	}

	public static <T> Specification<T> lessThanOrEqualTo(final String field, final LocalDate value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.lessThanOrEqualTo(root.get(field), Date.valueOf(value));
		};
	}


	public static <T> Specification<T> le(final String field, final Integer value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.le(root.get(field), value);
		};
	}

	public static <T> Specification<T> equal(final String field, final Integer value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.equal(root.get(field), value);
		};
	}

	public static <T> Specification<T> equal(final String field, final String value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.equal(root.get(field), value);
		};
	}

	public static <T> Specification<T> like(final String field, final String value) {
		return (root, query, builder) -> {
			if (Objects.isNull(value)) return null;

			return builder.like(root.get(field), "%" + value + "%");
		};
	}
}
