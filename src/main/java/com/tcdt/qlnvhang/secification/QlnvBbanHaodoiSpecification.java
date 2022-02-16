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

import com.tcdt.qlnvhang.request.search.QlnvBbanHaodoiSearchReq;
import com.tcdt.qlnvhang.table.QlnvBbanHaodoiHdr;

public class QlnvBbanHaodoiSpecification {
	public static Specification<QlnvBbanHaodoiHdr> buildSearchQuery(final QlnvBbanHaodoiSearchReq objReq) {
		return new Specification<QlnvBbanHaodoiHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4065919270139315550L;

			@Override
			public Predicate toPredicate(Root<QlnvBbanHaodoiHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soBban = objReq.getSoBban();
				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String maHhoa = objReq.getMaHhoa();
				String maDvi = objReq.getMaDvi();
				String soBbanTinhkho = objReq.getSoBbanTinhkho();

				if (StringUtils.isNotEmpty(soBban))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soBban")), "%" + soBban.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayLap) && ObjectUtils.isNotEmpty(denNgayLap)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgayLap)));
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayLap"), new DateTime(denNgayLap).plusDays(1).toDate())));
				} else if (ObjectUtils.isNotEmpty(tuNgayLap)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgayLap)));
				} else if (ObjectUtils.isNotEmpty(denNgayLap)) {
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayLap"), denNgayLap)));
				}

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotEmpty(soBbanTinhkho))
					predicate.getExpressions().add(builder.like(builder.lower(root.get("soBbanTinhkho")),
							"%" + soBbanTinhkho.toLowerCase() + "%"));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				return predicate;
			}
		};
	}
}
