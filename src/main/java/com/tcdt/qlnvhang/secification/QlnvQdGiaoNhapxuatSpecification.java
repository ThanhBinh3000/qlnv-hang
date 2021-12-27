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

import com.tcdt.qlnvhang.request.search.QlnvQdGiaoNhapxuatSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdGiaoNhapxuatHdr;

public class QlnvQdGiaoNhapxuatSpecification {
	public static Specification<QlnvQdGiaoNhapxuatHdr> buildSearchQuery(final QlnvQdGiaoNhapxuatSearchReq objReq) {
		return new Specification<QlnvQdGiaoNhapxuatHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<QlnvQdGiaoNhapxuatHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String soHdong = objReq.getSoHdong();
				Date tuNgayKy = objReq.getTuNgayKy();
				Date denNgayKy = objReq.getDenNgayKy();
				String maHhoa = objReq.getMaHhoa();
				String trangThai = objReq.getTrangThai();
				String maDvi = objReq.getMaDvi();
				String loaiHdong = objReq.getLoaiHdong();

				if (StringUtils.isNotEmpty(soHdong))
					predicate.getExpressions()
							.add(builder.like(builder.lower(root.get("soHdong")), "%" + soHdong.toLowerCase() + "%"));

				if (ObjectUtils.isNotEmpty(tuNgayKy) && ObjectUtils.isNotEmpty(denNgayKy)) {
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayKy"), tuNgayKy)));
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayKy"), new DateTime(denNgayKy).plusDays(1).toDate())));
				}

				if (StringUtils.isNotBlank(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotBlank(loaiHdong))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHdong"), loaiHdong)));

				if (StringUtils.isNotBlank(maHhoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maHhoa"), maHhoa)));

				return predicate;
			}
		};
	}
}
