package com.tcdt.qlnvhang.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;
import java.util.List;

/**
 * @param <E> Entity
 * @param <D> Dto
 */
public interface AbstractMapper<E, D> {

	E toEntity(D dto);

	List<E> toEntity(Collection<D> dto);

	D toDto(E entity);

	List<D> toDto(Collection<E> entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(@MappingTarget E entity, D dto);
}
