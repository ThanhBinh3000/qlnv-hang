package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvKhoachLcntSearchReq;
import com.tcdt.qlnvhang.table.QlnvKhoachLcntHdr;

public class QlnvKhoachLcntSpecification {

	@SuppressWarnings("serial")
	public static Specification<QlnvKhoachLcntHdr> buildSearchQuery(final QlnvKhoachLcntSearchReq req) {
		return new Specification<QlnvKhoachLcntHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvKhoachLcntHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maDvi = req.getMaDvi();
					String loaiHanghoa = req.getLoaiHanghoa();
					String hanghoa = req.getHanghoa();
					String soDx = req.getSoDx();
					Date tuNgayLap = req.getTuNgayLap();
					Date denNgayLap = req.getDenNgayLap();
					
					root.fetch("detailList", JoinType.LEFT);
					
					if (ObjectUtils.isNotEmpty(tuNgayLap) && ObjectUtils.isNotEmpty(denNgayLap)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayDx"), tuNgayLap)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayDx"), new DateTime(denNgayLap).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soDx)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soDx"), soDx)));
					}
					if (StringUtils.isNotBlank(loaiHanghoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHanghoa"), loaiHanghoa)));
					}
					if (StringUtils.isNotBlank(hanghoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("hanghoa"), hanghoa)));
					}
				}
				return predicate;
			}
		};
	}
}
