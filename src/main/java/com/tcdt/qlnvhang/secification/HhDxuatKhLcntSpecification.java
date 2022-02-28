package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;

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

				if (StringUtils.isNotEmpty(soDxuat))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soDxuat")), "%" + soDxuat.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayKy) && ObjectUtils.isNotEmpty(denNgayKy)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayKy"), tuNgayKy)));
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayKy"), new DateTime(denNgayKy).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(loaiVthh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				if (StringUtils.isNotEmpty(trichYeu))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("trichYeu")), "%" + trichYeu.toLowerCase() + "%"));

				return predicate;
			}
		};
	}
}
