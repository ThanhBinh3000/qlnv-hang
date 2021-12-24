package com.tcdt.qlnvhang.secification;

import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaHangSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaHangHdr;

public class DXuatKHMuaHangSpecification {	

	@SuppressWarnings("serial")
	public static Specification<QlnvDxkhMuaHangHdr> buildSearchQuery(final QlnvDxkhMuaHangSearchReq req) {
		return new Specification<QlnvDxkhMuaHangHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvDxkhMuaHangHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maDvi = req.getMaDvi();
					Date tuNgay = req.getTuNgayLap();
					Date denNgay = req.getDenNgayLap();
					String maHhoa = req.getMaHhoa();
					String soDxuat = req.getSoDxuat();
					String soQDKhoach = req.getSoQdKhoach();
					root.fetch("children", JoinType.LEFT);
					if (denNgay != null) {
						if (tuNgay != null) {
							predicate.getExpressions()
									.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayLap"), tuNgay)));
							predicate.getExpressions()
									.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), denNgay)));
						} else {
							predicate.getExpressions()
									.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), denNgay)));
						}
					} else {
						if (tuNgay != null) {
							predicate.getExpressions()
									.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayLap"), tuNgay)));
						}
					}

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soDxuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soDxuat"), soDxuat)));
					}
					if (StringUtils.isNotBlank(soQDKhoach)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKhoach"), soQDKhoach)));
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
