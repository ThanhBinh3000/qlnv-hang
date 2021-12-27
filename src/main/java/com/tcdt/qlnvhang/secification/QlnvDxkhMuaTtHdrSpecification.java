package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaTtSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtHdr;

public class QlnvDxkhMuaTtHdrSpecification {
	public static Specification<QlnvDxkhMuaTtHdr> buildSearchQuery(final QlnvDxkhMuaTtSearchReq objReq) {
		return new Specification<QlnvDxkhMuaTtHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvDxkhMuaTtHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soDxuat = objReq.getSoDxuat();
				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String maDvi = objReq.getMaDvi();
				String trangThai = objReq.getTrangThai();
				String maHhoa = objReq.getMaHhoa();
				String soQdKhoach = objReq.getSoQdKhoach();

				if (StringUtils.isNotEmpty(soDxuat))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soDxuat")), "%" + soDxuat.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayLap) && ObjectUtils.isNotEmpty(denNgayLap)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgayLap)));
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayLap"), new DateTime(denNgayLap).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				if (StringUtils.isNotBlank(soQdKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKhoach"), soQdKhoach)));

				return predicate;
			}
		};
	}
}
