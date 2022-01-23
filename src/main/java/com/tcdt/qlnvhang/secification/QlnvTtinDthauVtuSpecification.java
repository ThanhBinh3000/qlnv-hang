package com.tcdt.qlnvhang.secification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvTtinDthauVtuHdrSearchReq;
import com.tcdt.qlnvhang.table.QlnvTtinDthauVtuHdr;

public class QlnvTtinDthauVtuSpecification {

	@SuppressWarnings("serial")
	public static Specification<QlnvTtinDthauVtuHdr> buildSearchQuery(final QlnvTtinDthauVtuHdrSearchReq req) {
		return new Specification<QlnvTtinDthauVtuHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvTtinDthauVtuHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String soQdKh = req.getSoQdKh();
					String maVtu = req.getMaVtu();
					String tenVtu = req.getTenVtu();
					
					root.fetch("detailList", JoinType.LEFT);

					if (StringUtils.isNotBlank(soQdKh)) {
						predicate.getExpressions().add(
								builder.like(builder.lower(root.get("soQdKh")), "%" + soQdKh.toLowerCase() + "%"));
					}
					if (StringUtils.isNotBlank(maVtu)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maVtu"), maVtu)));
					}
					if (StringUtils.isNotBlank(tenVtu)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("tenVtu"), tenVtu)));
					}
				}
				return predicate;
			}
		};
	}
}
