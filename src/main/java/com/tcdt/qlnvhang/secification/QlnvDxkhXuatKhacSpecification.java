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
import com.tcdt.qlnvhang.request.search.QlnvDxkhXuatKhacSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaHangHdr;
import com.tcdt.qlnvhang.table.QlnvDxkhXuatKhacHdr;

public class QlnvDxkhXuatKhacSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvDxkhXuatKhacHdr> buildSearchQuery(final QlnvDxkhXuatKhacSearchReq req) {
		return new Specification<QlnvDxkhXuatKhacHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvDxkhXuatKhacHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					Date tuNgay = req.getTuNgayLap();
					Date denNgay = req.getDenNgayLap();
					String soDxuat = req.getSoDxuat();
					String lhangDtQgia = req.getLhangDtQgia();
					String lhinhXuat = req.getLhinhXuat();
					String dviDtQgia = req.getDviDtQgia();
					root.fetch("children", JoinType.LEFT);
					if (denNgay != null) {
						if (tuNgay != null) {
							predicate.getExpressions()
									.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayDxuat"), tuNgay)));
							predicate.getExpressions()
									.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayDxuat"), denNgay)));
						} else {
							predicate.getExpressions()
									.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayDxuat"), denNgay)));
						}
					} else {
						if (tuNgay != null) {
							predicate.getExpressions()
									.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayDxuat"), tuNgay)));
						}
					}

					if (StringUtils.isNotBlank(dviDtQgia)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), dviDtQgia)));
					}
					if (StringUtils.isNotBlank(soDxuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soDxuat"), soDxuat)));
					}
					
					if (StringUtils.isNotBlank(lhangDtQgia)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), lhangDtQgia)));
					}
					
					if (StringUtils.isNotBlank(lhinhXuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("lhinhXuat"), lhinhXuat)));
					}
				}
				return predicate;
			}
		};
	}
}
