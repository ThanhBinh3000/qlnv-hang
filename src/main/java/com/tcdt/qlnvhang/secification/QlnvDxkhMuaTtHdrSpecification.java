package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaTtSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaTtThopSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtDtl;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtHdr;
import com.tcdt.qlnvhang.util.Contains;

public class QlnvDxkhMuaTtHdrSpecification {
	public static Specification<QlnvDxkhMuaTtHdr> buildSearchQuery(final QlnvDxkhMuaTtSearchReq objReq) {
		return new Specification<QlnvDxkhMuaTtHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvDxkhMuaTtHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soDxuat = objReq.getSoDxuat();
				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String maDvi = objReq.getMaDvi();
				String trangThai = objReq.getTrangThai();
				String maHhoa = objReq.getMaHhoa();
				String soQdKhoach = objReq.getSoQdKhoach();

				if (StringUtils.isNotEmpty(soDxuat))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soDxuat")), "%" + soDxuat.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayLap) && ObjectUtils.isNotEmpty(denNgayLap)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgayLap)));
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayLap"), new DateTime(denNgayLap).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				if (StringUtils.isNotBlank(soQdKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdKhoach"), soQdKhoach)));

				return predicate;
			}
		};
	}

	public static Specification<QlnvDxkhMuaTtDtl> buildTHopQuery(final QlnvDxkhMuaTtThopSearchReq objReq) {
		return new Specification<QlnvDxkhMuaTtDtl>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 821046090969276355L;

			@Override
			public Predicate toPredicate(Root<QlnvDxkhMuaTtDtl> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate1 = builder.conjunction();
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate1;

				String trangThai = Contains.DUYET;
				String maDvi = objReq.getMaDvi();
				String maHhoa = objReq.getMaHhoa();
				String soQdKhoach = objReq.getSoQdKhoach();
				root.fetch("children", JoinType.LEFT);

				Subquery<QlnvDxkhMuaTtHdr> hdrQuery = query.subquery(QlnvDxkhMuaTtHdr.class);
				Root<QlnvDxkhMuaTtHdr> hdr = hdrQuery.from(QlnvDxkhMuaTtHdr.class);

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("maDvi"), maDvi)));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(soQdKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("soQdKhoach"), soQdKhoach)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(hdr.get("maHhoa"), maHhoa)));

				predicate1.getExpressions().add(root.get("parent").in(hdrQuery.select(hdr).where(predicate)));

				return predicate1;
			}
		};
	}
}
