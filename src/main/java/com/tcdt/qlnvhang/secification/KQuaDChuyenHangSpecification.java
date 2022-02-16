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

import com.tcdt.qlnvhang.request.search.QlnvKqDChuyenHangSearchReq;
import com.tcdt.qlnvhang.table.QlnvKqDChuyenHang;

public class KQuaDChuyenHangSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvKqDChuyenHang> buildSearchQuery(final QlnvKqDChuyenHangSearchReq req) {
		return new Specification<QlnvKqDChuyenHang>() {
			@Override
			public Predicate toPredicate(Root<QlnvKqDChuyenHang> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maDviDi = req.getMaDviDi();
					Date tuNgay = req.getTuNgayBcao();
					Date denNgay = req.getDenNgayBcao();
					String maHhoa = req.getMaHhoa();
					String soQdinh = req.getSoQdinh();
					String hthucDChuyen = req.getHthucDchuyen();
					String maDviDen = req.getMaDviDen();

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayBcao"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayBcao"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDviDi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDviDi"), maDviDi)));
					}
					if (StringUtils.isNotBlank(maDviDen)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDviDen"), maDviDen)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinh"), soQdinh)));
					}
					if (StringUtils.isNotBlank(hthucDChuyen)) {
						predicate.getExpressions()
								.add(builder.and(builder.equal(root.get("hthucDchuyen"), hthucDChuyen)));
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
