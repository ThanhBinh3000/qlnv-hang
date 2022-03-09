package com.tcdt.qlnvhang.secification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhDthauSearchReq;
import com.tcdt.qlnvhang.table.HhDthau2;

public class HhDthau2Specification {
	public static Specification<HhDthau2> buildSearchQuery(final HhDthauSearchReq objReq) {
		return new Specification<HhDthau2>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhDthau2> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String namKhoach = objReq.getNamKhoach();
				String loaiVthh = objReq.getLoaiVthh();
				String maDvi = objReq.getMaDvi();
				String soQd = objReq.getSoQd();
				String tenHd = objReq.getTenHd();
				String trangThai = objReq.getTrangThai();

				if (StringUtils.isNotEmpty(soQd))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQd")), "%" + soQd.toLowerCase() + "%"));

				if (StringUtils.isNotEmpty(tenHd))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("tenHd")), "%" + tenHd.toLowerCase() + "%"));

				if (StringUtils.isNotEmpty(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotEmpty(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotEmpty(namKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

				predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				return predicate;
			}
		};
	}
}
