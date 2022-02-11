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
import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaHangThopSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuaHangSearchAdjustReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuaHangSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaHangHdr;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangDtl;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr;
import com.tcdt.qlnvhang.table.QlnvQdMuattDtl;
import com.tcdt.qlnvhang.table.QlnvQdMuattHdr;

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
					
					if (StringUtils.isNotEmpty(soQdinh))
						predicate.getExpressions()
								.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));
					
					if (StringUtils.isNotEmpty(soQDKhoach))
						predicate.getExpressions()
								.add(builder.like(builder.lower(root.get("soQdKh")), "%" + soQDKhoach.toLowerCase() + "%"));

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayLap"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(trangThai))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

					if (StringUtils.isNotBlank(maHhoa))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));
					
					
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
					if (StringUtils.isNotEmpty(soQdinh))
						predicate.getExpressions()
								.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));
					
					if (StringUtils.isNotEmpty(soQDKhoach))
						predicate.getExpressions()
								.add(builder.like(builder.lower(root.get("soQdKh")), "%" + soQDKhoach.toLowerCase() + "%"));

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayLap"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(trangThai))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					
					if (StringUtils.isNotBlank(soQdinhGoc)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinhGoc"), soQdinhGoc)));
					}
				}
				return predicate;
			}
		};
	}
	public static Specification<QlnvQdMuaHangHdr> buildSearchChildQuery(final @Valid QlnvQdMuaHangSearchReq objReq,
			List<String> listLoaiDc) {
		return new Specification<QlnvQdMuaHangHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5640036112400303704L;

			@Override
			public Predicate toPredicate(Root<QlnvQdMuaHangHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgayQdinh)));
					predicate.getExpressions().add(builder.and(
							builder.lessThan(root.get("ngayLap"), new DateTime(denNgayQdinh).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHanghoa"), maHhoa)));

				if (StringUtils.isNotBlank(soQdKh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKh"), soQdKh)));

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				predicate.getExpressions().add(root.get("loaiDchinh").in(listLoaiDc));

				return predicate;
			}
		};
	}
	public static Specification<QlnvQdMuaHangHdr> buildFindByIdQuery(final @Valid IdSearchReq objReq) {
		return new Specification<QlnvQdMuaHangHdr>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1609160279354911278L;

			@Override
			public Predicate toPredicate(Root<QlnvQdMuaHangHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Long id = objReq.getId();
				String maDvi = objReq.getMaDvi();

				root.fetch("children", JoinType.LEFT);
				Join<QlnvQdMuaHangHdr, QlnvQdMuaHangDtl> joinQuerry = root.join("children");

				predicate.getExpressions().add(builder.and(builder.equal(root.get("id"), id)));
				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
	public static Specification<QlnvDxkhMuaHangHdr> buildTHopQuery(final QlnvDxkhMuaHangThopSearchReq req) {
		return new Specification<QlnvDxkhMuaHangHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2124090610735753749L;

			@Override
			public Predicate toPredicate(Root<QlnvDxkhMuaHangHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maHhoa = req.getMaHhoa();
					String soQDKhoach = req.getSoQdKhoach();
					String pthucBan = req.getPthucBan();
					root.fetch("children", JoinType.LEFT);					

					if (StringUtils.isNotBlank(pthucBan)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("pthucBan"), pthucBan)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
					
					if (StringUtils.isNotBlank(soQDKhoach)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKhoach"), soQDKhoach)));
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
