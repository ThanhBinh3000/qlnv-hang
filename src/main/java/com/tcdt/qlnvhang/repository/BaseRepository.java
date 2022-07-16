package com.tcdt.qlnvhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<E, PK extends Serializable> extends PagingAndSortingRepository<E, PK>, JpaSpecificationExecutor<E>, JpaRepository<E, PK> {
	List<E> findAll();

	Integer findMaxSo(String maDvi, Integer nam);
}