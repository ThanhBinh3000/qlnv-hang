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

import com.tcdt.qlnvhang.request.search.HhPaKhlcntSearchReq;
import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;

public class HhPaKhlcntSpecification {
	public static Specification<HhPaKhlcntHdr> buildSearchQuery(final HhPaKhlcntSearchReq objReq) {
		return new Specification<HhPaKhlcntHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhPaKhlcntHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String namKhoach = objReq.getNamKhoach();
				Date tuNgayTao = objReq.getTuNgayTao();
				Date denNgayTao = objReq.getDenNgayTao();
				String loaiVthh = objReq.getLoaiVthh();

				if (StringUtils.isNotEmpty(namKhoach))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

				if (ObjectUtils.isNotEmpty(tuNgayTao))
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayTao"), tuNgayTao)));

				if (ObjectUtils.isNotEmpty(denNgayTao))
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayTao"), new DateTime(denNgayTao).plusDays(1).toDate())));

				if (StringUtils.isNotBlank(loaiVthh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				return predicate;
			}
		};
	}
}
