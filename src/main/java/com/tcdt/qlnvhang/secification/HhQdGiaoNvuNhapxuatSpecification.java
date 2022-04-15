package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.*;

import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatDtl;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;

public class HhQdGiaoNvuNhapxuatSpecification {

	@SuppressWarnings("serial")
	public static Specification<HhQdGiaoNvuNhapxuatHdr> buildSearchQuery(final HhQdNhapxuatSearchReq req) {
		return new Specification<HhQdGiaoNvuNhapxuatHdr>() {
			@Override
			public Predicate toPredicate(Root<HhQdGiaoNvuNhapxuatHdr> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				
				if (req != null) {
					String trangThai = req.getTrangThai();
					String maDvi = req.getMaDvi();
					Date ngayQd = req.getNgayQd();
					String soQD = req.getSoQd();
					String soHd = req.getSoHd();
					String maVthh = req.getMaVthh();
					String loaiQd = req.getLoaiQd();

					Join<HhQdGiaoNvuNhapxuatHdr, HhQdGiaoNvuNhapxuatDtl> joinQuerry = root.join("children");

					if (ngayQd != null) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayQd"), ngayQd)));
						predicate.getExpressions().add(builder
								.and(builder.lessThan(root.get("ngayQd"), new DateTime(ngayQd).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDvi))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

					if (StringUtils.isNotBlank(trangThai))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

					if (StringUtils.isNotBlank(soQD))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQD"), soQD)));

					if (StringUtils.isNotBlank(soHd))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soHd"), soHd)));

					if (StringUtils.isNotBlank(maVthh))
						predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maVthh"), maVthh)));

					if (StringUtils.isNotBlank(loaiQd))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));

				}
				return predicate;
			}
		};
	}
}
