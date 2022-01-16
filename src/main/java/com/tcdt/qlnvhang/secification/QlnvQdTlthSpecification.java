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
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdTlthSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdTlthDtl;
import com.tcdt.qlnvhang.table.QlnvQdTlthHdr;

public class QlnvQdTlthSpecification {

	@SuppressWarnings("serial")
	public static Specification<QlnvQdTlthHdr> buildSearchQuery(final QlnvQdTlthSearchReq req) {
		return new Specification<QlnvQdTlthHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdTlthHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					Date tuNgayQdinh = req.getTuNgayQdinh();
					Date denNgayQdinh = req.getDenNgayQdinh();
					String maHhoa = req.getMaHhoa();
					String soQdinh = req.getSoQdinh();
					String lhinhXuat = req.getLhinhXuat();
					root.fetch("children", JoinType.LEFT);
					
					if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQdinh"), tuNgayQdinh)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayQdinh"), new DateTime(denNgayQdinh).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));
					}
					if (StringUtils.isNotBlank(lhinhXuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("lhinhXuat"), lhinhXuat)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));
					}
				}
				return predicate;
			}
		};
	}
	
	public static Specification<QlnvQdTlthHdr> buildSearchChildQuery(final QlnvQdTlthSearchReq req) {
		return new Specification<QlnvQdTlthHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7196589727342771480L;

			@Override
			public Predicate toPredicate(Root<QlnvQdTlthHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					Date tuNgayQdinh = req.getTuNgayQdinh();
					Date denNgayQdinh = req.getDenNgayQdinh();
					String maHhoa = req.getMaHhoa();
					String soQdinh = req.getSoQdinh();
					String lhinhXuat = req.getLhinhXuat();
					String maDvi = req.getMaDvi();
					root.fetch("children", JoinType.LEFT);
					
					Join<QlnvQdTlthHdr, QlnvQdTlthDtl> joinQuerry = root.join("children");
					
					if (ObjectUtils.isNotEmpty(tuNgayQdinh) && ObjectUtils.isNotEmpty(denNgayQdinh)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQdinh"), tuNgayQdinh)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayQdinh"), new DateTime(denNgayQdinh).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(soQdinh)) {
						predicate.getExpressions().add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));
					}
					if (StringUtils.isNotBlank(lhinhXuat)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("lhinhXuat"), lhinhXuat)));
					}
					if (StringUtils.isNotBlank(maHhoa)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));
					}
					
					if (StringUtils.isNotBlank(maDvi)) {
						predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));
					}
				}
				return predicate;
			}
		};
	}
	
	public static Specification<QlnvQdTlthHdr> buildFindByIdQuery(final @Valid IdSearchReq objReq) {
		return new Specification<QlnvQdTlthHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2722133552384254373L;

			@Override
			public Predicate toPredicate(Root<QlnvQdTlthHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Long id = objReq.getId();
				String maDvi = objReq.getMaDvi();

				root.fetch("children", JoinType.LEFT);
				Join<QlnvQdTlthHdr, QlnvQdTlthDtl> joinQuerry = root.join("children");

				predicate.getExpressions().add(builder.and(builder.equal(root.get("id"), id)));
				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
}
