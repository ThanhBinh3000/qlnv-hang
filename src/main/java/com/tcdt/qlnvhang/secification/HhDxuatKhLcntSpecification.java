package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntDtl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhDxKhLcntDsChuaThReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.util.Contains;

public class HhDxuatKhLcntSpecification {
	public static Specification<HhDxuatKhLcntHdr> buildSearchQuery(final HhDxuatKhLcntSearchReq objReq) {
		return new Specification<HhDxuatKhLcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soDxuat = objReq.getSoDxuat();
				Date tuNgayKy = objReq.getTuNgayKy();
				Date denNgayKy = objReq.getDenNgayKy();
				String trangThai = objReq.getTrangThai();
				String loaiVthh = objReq.getLoaiVthh();
				String trichYeu = objReq.getTrichYeu();
				String maDvi = objReq.getMaDvi();

				if (StringUtils.isNotEmpty(soDxuat))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soDxuat")), "%" + soDxuat.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayKy))
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayKy"), tuNgayKy)));

				if (ObjectUtils.isNotEmpty(tuNgayKy) && ObjectUtils.isNotEmpty(denNgayKy))
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayKy"), new DateTime(denNgayKy).plusDays(1).toDate())));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(loaiVthh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				if (StringUtils.isNotEmpty(trichYeu))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("trichYeu")), "%" + trichYeu.toLowerCase() + "%"));

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}

	public static Specification<HhDxuatKhLcntHdr> buildTHopQuery(final @Valid HhDxKhLcntTChiThopReq req) {
		return new Specification<HhDxuatKhLcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2124090610735753749L;

			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(req))
					return predicate;

				root.fetch("children1", JoinType.LEFT);
				Join<HhDxuatKhLcntHdr, HhDxuatKhLcntDtl> joinQuerry = root.join("children1");

				String trangThai = Contains.DUYET;
				String namKhoach = req.getNamKh();
				String loaiVthh = req.getLoaiVthh();
				String hthucLcnt = req.getHthucLcnt();
				String pthucLcnt = req.getPthucLcnt();
				String loaiHdong = req.getLoaiHdong();
				String nguonVon = req.getNguonVon();

				// Add paramter vao bang hdr
				predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));
				predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
				predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));
				// Add parameter vao bang dtl
				predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("hthucLcnt"), hthucLcnt)));
				predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("pthucLcnt"), pthucLcnt)));
				predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("loaiHdong"), loaiHdong)));
				predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("nguonVon"), nguonVon)));

				return predicate;
			}
		};
	}

	public static Specification<HhDxuatKhLcntHdr> buildDsChuaTh(final @Valid HhDxKhLcntDsChuaThReq req) {
		return new Specification<HhDxuatKhLcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2124090610735753749L;

			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(req))
					return predicate;

				root.fetch("children1", JoinType.LEFT);

				// Add paramter vao bang hdr
				predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), req.getNamKhoach())));
				predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), req.getTrangThai())));
				predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), req.getLoaiVthh())));

				return predicate;
			}
		};
	}
}
