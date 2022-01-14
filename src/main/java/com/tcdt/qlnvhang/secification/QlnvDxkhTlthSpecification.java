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

import com.tcdt.qlnvhang.request.search.QlnvDxkhTlthSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhTlthHdr;

public class QlnvDxkhTlthSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvDxkhTlthHdr> buildSearchQuery(final QlnvDxkhTlthSearchReq req) {
		return new Specification<QlnvDxkhTlthHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvDxkhTlthHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String maDvi = req.getMaDvi();
					Date tuNgay = req.getTuNgayLap();
					Date denNgay = req.getDenNgayLap();
					String maHhoa = req.getMaHhoa();
					String soDxuat = req.getSoDxuat();
					String lhinhXuat = req.getLhinhXuat();
					root.fetch("children", JoinType.LEFT);
					
					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayDxuat"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayDxuat"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));
					}
					
					if (StringUtils.isNotBlank(soDxuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soDxuat"), soDxuat)));
					}
					if (StringUtils.isNotBlank(lhinhXuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("lhinhXuat"), lhinhXuat)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));
					}
				}
				return predicate;
			}
		};
	}
}
