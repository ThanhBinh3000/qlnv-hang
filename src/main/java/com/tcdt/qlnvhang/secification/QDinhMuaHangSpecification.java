package com.tcdt.qlnvhang.secification;

import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvQdMuaHangSearchAdjustReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuaHangSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr;

public class QDinhMuaHangSpecification {	

	@SuppressWarnings("serial")
	public static Specification<QlnvQdMuaHangHdr> buildSearchQuery(final QlnvQdMuaHangSearchReq req) {
		return new Specification<QlnvQdMuaHangHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdMuaHangHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					Date tuNgay = req.getTuNgayQdinh();
					Date denNgay = req.getDenNgayQdinh();
					String maHhoa = req.getMaHhoa();
					String soQdinh = req.getSoQdinh();
					String soQDKhoach = req.getSoQdKh();
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
					
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinh"), soQdinh)));
					}
					if (StringUtils.isNotBlank(soQDKhoach)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKh"), soQDKhoach)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));
					}
				}
				return predicate;
			}
		};
	}
	@SuppressWarnings("serial")
	public static Specification<QlnvQdMuaHangHdr> buildSearchQueryAjust(final QlnvQdMuaHangSearchAdjustReq req) {
		return new Specification<QlnvQdMuaHangHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdMuaHangHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					Date tuNgay = req.getTuNgayQdinh();
					Date denNgay = req.getDenNgayQdinh();
					String soQdinhGoc = req.getSoQdinhGoc();
					String soQdinh = req.getSoQdinh();
					String soQDKhoach = req.getSoQdKh();
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
					
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinh"), soQdinh)));
					}
					if (StringUtils.isNotBlank(soQDKhoach)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKh"), soQDKhoach)));
					}
					if (StringUtils.isNotBlank(soQdinhGoc)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinhGoc"), soQdinhGoc)));
					}
				}
				return predicate;
			}
		};
	}
}
