package com.tcdt.qlnvhang.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu.GenericRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class DataUtils {
	@Autowired
	private ObjectMapper objectMapper;

	public <T> T toObject(Object source, Class<T> clazz) {
		if (source == null) return null;
		return objectMapper.convertValue(source, clazz);
	}

	public <T> List<T> toListObject(Object obj, Class<T> clazz) throws Exception {
		if (obj instanceof Collection) {
			Collection<Object> list = (Collection<Object>) obj;
			if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
			return list.stream().map(item -> this.toObject(item, clazz)).collect(Collectors.toList());
		}
		throw new Exception("Can not convert to the list object");
	}

	public <T> void validateExits(GenericRepository<T, Long> repository, Long id, boolean isCreate) throws Exception {
		if (id == null || repository == null) throw new Exception("Id or repository must not be null");

		Optional<T> entity = repository.findById(id);

		if (isCreate && entity.isPresent()) {
			log.error("Entity {} existed", repository.getClass().getSimpleName());
			throw new Exception("Entity existed");
		} else if (!entity.isPresent()) {
			log.error("Entity {} not found", repository.getClass().getSimpleName());
			throw new Exception("Entity not found");
		}
	}

	public <T> List<T> findAllByIds(GenericRepository<T, Long> repository, Set<Long> ids) throws Exception {
		if (CollectionUtils.isEmpty(ids) || repository == null) {
			throw new Exception("Id or repository must not be null");
		}
		List<T> entities = repository.findByIdIn(ids);

		if (CollectionUtils.isEmpty(entities)) {
			throw new Exception("Entity not found");
		}

		return entities;
	}

	public static String toStringValue (Object obj) {
		return Optional.ofNullable(obj).map(Object::toString).orElse("");
	}

}
