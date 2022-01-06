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

import com.tcdt.qlnvhang.request.search.QlnvBkeCanhangSearchReq;
import com.tcdt.qlnvhang.table.QlnvBkeCanhangHdr;

public class QlnvBkeCanhangSpecification {
	public static Specification<QlnvBkeCanhangHdr> buildSearchQuery(final QlnvBkeCanhangSearchReq objReq) {
		return new Specification<QlnvBkeCanhangHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvBkeCanhangHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soBke = objReq.getSoBke();
				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String maHhoa = objReq.getMaHhoa();
				String trangThai = objReq.getTrangThai();
				String maDvi = objReq.getMaDvi();
				String loaiBke = objReq.getLoaiBke();
				String soHdong = objReq.getSoHdong();

				if (StringUtils.isNotEmpty(soBke))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soBke")), "%" + soBke.toLowerCase() + "%"));

				if (StringUtils.isNotEmpty(soHdong))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soHdong")), "%" + soHdong.toLowerCase() + "%"));

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

				if (StringUtils.isNotBlank(loaiBke))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiBke"), loaiBke)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				return predicate;
			}
		};
	}
}
