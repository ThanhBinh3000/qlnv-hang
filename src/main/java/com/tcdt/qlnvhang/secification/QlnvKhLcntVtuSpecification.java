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

import com.tcdt.qlnvhang.request.search.QlnvKhLcntVtuHdrSearchReq;
import com.tcdt.qlnvhang.table.QlnvKhLcntVtuHdr;

public class QlnvKhLcntVtuSpecification {

	@SuppressWarnings("serial")
	public static Specification<QlnvKhLcntVtuHdr> buildSearchQuery(final QlnvKhLcntVtuHdrSearchReq req) {
		return new Specification<QlnvKhLcntVtuHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvKhLcntVtuHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String maVtu = req.getMaVtu();
					String nguoiTao = req.getNguoiTao();
					Date tuNgayLap = req.getTuNgayLap();
					Date denNgayLap = req.getDenNgayLap();
					String trangThai = req.getTrangThai();
					
					root.fetch("detailList", JoinType.LEFT);
					
					if (ObjectUtils.isNotEmpty(tuNgayLap) && ObjectUtils.isNotEmpty(denNgayLap)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayTao"), tuNgayLap)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayTao"), new DateTime(denNgayLap).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maVtu)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maVtu"), maVtu)));
					}
					
					if (StringUtils.isNotBlank(nguoiTao)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("nguoiTao"), nguoiTao)));
					}
					if (StringUtils.isNotBlank(trangThai)) {
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					}
				}
				return predicate;
			}
		};
	}
	
}
