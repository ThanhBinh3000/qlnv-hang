package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class KtNganLoRepositoryCustomImpl implements KtNganLoRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<KtNganLo> selectParams(String ma, String ten, Long id, Pageable pageable) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT kt FROM KtNganLo kt ");
		setCondition(ma, ten, id, builder);

		TypedQuery<KtNganLo> query = em.createQuery(builder.toString(), KtNganLo.class);
		this.setParameter(ma, ten, id, query);

		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
		List<KtNganLo> data = query.getResultList();
		return new PageImpl<>(data, pageable, this.count(ma, ten, id));
	}

	private void setCondition(String ma, String ten, Long id, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(ma)) {
			builder.append("AND ").append("lower(kt.maNganlo) LIKE :ma ");
		}

		if (!StringUtils.isEmpty(ten)) {
			builder.append("AND ").append("lower(kt.tenNganlo) LIKE :ten ");
		}

		if (id != null) {
			builder.append("AND ").append("kt.parent.id = :nganKhoId ");
		}
	}

	private int count(String ma, String ten, Long id) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(kt.id) FROM KtNganLo kt ");
		this.setCondition(ma, ten, id, builder);
		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
		this.setParameter(ma, ten, id, query);
		return query.getSingleResult().intValue();
	}

	private void setParameter(String ma, String ten, Long id, Query query) {
		if (!StringUtils.isEmpty(ma)) {
			query.setParameter("ma", "%" + ma + "%");
		}

		if (!StringUtils.isEmpty(ten)) {
			query.setParameter("ten", "%" + ten + "%");
		}

		if (id != null) {
			query.setParameter("nganKhoId", id);
		}
	}
}
