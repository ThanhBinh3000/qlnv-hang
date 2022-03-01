package com.tcdt.qlnvhang.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Export UnitScaler common methods
 * 
 * @version 1.0
 * 
 * @author hoanglb
 *
 */
public class UnitScaler {

	public static final Map<String, String> unitConverter = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(Contains.DVT_KG, "1");
			put(Contains.DVT_YEN, "10");
			put(Contains.DVT_TA, "100");
			put(Contains.DVT_TAN, "1000");
		}
	};

	public static <T> void formatRowList(T list, String unitFrom, String unitTo) {
		String rawScaleFrom = unitConverter.get(unitFrom);
		String rawScaleTo = unitConverter.get(unitTo);
		if (rawScaleFrom == null || rawScaleTo == null)
			throw new UnsupportedOperationException("List type unknown");
		BigDecimal unitScaleFrom = new BigDecimal(rawScaleFrom);
		BigDecimal unitScaleTo = new BigDecimal(rawScaleTo);
		Field[] fields = list.getClass().getDeclaredFields();
		for (Field field : fields) {
			String format = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
			try {
				Method getter = list.getClass().getMethod("get" + format, null);
				Class<?> type = getter.getReturnType();
				Method setter = list.getClass().getMethod("set" + format, type);
				Object raw = getter.invoke(list, null);
				if (type.equals(BigDecimal.class) && ((BigDecimal) raw).scale() == 0
						&& format.equals(Contains.COLUMN_CONVERT)) {
					BigDecimal value = ((BigDecimal) raw).multiply(unitScaleFrom).divide(unitScaleTo).setScale(0,
							RoundingMode.HALF_UP);
					setter.invoke(list, value);
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}

	public static <T> void formatList(Collection<T> lists, String unit) {
		for (T list : lists) {
			UnitScaler.formatRowList(list, "kg", unit);
		}
	}

	public static <T> void reverseFormatList(Collection<T> lists, String unit) {
		for (T list : lists) {
			UnitScaler.formatRowList(list, unit, "kg");
		}
	}
}