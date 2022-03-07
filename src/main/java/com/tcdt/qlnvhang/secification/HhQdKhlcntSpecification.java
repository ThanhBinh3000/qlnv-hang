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

import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;

public class HhQdKhlcntSpecification {
	public static Specification<HhQdKhlcntHdr> buildSearchQuery(final HhQdKhlcntSearchReq objReq) {
		return new Specification<HhQdKhlcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhQdKhlcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String namKhoach = objReq.getNamKhoach();
				Date tuNgayQd = objReq.getTuNgayQd();
				Date denNgayQd = objReq.getDenNgayQd();
				String loaiVthh = objReq.getLoaiVthh();
				String soQd = objReq.getSoQd();

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

				if (StringUtils.isNotEmpty(soQd))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soQd")), "%" + soQd.toLowerCase() + "%"));

				return predicate;
			}
		};
	}
}
