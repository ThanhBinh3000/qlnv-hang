package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhQdPduyetKqlcntSearchReq;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;

public class HhQdPduyetKqlcntSpecification {
	public static Specification<HhQdPduyetKqlcntHdr> buildSearchQuery(final HhQdPduyetKqlcntSearchReq objReq) {
		return new Specification<HhQdPduyetKqlcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhQdPduyetKqlcntHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String namKhoach = objReq.getNamKhoach();
				Date tuNgayQd = objReq.getTuNgayQd();
				Date denNgayQd = objReq.getDenNgayQd();
				String loaiVthh = objReq.getLoaiVthh();
				String soQd = objReq.getSoQd();
				String trangThai = objReq.getTrangThai();
				String maDvi = objReq.getMaDvi();

				root.fetch("children", JoinType.LEFT);

				if (StringUtils.isNotEmpty(namKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

				if (ObjectUtils.isNotEmpty(tuNgayQd))
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgayQd)));

				if (ObjectUtils.isNotEmpty(denNgayQd))
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayQd"), new DateTime(denNgayQd).plusDays(1).toDate())));

				if (StringUtils.isNotBlank(loaiVthh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				if (StringUtils.isNotBlank(soQd))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQd")), "%" + soQd.toLowerCase() + "%"));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				return predicate;
			}
		};
	}
}
