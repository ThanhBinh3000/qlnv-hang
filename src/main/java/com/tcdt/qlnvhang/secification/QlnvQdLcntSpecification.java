package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrSearchReq;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDtl;

public class QlnvQdLcntSpecification {
	@SuppressWarnings("serial")
	public static Specification<HhDxuatKhLcntHdr> buildSearchQuery(final QlnvQdLcntHdrSearchReq objReq) {
		return new Specification<HhDxuatKhLcntHdr>() {
			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQd();
				Date denNgayQdinh = objReq.getDenNgayQd();
				String trangThai = objReq.getTrangThai();
				String maHanghoa = objReq.getMaHanghoa();
				String loaiHanghoa = objReq.getLoaiHanghoa();
				String soQdGiaoCtkh = objReq.getSoQdGiaoCtkh();
				String loaiDieuChinh = objReq.getLoaiDieuChinh();
				String loaiQd = objReq.getLoaiQd();

				root.fetch("detailList", JoinType.LEFT);
				if (StringUtils.isNotEmpty(loaiQd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));

				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayQd"), denNgayQdinh)));
				} else if (ObjectUtils.isNotEmpty(tuNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
				} else if (ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayQd"), denNgayQdinh)));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHanghoa)));

				if (StringUtils.isNotBlank(loaiHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHanghoa"), loaiHanghoa)));

				if (StringUtils.isNotBlank(soQdGiaoCtkh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdGiaoCtkh"), soQdGiaoCtkh)));

				if (StringUtils.isNotBlank(loaiDieuChinh))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("loaiDieuChinh"), loaiDieuChinh)));

				if (StringUtils.isNotBlank(loaiQd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));

				return predicate;
			}
		};
	}

	public static Specification<HhDxuatKhLcntHdr> buildSearchChildQuery(final QlnvQdLcntHdrSearchReq objReq) {
		return new Specification<HhDxuatKhLcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3711358892840221715L;

			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQd();
				Date denNgayQdinh = objReq.getDenNgayQd();
				String trangThai = objReq.getTrangThai();
				String maHanghoa = objReq.getMaHanghoa();
				String loaiHanghoa = objReq.getLoaiHanghoa();
				String soQdGiaoCtkh = objReq.getSoQdGiaoCtkh();
				String loaiDieuChinh = objReq.getLoaiDieuChinh();
				String loaiQd = objReq.getLoaiQd();
				String maDvi = objReq.getMaDvi();

				root.fetch("detailList", JoinType.LEFT);
				Join<HhDxuatKhLcntHdr, HhDchinhDxKhLcntDtl> joinQuerry = root.join("detailList");
				
				if (StringUtils.isNotEmpty(loaiQd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));

				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayQd"), denNgayQdinh)));
				} else if (ObjectUtils.isNotEmpty(tuNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
				} else if (ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayQd"), denNgayQdinh)));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHanghoa)));

				if (StringUtils.isNotBlank(loaiHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHanghoa"), loaiHanghoa)));

				if (StringUtils.isNotBlank(soQdGiaoCtkh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdGiaoCtkh"), soQdGiaoCtkh)));

				if (StringUtils.isNotBlank(loaiDieuChinh))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("loaiDieuChinh"), loaiDieuChinh)));

				if (StringUtils.isNotBlank(maDvi)) {
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));
				}
				return predicate;
			}
		};
	}
	
	public static Specification<HhDxuatKhLcntHdr> buildFindByIdQuery(final @Valid IdSearchReq objReq) {
		return new Specification<HhDxuatKhLcntHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8090749144493370224L;

			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Long id = objReq.getId();
				String maDvi = objReq.getMaDvi();

				root.fetch("detailList", JoinType.LEFT);
				Join<HhDxuatKhLcntHdr, HhDchinhDxKhLcntDtl> joinQuerry = root.join("detailList");

				predicate.getExpressions().add(builder.and(builder.equal(root.get("id"), id)));
				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
	
	public static Specification<HhDxuatKhLcntHdr> buildSearchQuery(final QlnvQdLcntHdrDChinhSearchReq objReq) {
		return new Specification<HhDxuatKhLcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2386244598818116955L;

			@Override
			public Predicate toPredicate(Root<HhDxuatKhLcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				String soQdinhGoc = objReq.getSoQdinhGoc();
				Date tuNgayQdinh = objReq.getTuNgayQd();
				Date denNgayQdinh = objReq.getDenNgayQd();
				String trangThai = objReq.getTrangThai();
				String maHanghoa = objReq.getMaHanghoa();
				String loaiHanghoa = objReq.getLoaiHanghoa();
				String soQdGiaoCtkh = objReq.getSoQdGiaoCtkh();
				String loaiDieuChinh = objReq.getLoaiDieuChinh();
				String loaiQd = objReq.getLoaiQd();

				root.fetch("detailList", JoinType.LEFT);
				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayQd"), denNgayQdinh)));
				} else if (ObjectUtils.isNotEmpty(tuNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
				} else if (ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions().add(builder.and(builder.lessThan(root.get("ngayQd"), denNgayQdinh)));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHanghoa)));

				if (StringUtils.isNotBlank(loaiHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHanghoa"), loaiHanghoa)));

				if (StringUtils.isNotBlank(soQdGiaoCtkh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdGiaoCtkh"), soQdGiaoCtkh)));

				if (StringUtils.isNotBlank(loaiDieuChinh))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("loaiDieuChinh"), loaiDieuChinh)));

				if (StringUtils.isNotBlank(soQdinhGoc))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinhGoc"), soQdinhGoc)));

				if (StringUtils.isNotEmpty(loaiQd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));
				return predicate;
			}
		};
	}
}
