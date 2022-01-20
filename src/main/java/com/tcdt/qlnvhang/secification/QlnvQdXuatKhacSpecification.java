package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvQdXuatKhacSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdXuatKhacHdr;

public class QlnvQdXuatKhacSpecification {

	@SuppressWarnings("serial")
	public static Specification<QlnvQdXuatKhacHdr> buildSearchQuery(final QlnvQdXuatKhacSearchReq req) {
		return new Specification<QlnvQdXuatKhacHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdXuatKhacHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					Date tuNgay = req.getTuNgayLap();
					Date denNgay = req.getDenNgayLap();
					String soQdinh = req.getSoQdinh();
					String maHhoa = req.getMaHhoa();
					String lhinhXuat = req.getLhinhXuat();
					String trangThai = req.getTrangThai();
					
					root.fetch("children", JoinType.LEFT);

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQdinh"), tuNgay)));
						predicate.getExpressions()
								.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayQdinh"), denNgay)));
					} else if (ObjectUtils.isNotEmpty(tuNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQdinh"), tuNgay)));
					} else if (ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayQdinh"), denNgay)));
					}

					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));
					}
					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinh"), soQdinh)));
					}

					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
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
