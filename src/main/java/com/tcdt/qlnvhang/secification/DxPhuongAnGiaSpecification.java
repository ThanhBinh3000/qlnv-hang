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

import com.tcdt.qlnvhang.request.search.QlnvDxPhuongAnGiaSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxPhuongAnGiaHdr;

public class DxPhuongAnGiaSpecification {
	public static Specification<QlnvDxPhuongAnGiaHdr> buildSearchQuery(final QlnvDxPhuongAnGiaSearchReq objReq) {
		return new Specification<QlnvDxPhuongAnGiaHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvDxPhuongAnGiaHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soDxuat = objReq.getSoDxuat();
				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String maDvi = objReq.getMaDvi();
				String trangThai = objReq.getTrangThai();
				String namKhoach = objReq.getNamKhoach();

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

				if (StringUtils.isNotBlank(namKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

				return predicate;
			}
		};
	}
}
