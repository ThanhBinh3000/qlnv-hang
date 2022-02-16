package com.tcdt.qlnvhang.secification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvTtinDthauHhoaHdrSearchReq;
import com.tcdt.qlnvhang.table.QlnvTtinDthauHhoaHdr;

public class QlnvTtinDthauHhoaSpecification {

	@SuppressWarnings("serial")
	public static Specification<QlnvTtinDthauHhoaHdr> buildSearchQuery(final QlnvTtinDthauHhoaHdrSearchReq req) {
		return new Specification<QlnvTtinDthauHhoaHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvTtinDthauHhoaHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String soQdKh = req.getSoQdKh();
					String maHhoa = req.getMaHhoa();
					String tenHhoa = req.getTenHhoa();
					
					root.fetch("detailList", JoinType.LEFT);

					if (StringUtils.isNotBlank(soQdKh)) {
						predicate.getExpressions().add(
								builder.like(builder.lower(root.get("soQdKh")), "%" + soQdKh.toLowerCase() + "%"));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));
					}
					if (StringUtils.isNotBlank(tenHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("tenHhoa"), tenHhoa)));
					}
				}
				return predicate;
			}
		};
	}
}
