package com.tcdt.qlnvhang.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataUtils {
	@Autowired
	private ObjectMapper objectMapper;

	public <T> T mapObject(Object source, Class<T> toClass) {
		if (source == null) return null;
		return objectMapper.convertValue(source, toClass);
	}
}
