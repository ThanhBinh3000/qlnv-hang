package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class KtNganKhoRepositoryCustomImpl implements KtNganKhoRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<KtNganKho> selectParams(String ma, String ten, Long id, Pageable pageable) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT kt FROM KtNganKho kt ");
		setCondition(ma, ten, id, builder);

		TypedQuery<KtNganKho> query = em.createQuery(builder.toString(), KtNganKho.class);
		this.setParameter(ma, ten, id, query);

		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
		List<KtNganKho> data = query.getResultList();
		return new PageImpl<>(data, pageable, this.count(ma, ten, id));
	}

	private void setCondition(String ma, String ten, Long id, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(ma)) {
			builder.append("AND ").append("lower(kt.maNgankho) LIKE :ma ");
		}

		if (!StringUtils.isEmpty(ten)) {
			builder.append("AND ").append("lower(kt.tenNgankho) LIKE :ten ");
		}

		if (id != null) {
			builder.append("AND ").append("kt.parent.id = :nhaKhoId ");
		}
	}

	private int count(String ma, String ten, Long id) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(kt.id) FROM KtNganKho kt ");
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
			query.setParameter("nhaKhoId", id);
		}
	}
}
