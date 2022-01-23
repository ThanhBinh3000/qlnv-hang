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
import com.tcdt.qlnvhang.request.search.QlnvQdLcntVtuHdrDChinhSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntVtuHdrSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuHdr;

public class QlnvQdLcntVtuSpecification {
	@SuppressWarnings("serial")
	public static Specification<QlnvQdLcntVtuHdr> buildSearchQuery(final QlnvQdLcntVtuHdrSearchReq objReq) {
		return new Specification<QlnvQdLcntVtuHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdLcntVtuHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQd();
				Date denNgayQdinh = objReq.getDenNgayQd();
				String trangThai = objReq.getTrangThai();
				String maHanghoa = objReq.getMaHanghoa();
				String loaiHanghoa = objReq.getLoaiHanghoa();
				String loaiDieuChinh = objReq.getLoaiDieuChinh();
				String loaiQd = objReq.getLoaiQd();

				root.fetch("detailList", JoinType.LEFT);
				
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

				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));
				
				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHanghoa)));

				if (StringUtils.isNotBlank(loaiHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHanghoa"), loaiHanghoa)));

				if (StringUtils.isNotBlank(loaiDieuChinh))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("loaiDieuChinh"), loaiDieuChinh)));

				if (StringUtils.isNotBlank(loaiQd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));

				return predicate;
			}
		};
	}
	
	
	public static Specification<QlnvQdLcntVtuHdr> buildSearchChildQuery(final QlnvQdLcntVtuHdrSearchReq objReq) {
		return new Specification<QlnvQdLcntVtuHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 3015273026770144320L;

			@Override
			public Predicate toPredicate(Root<QlnvQdLcntVtuHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQd();
				Date denNgayQdinh = objReq.getDenNgayQd();
				String trangThai = objReq.getTrangThai();
				String maHanghoa = objReq.getMaHanghoa();
				String loaiHanghoa = objReq.getLoaiHanghoa();
				String loaiDieuChinh = objReq.getLoaiDieuChinh();
				String loaiQd = objReq.getLoaiQd();
				String maDvi = objReq.getMaDvi();

				root.fetch("detailList", JoinType.LEFT);
				Join<QlnvQdLcntVtuHdr, QlnvQdLcntVtuDtl> joinQuerry = root.join("detailList");
				Join<QlnvQdLcntVtuDtl, QlnvQdLcntVtuDtlCtiet> joinCtietQuery = joinQuerry.join("detailList");
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

				if (StringUtils.isNotBlank(loaiDieuChinh))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("loaiDieuChinh"), loaiDieuChinh)));

				if (StringUtils.isNotBlank(maDvi)) {
					predicate.getExpressions().add(builder.and(builder.equal(joinCtietQuery.get("maDvi"), maDvi)));
				}
				return predicate;
			}
		};
	}
	
	public static Specification<QlnvQdLcntVtuHdr> buildFindByIdQuery(final @Valid IdSearchReq objReq) {
		return new Specification<QlnvQdLcntVtuHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4005217827965209754L;

			@Override
			public Predicate toPredicate(Root<QlnvQdLcntVtuHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Long id = objReq.getId();
				String maDvi = objReq.getMaDvi();

				root.fetch("detailList", JoinType.LEFT);
				Join<QlnvQdLcntVtuHdr, QlnvQdLcntVtuDtl> joinQuerry = root.join("detailList");
				Join<QlnvQdLcntVtuDtl, QlnvQdLcntVtuDtlCtiet> joinCtietQuery = joinQuerry.join("detailList");
				predicate.getExpressions().add(builder.and(builder.equal(root.get("id"), id)));
				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinCtietQuery.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
	
	public static Specification<QlnvQdLcntVtuHdr> buildSearchQuery(final QlnvQdLcntVtuHdrDChinhSearchReq objReq) {
		return new Specification<QlnvQdLcntVtuHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5322193869572977697L;

			@Override
			public Predicate toPredicate(Root<QlnvQdLcntVtuHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQd();
				Date denNgayQdinh = objReq.getDenNgayQd();
				String trangThai = objReq.getTrangThai();
				String maHanghoa = objReq.getMaHanghoa();
				String loaiHanghoa = objReq.getLoaiHanghoa();
				String loaiDieuChinh = objReq.getLoaiDieuChinh();
				String loaiQd = objReq.getLoaiQd();
				String soQdKh = objReq.getSoQdKh();

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

				if (StringUtils.isNotBlank(loaiDieuChinh))
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("loaiDieuChinh"), loaiDieuChinh)));

				if (StringUtils.isNotEmpty(loaiQd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));
				
				if (StringUtils.isNotEmpty(soQdKh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKh"), soQdKh)));
				return predicate;
			}
		};
	}
}
