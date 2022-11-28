package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhHopDongSearchReq;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;

public class HhHopDongSpecification {
	public static Specification<HhHopDongHdr> buildSearchQuery(final HhHopDongSearchReq objReq) {
		return new Specification<HhHopDongHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@SuppressWarnings({ "unused", "unchecked" })
			@Override
			public Predicate toPredicate(Root<HhHopDongHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String loaiVthh = objReq.getLoaiVthh();
				String maDvi = objReq.getMaDvi();
				String sohd = objReq.getSoHd();
//				String maDviB = objReq.getMaDviB();
				String trangThai = objReq.getTrangThai();
				Date tuNgayKy = objReq.getTuNgayKy();
				Date denNgayKy = objReq.getDenNgayKy();

				Join<Object, Object> fetchParent = (Join<Object, Object>) root.fetch("children1", JoinType.LEFT);

//				if (StringUtils.isNotEmpty(maDviB))
//					predicate.getExpressions()
//							.add(builder.like(builder.lower(fetchParent.get("ten")), "%" + maDviB.toLowerCase() + "%"));

				if (StringUtils.isNotEmpty(sohd))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("sohd")), "%" + sohd.toLowerCase() + "%"));

				if (StringUtils.isNotEmpty(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotEmpty(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (ObjectUtils.isNotEmpty(tuNgayKy))
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayKy"), tuNgayKy)));

				if (ObjectUtils.isNotEmpty(tuNgayKy) && ObjectUtils.isNotEmpty(denNgayKy))
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayKy"), new DateTime(denNgayKy).plusDays(1).toDate())));

				predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				return predicate;
			}
		};
	}
}
