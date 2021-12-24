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

import com.tcdt.qlnvhang.request.search.QlnvTtinHdongHangSearchReq;
import com.tcdt.qlnvhang.table.QlnvTtinHdongHangHdr;

public class QlnvTtinHdongHangSpecification {
	public static Specification<QlnvTtinHdongHangHdr> buildSearchQuery(final QlnvTtinHdongHangSearchReq objReq) {
		return new Specification<QlnvTtinHdongHangHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvTtinHdongHangHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soHdong = objReq.getSoHdong();
				Date tuNgayHdong = objReq.getTuNgayHdong();
				Date denNgayHdong = objReq.getDenNgayHdong();
				String maHhoa = objReq.getMaHhoa();
				String tthaiHdong = objReq.getTthaiHdong();
				String maDvi = objReq.getMaDvi();
				String loaiHdong = objReq.getLoaiHdong();

				if (StringUtils.isNotEmpty(soHdong))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soHdong")), "%" + soHdong.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayHdong) && ObjectUtils.isNotEmpty(denNgayHdong)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayHluc"), tuNgayHdong)));
					predicate.getExpressions().add(builder.and(
							builder.lessThan(root.get("ngayHluc"), new DateTime(denNgayHdong).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotBlank(tthaiHdong))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("tthaiHdong"), tthaiHdong)));

				if (StringUtils.isNotBlank(loaiHdong))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHdong"), loaiHdong)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));

				return predicate;
			}
		};
	}
}
