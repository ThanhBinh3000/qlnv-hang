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

import com.tcdt.qlnvhang.request.search.HhBbNghiemthuKlstSearchReq;
import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;

public class HhBbNghiemthuKlstSpecification {
	public static Specification<HhBbNghiemthuKlstHdr> buildSearchQuery(final HhBbNghiemthuKlstSearchReq objReq) {
		return new Specification<HhBbNghiemthuKlstHdr>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3571167956165654062L;

			@Override
			public Predicate toPredicate(Root<HhBbNghiemthuKlstHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				Date tuNgayLap = objReq.getTuNgayLap();
				Date denNgayLap = objReq.getDenNgayLap();
				String loaiVthh = objReq.getLoaiVthh();
				String trangThai = objReq.getTrangThai();
				String soBb = objReq.getSoBb();
				String maDvi = objReq.getMaDvi();
				String maNgankho = objReq.getMaNganKho();

				if (StringUtils.isNotEmpty(soBb))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soBb"), soBb)));

				if (ObjectUtils.isNotEmpty(tuNgayLap))
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayLap"), tuNgayLap)));

				if (ObjectUtils.isNotEmpty(denNgayLap))
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayLap"), new DateTime(denNgayLap).plusDays(1).toDate())));

				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

				if (StringUtils.isNotEmpty(maDvi))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

				if (StringUtils.isNotEmpty(maNgankho))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maNgankho"), maNgankho)));

				predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

				return predicate;
			}
		};
	}
}
