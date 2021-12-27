package com.tcdt.qlnvhang.secification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaTtThopSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtDtl;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtHdr;
import com.tcdt.qlnvhang.util.Contains;

public class QlnvDxkhMuaTtDtlSpecification {
	public static Specification<QlnvDxkhMuaTtDtl> buildTHopQuery(final QlnvDxkhMuaTtThopSearchReq objReq) {
		return new Specification<QlnvDxkhMuaTtDtl>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2124090610735753749L;

			@Override
			public Predicate toPredicate(Root<QlnvDxkhMuaTtDtl> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate1 = builder.conjunction();
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate1;

				String trangThai = Contains.DUYET;
				String maDvi = objReq.getMaDvi();
				String maHhoa = objReq.getMaHhoa();
				String soQdKhoach = objReq.getSoQdKhoach();
				root.fetch("children", JoinType.LEFT);

				Subquery<QlnvDxkhMuaTtHdr> hdrQuery = query.subquery(QlnvDxkhMuaTtHdr.class);
				Root<QlnvDxkhMuaTtHdr> hdr = hdrQuery.from(QlnvDxkhMuaTtHdr.class);

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("maDvi"), maDvi)));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(soQdKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("soQdKhoach"), soQdKhoach)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("maHhoa"), maHhoa)));

				predicate1.getExpressions().add(root.get("parent").in(hdrQuery.select(hdr).where(predicate)));

				return predicate1;
			}
		};
	}
}
