package com.tcdt.qlnvhang.util.query;

import com.tcdt.qlnvhang.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.Query;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryUtils {
	private Class clazz;
	private String alias;

	public static final String COUNT_ALL = " COUNT(*) ";
	public static final String SELECT = " SELECT ";
	public static final String FROM = " FROM ";
	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String COMMA = ", ";
	public static final String AND = " AND ";
	public static final String DEFAULT_CONDITION = " 1=1 ";
	public static final String WHERE = " WHERE ";

	public String getClassName() {
		return this.clazz.getSimpleName() + SPACE;
	}

	public String getField(String field) {
		return String.format(" %s.%s", this.alias, field);
	}

	public String selectField(String field) {
		return String.format(" , %s.%s", this.alias, field);
	}

	public String buildAliasName() {
		return String.format("%s %s", this.getClassName(), this.alias);
	}

	public static String buildLeftJoin(QueryUtils left, QueryUtils right, String leftField, String rightField) {
		return String.format(" LEFT JOIN %s ON %s = %s ", right.buildAliasName(), left.getField(leftField), right.getField(rightField));
	}

	public static String buildInnerJoin(QueryUtils left, QueryUtils right, String leftField, String rightField) {
		return String.format(" INNER JOIN %s ON %s = %s ", right.buildAliasName(), left.getField(leftField), right.getField(rightField));
	}

	public void like(Operator operator, String field, Object req, StringBuilder builder) {
		if (Objects.nonNull(req)) {
			builder.append(String.format(" %s %s LIKE :%s ", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field));
		}
	}

	public void eq(Operator operator, String field, Object req, StringBuilder builder) {
		if (Objects.nonNull(req)) {
			builder.append(String.format(" %s %s = :%s ", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field));
		}
	}

	public void ge(Operator operator, String field, Object req, StringBuilder builder) {
		if (Objects.nonNull(req)) {
			builder.append(String.format(" %s %s >= :%s ", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field));
		}
	}

	public void le(Operator operator, String field, Object req, StringBuilder builder) {
		if (Objects.nonNull(req)) {
			builder.append(String.format(" %s %s <= :%s ", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field));
		}
	}

	public void lt(Operator operator, String field, Object req, StringBuilder builder) {
		if (Objects.nonNull(req)) {
			builder.append(String.format(" %s %s < :%s ", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field));
		}
	}

	public void gt(Operator operator, String field, Object req, StringBuilder builder) {
		if (Objects.nonNull(req)) {
			builder.append(String.format(" %s %s > :%s ", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field));
		}
	}

	public String inLst(Operator operator, String field, Object req) {
		if (Objects.nonNull(req)) {
			return String.format(" %s %s IN :%s", Optional.ofNullable(operator).map(Enum::toString).orElse(""), this.getField(field), field);
		}
		return "";
	}

	public static String buildLikeParam(String filed) {
		return "%" + filed + "%";
	}

	public static void buildSort(Pageable pageable, StringBuilder builder) {
		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			builder.append("ORDER BY ").append(sort.get()
					.map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
		}

	}

	public StringBuilder countBy(String filed) {
		StringBuilder builder = new StringBuilder();
		return builder.append(String.format("SELECT COUNT(DISTINCT %s) FROM %s ", this.getField(filed), this.buildAliasName()));
	}

	public static void setParam(Query query, String paramName, Object value) {
		if (StringUtils.isEmpty(paramName)) return;

		if (Objects.nonNull(value)) {
			query.setParameter(paramName, value);
		}
	}

	public static void setLikeParam(Query query, String paramName, String value) {
		if (StringUtils.isEmpty(paramName)) return;

		if (Objects.nonNull(value)) {
			query.setParameter(paramName, buildLikeParam(value));
		}
	}

	public static void buildWhereClause(StringBuilder builder) {
		builder.append(QueryUtils.WHERE).append(QueryUtils.DEFAULT_CONDITION);
	}

	public static String buildQuery(StringBuilder builder) {
		return builder.toString().replace("SELECT  ,", SELECT);
	}
	public static void selectFields(StringBuilder builder, QueryUtils qU, String field) {
		builder.append(qU.selectField(field));
	}

}
