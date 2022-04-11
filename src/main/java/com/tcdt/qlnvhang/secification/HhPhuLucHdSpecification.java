package com.tcdt.qlnvhang.secification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhPhuLucHdSearchReq;
import com.tcdt.qlnvhang.table.HhPhuLucHd;

public class HhPhuLucHdSpecification {
	public static Specification<HhPhuLucHd> buildSearchQuery(final HhPhuLucHdSearchReq objReq) {
		return new Specification<HhPhuLucHd>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhPhuLucHd> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String namKhoach = objReq.getNamKhoach();
				String soHd = objReq.getSoHd();
				String soPluc = objReq.getSoPluc();
				String loaiVthh = objReq.getLoaiVthh();

				if (StringUtils.isNotEmpty(namKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

				if (StringUtils.isNotEmpty(soHd))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soHd"), soHd)));

				if (StringUtils.isNotEmpty(soPluc))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soPluc"), soPluc)));

				if (StringUtils.isNotBlank(loaiVthh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				return predicate;
			}
		};
	}

}
