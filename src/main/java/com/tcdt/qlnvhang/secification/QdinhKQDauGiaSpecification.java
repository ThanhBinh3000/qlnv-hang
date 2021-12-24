package com.tcdt.qlnvhang.secification;

import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvQdKQDGHangSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdKQDGThopSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdKQDGHangHdr;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangDtl;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangHdr;
import com.tcdt.qlnvhang.util.Contains;

public class QdinhKQDauGiaSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvTtinDauGiaHangDtl> buildTHopQuery(final QlnvQdKQDGThopSearchReq req) {
		return new Specification<QlnvTtinDauGiaHangDtl>() {
			@Override
			public Predicate toPredicate(Root<QlnvTtinDauGiaHangDtl> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate1 = builder.conjunction();
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = Contains.DUYET;
					String maDvi = req.getMaDvi();
					String maHhoa = req.getMaHhoa();
					Integer lanDauGia = req.getLanDauGia();
					root.fetch("children", JoinType.LEFT);
					
					Subquery<QlnvTtinDauGiaHangHdr> hdrQuery = query.subquery(QlnvTtinDauGiaHangHdr.class);
					Root<QlnvTtinDauGiaHangHdr> hdr = hdrQuery.from(QlnvTtinDauGiaHangHdr.class);

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(hdr.get("maDvi"), maDvi)));						
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(hdr.get("trangThai"), trangThai)));
					}
					if (lanDauGia != null && lanDauGia > 0) {
						predicate.getExpressions().add(builder.and(builder.equal(hdr.get("lanDaugia"), lanDauGia)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(hdr.get("maHanghoa"), maHhoa)));
					}
					predicate1.getExpressions().add(root.get("parent").in(hdrQuery.select(hdr).where(predicate)));
				}
				return predicate1;
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<QlnvQdKQDGHangHdr> buildSearchQuery(final QlnvQdKQDGHangSearchReq req) {
		return new Specification<QlnvQdKQDGHangHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdKQDGHangHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maDvi = req.getMaDvi();
					Date tuNgay = req.getTuNgay();
					Date denNgay = req.getDenNgay();
					String maHhoa = req.getMaHhoa();
					String soQdinh = req.getSoQdinh();
					root.fetch("children", JoinType.LEFT);
					if (denNgay != null) {
						if (tuNgay != null) {
							predicate.getExpressions()
									.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayQdinh"), tuNgay)));
							predicate.getExpressions()
									.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQdinh"), denNgay)));
						} else {
							predicate.getExpressions()
									.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQdinh"), denNgay)));
						}
					} else {
						if (tuNgay != null) {
							predicate.getExpressions()
									.add(builder.and(builder.lessThanOrEqualTo(root.get("ngayQdinh"), tuNgay)));
						}
					}

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinh"), soQdinh)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));
					}
				}
				return predicate;
			}
		};
	}
}
