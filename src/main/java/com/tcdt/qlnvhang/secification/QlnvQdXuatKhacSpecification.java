package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdXuatKhacSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdXuatKhacDtl;
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

	public static Specification<QlnvQdXuatKhacHdr> buildSearchChildQuery(final QlnvQdXuatKhacSearchReq req) {
		return new Specification<QlnvQdXuatKhacHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5624279346273762105L;

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
					String maDvi = req.getMaDvi();

					root.fetch("children", JoinType.LEFT);
					Join<QlnvQdXuatKhacHdr, QlnvQdXuatKhacDtl> joinQuerry = root.join("children");

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

					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));
					}
				}
				return predicate;
			}
		};
	}

	public static Specification<QlnvQdXuatKhacHdr> buildFindByIdQuery(final @Valid IdSearchReq objReq) {
		return new Specification<QlnvQdXuatKhacHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1154648236308329689L;

			@Override
			public Predicate toPredicate(Root<QlnvQdXuatKhacHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Long id = objReq.getId();
				String maDvi = objReq.getMaDvi();

				root.fetch("children", JoinType.LEFT);
				Join<QlnvQdXuatKhacHdr, QlnvQdXuatKhacDtl> joinQuerry = root.join("children");

				predicate.getExpressions().add(builder.and(builder.equal(root.get("id"), id)));
				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
}
