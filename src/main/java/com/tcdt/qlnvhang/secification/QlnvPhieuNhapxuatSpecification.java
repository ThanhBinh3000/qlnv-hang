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

import com.tcdt.qlnvhang.request.search.QlnvPhieuNhapxuatSearchReq;
import com.tcdt.qlnvhang.table.QlnvPhieuNhapxuatHdr;

public class QlnvPhieuNhapxuatSpecification {
	public static Specification<QlnvPhieuNhapxuatHdr> buildSearchQuery(final QlnvPhieuNhapxuatSearchReq objReq) {
		return new Specification<QlnvPhieuNhapxuatHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvPhieuNhapxuatHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soPhieu = objReq.getSoPhieu();
				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String maHhoa = objReq.getMaHhoa();
				String trangThai = objReq.getTrangThai();
				String maDvi = objReq.getMaDvi();
				String lhinhNhapxuat = objReq.getLhinhNhapxuat();
				String soQdinhNhapxuat = objReq.getSoQdinhNhapxuat();

				if (StringUtils.isNotEmpty(soPhieu))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soPhieu")), "%" + soPhieu.toLowerCase() + "%"));

				if (StringUtils.isNotEmpty(soQdinhNhapxuat))
					predicate.getExpressions().add(builder.like(builder.lower(root.get("soQdinhNhapxuat")),
							"%" + soQdinhNhapxuat.toLowerCase() + "%"));

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

				if (StringUtils.isNotBlank(lhinhNhapxuat))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("lhinhNhapxuat"), lhinhNhapxuat)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				return predicate;
			}
		};
	}
}
