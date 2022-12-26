package com.tcdt.qlnvhang.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

//extends BaseController implements UserDetailsService
@Service
public interface BaseService<E,R, PK extends Serializable>   {

	Page<E> searchPage (R req);

	List<E> searchAll (R req);

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	E create(R req) throws Exception;

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	E update(R req) throws Exception;

	E detail (PK id) throws Exception;

	E approve (R req) throws Exception;

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	void delete(PK id) throws Exception;

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	void deleteMulti(List<PK> listMulti);

	boolean export(R req) throws Exception;

}