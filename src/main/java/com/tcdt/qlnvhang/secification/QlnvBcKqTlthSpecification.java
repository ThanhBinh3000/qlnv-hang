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

import com.tcdt.qlnvhang.request.search.QlnvBcKqTlthSearchReq;
import com.tcdt.qlnvhang.table.QlnvBcKqTlth;

public class QlnvBcKqTlthSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvBcKqTlth> buildSearchQuery(final QlnvBcKqTlthSearchReq req) {
		return new Specification<QlnvBcKqTlth>() {
			@Override
			public Predicate toPredicate(Root<QlnvBcKqTlth> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String maDvi = req.getMaDvi();
					Date tuNgay = req.getTuNgayLapBC();
					Date denNgay = req.getDenNgayLapBC();
					String maHhoa = req.getMaHanghoa();
					String lhinhXuat = req.getLhinhXuat();
					String soQdTlth = req.getSoQdTlth();

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayTao"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayTao"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));
					}
					if (StringUtils.isNotBlank(soQdTlth)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdTlth"), soQdTlth)));
					}
					if (StringUtils.isNotBlank(lhinhXuat)) {
						predicate.getExpressions()
								.add(builder.and(builder.equal(root.get("lhinhXuat"), lhinhXuat)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));
					}
				}
				return predicate;
			}
		};
	}
}
