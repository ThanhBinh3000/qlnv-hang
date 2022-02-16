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

import com.tcdt.qlnvhang.request.search.QlnvQdPhuongAnGiaSearchReq;
import com.tcdt.qlnvhang.table.QlnvDxPhuongAnGiaHdr;
import com.tcdt.qlnvhang.table.QlnvQdPhuongAnGiaHdr;

public class QDinhPAnGiaSpecification {	

	@SuppressWarnings("serial")
	public static Specification<QlnvQdPhuongAnGiaHdr> buildSearchQuery(final QlnvQdPhuongAnGiaSearchReq req) {
		return new Specification<QlnvQdPhuongAnGiaHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvQdPhuongAnGiaHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					Date tuNgay = req.getTuNgayQdinh();
					Date denNgay = req.getDenNgayQdinh();
					String soQdinh = req.getSoQdinh();
					String namKhoach = req.getNamKhoach();
					root.fetch("children", JoinType.LEFT);					
					
					if (StringUtils.isNotEmpty(soQdinh))
						predicate.getExpressions()
								.add(builder.like(builder.lower(root.get("soQdinh")), "%" + soQdinh.toLowerCase() + "%"));
					
					if (StringUtils.isNotEmpty(namKhoach))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

					if (ObjectUtils.isNotEmpty(tuNgay) && ObjectUtils.isNotEmpty(denNgay)) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), tuNgay)));
						predicate.getExpressions().add(builder.and(
								builder.lessThan(root.get("ngayQd"), new DateTime(denNgay).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(trangThai))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					
				}
				return predicate;
			}
		};
	}	
	@SuppressWarnings("serial")
	public static Specification<QlnvDxPhuongAnGiaHdr> buildTHopQuery(final QlnvQdPhuongAnGiaSearchReq req) {
		return new Specification<QlnvDxPhuongAnGiaHdr>() {
			@Override
			public Predicate toPredicate(Root<QlnvDxPhuongAnGiaHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (req != null) {
					String trangThai = req.getTrangThai();
					String namKhoach = req.getNamKhoach();
					root.fetch("children", JoinType.LEFT);					
					
					
					
					if (StringUtils.isNotEmpty(namKhoach))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("namKhoach"), namKhoach)));

					
					if (StringUtils.isNotBlank(trangThai))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
					
				}
				return predicate;
			}
		};
	}	
}
