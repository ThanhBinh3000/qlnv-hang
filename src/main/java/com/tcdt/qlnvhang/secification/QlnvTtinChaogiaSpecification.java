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

import com.tcdt.qlnvhang.request.search.QlnvTtinChaogiaSearchReq;
import com.tcdt.qlnvhang.table.QlnvTtinChaogiaHdr;

public class QlnvTtinChaogiaSpecification {
	public static Specification<QlnvTtinChaogiaHdr> buildSearchQuery(final QlnvTtinChaogiaSearchReq objReq) {
		return new Specification<QlnvTtinChaogiaHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvTtinChaogiaHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdKhoach = objReq.getSoQdKhoach();
				Date tuNgayCgia = objReq.getTuNgayCgia();
				Date denNgayCgia = objReq.getDenNgayCgia();
				String trangThai = objReq.getTrangThai();
				String maHhoa = objReq.getMaHhoa();
				String loaiMuaban = objReq.getLoaiMuaban();
				String maDvi = objReq.getMaDvi();

				if (StringUtils.isNotEmpty(soQdKhoach))
					predicate.getExpressions().add(
							builder.like(builder.lower(root.get("soQdKhoach")), "%" + soQdKhoach.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayCgia) && ObjectUtils.isNotEmpty(denNgayCgia)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayThien"), tuNgayCgia)));
					predicate.getExpressions().add(builder.and(
							builder.lessThan(root.get("ngayThien"), new DateTime(denNgayCgia).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				if (StringUtils.isNotBlank(loaiMuaban))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiMuaban"), loaiMuaban)));

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
}
