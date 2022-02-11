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
import com.tcdt.qlnvhang.table.QlnvPhieuNXuatBoNganhHdr;

public class PhieuNXuatBoNganhSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvPhieuNXuatBoNganhHdr> buildSearchQuery(final QlnvPhieuNhapxuatSearchReq req) {
		return new Specification<QlnvPhieuNXuatBoNganhHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvPhieuNXuatBoNganhHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maDvi = req.getMaDvi();
					Date tuNgay = req.getTuNgayLap();
					Date denNgay = req.getDenNgayLap();
					String maHhoa = req.getMaHhoa();
					String soQdinh = req.getSoQdinhNhapxuat();
					String lhinhNhapxuat = req.getLhinhNhapxuat();
					String soPhieu = req.getSoPhieu();

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayLap"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));
					}
					if (StringUtils.isNotBlank(soPhieu)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soPhieu"), soPhieu)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinhNhapxuat"), soQdinh)));
					}
					if (StringUtils.isNotBlank(lhinhNhapxuat)) {
						predicate.getExpressions()
								.add(builder.and(builder.equal(root.get("lhinhNhapxuat"), lhinhNhapxuat)));
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
