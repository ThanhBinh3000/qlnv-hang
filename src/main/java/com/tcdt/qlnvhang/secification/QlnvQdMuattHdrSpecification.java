package com.tcdt.qlnvhang.secification;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuattSearchAdjustReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuattSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdMuattDtl;
import com.tcdt.qlnvhang.table.QlnvQdMuattHdr;

public class QlnvQdMuattHdrSpecification {
	public static Specification<QlnvQdMuattHdr> buildSearchQuery(final QlnvQdMuattSearchReq objReq) {
		return new Specification<QlnvQdMuattHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvQdMuattHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQdinh();
				Date denNgayQdinh = objReq.getDenNgayQdinh();
				String trangThai = objReq.getTrangThai();
				String maHhoa = objReq.getMaHhoa();
				String soQdKh = objReq.getSoQdKh();

				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(
							builder.lessThan(root.get("ngayQd"), new DateTime(denNgayQdinh).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				if (StringUtils.isNotBlank(soQdKh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKh"), soQdKh)));

				return predicate;
			}
		};
	}

	public static Specification<QlnvQdMuattHdr> buildSearchAdjQuery(final @Valid QlnvQdMuattSearchAdjustReq objReq) {
		return new Specification<QlnvQdMuattHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7502182145061874464L;

			@Override
			public Predicate toPredicate(Root<QlnvQdMuattHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQdinh();
				Date denNgayQdinh = objReq.getDenNgayQdinh();
				String trangThai = objReq.getTrangThai();
				String soQdKh = objReq.getSoQdKh();
				String soQdinhGoc = objReq.getSoQdinhGoc();
				String loaiDchinh = objReq.getLoaiDchinh();

				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(
							builder.lessThan(root.get("ngayQd"), new DateTime(denNgayQdinh).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotEmpty(soQdinhGoc))
					predicate.getExpressions().add(
							builder.like(builder.lower(root.get("soQdinhGoc")), "%" + soQdinhGoc.toLowerCase() + "%"));

				if (StringUtils.isNotBlank(soQdKh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdKh")), "%" + soQdKh.toLowerCase() + "%"));

				if (StringUtils.isNotBlank(loaiDchinh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiDchinh"), loaiDchinh)));

				return predicate;
			}
		};
	}

	public static Specification<QlnvQdMuattHdr> buildSearchChildQuery(final @Valid QlnvQdMuattSearchReq objReq,
			List<String> listLoaiDc) {
		return new Specification<QlnvQdMuattHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5640036112400303704L;

			@Override
			public Predicate toPredicate(Root<QlnvQdMuattHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soQdinh = objReq.getSoQdinh();
				Date tuNgayQdinh = objReq.getTuNgayQdinh();
				Date denNgayQdinh = objReq.getDenNgayQdinh();
				String trangThai = objReq.getTrangThai();
				String maHhoa = objReq.getMaHhoa();
				String soQdKh = objReq.getSoQdKh();
				String maDvi = objReq.getMaDvi();

				root.fetch("children", JoinType.LEFT);
				Join<QlnvQdMuattHdr, QlnvQdMuattDtl> joinQuerry = root.join("children");

				if (StringUtils.isNotEmpty(soQdinh))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(
							builder.lessThan(root.get("ngayQd"), new DateTime(denNgayQdinh).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				if (StringUtils.isNotBlank(soQdKh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKh"), soQdKh)));

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				predicate.getExpressions().add(root.get("loaiDchinh").in(listLoaiDc));

				return predicate;
			}
		};
	}

	public static Specification<QlnvQdMuattHdr> buildFindByIdQuery(final @Valid IdSearchReq objReq) {
		return new Specification<QlnvQdMuattHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1609160279354911278L;

			@Override
			public Predicate toPredicate(Root<QlnvQdMuattHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Long id = objReq.getId();
				String maDvi = objReq.getMaDvi();

				root.fetch("children", JoinType.LEFT);
				Join<QlnvQdMuattHdr, QlnvQdMuattDtl> joinQuerry = root.join("children");

				predicate.getExpressions().add(builder.and(builder.equal(root.get("id"), id)));
				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
}
