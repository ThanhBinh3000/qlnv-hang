package com.tcdt.qlnvhang.secification;

import java.util.Date;
import java.util.Set;

import javax.persistence.criteria.*;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import org.springframework.util.CollectionUtils;

public class HhQdGiaoNvuNhapxuatSpecification {

	@SuppressWarnings("serial")
	public static Specification<NhQdGiaoNvuNhapxuatHdr> buildSearchQuery(final HhQdNhapxuatSearchReq req) {
		return new Specification<NhQdGiaoNvuNhapxuatHdr>() {
			@Override
			public Predicate toPredicate(Root<NhQdGiaoNvuNhapxuatHdr> root, CriteriaQuery<?> query,
										 CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				
				if (req != null) {
					Set<String> trangThais = req.getTrangThais();
					String maDvi = req.getMaDvi();
					Date ngayQd = req.getNgayQd();
					String soQd = req.getSoQd();
//					String soHd = req.getSoHd();
					String maVthh = req.getMaVthh();
					String loaiQd = req.getLoaiQd();
//					Integer namNhap = Integer.valueOf(req.getNamNhap());
//					String veViec = req.getVeViec();

					Join<NhQdGiaoNvuNhapxuatHdr, NhQdGiaoNvuNhapxuatDtl> joinQuerry = root.join("children");

					if (ngayQd != null) {
						predicate.getExpressions()
								.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayKy"), ngayQd)));
						predicate.getExpressions().add(builder
								.and(builder.lessThan(root.get("ngayKy"), new DateTime(ngayQd).plusDays(1).toDate())));
					}

					if (StringUtils.isNotBlank(maDvi))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

					if (!CollectionUtils.isEmpty(trangThais))
						predicate.getExpressions().add(builder.and(root.get("trangThai").in(trangThais)));

					if (StringUtils.isNotBlank(soQd))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("soQd"), soQd)));

//					if (StringUtils.isNotBlank(soHd))
//						predicate.getExpressions().add(builder.and(builder.equal(root.get("soHd"), soHd)));

					if (StringUtils.isNotBlank(maVthh))
						predicate.getExpressions().add(builder.and(builder.equal(joinQuerry.get("maVthh"), maVthh)));

					if (StringUtils.isNotBlank(loaiQd))
						predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiQd"), loaiQd)));

//					if (!Objects.isNull(namNhap))
//						predicate.getExpressions().add(builder.and(builder.equal(root.get("namNhap"), namNhap)));

//					if (!Objects.isNull(veViec))
//						predicate.getExpressions().add(builder.and(builder.equal(root.get("veViec"), veViec)));

				}
				return predicate;
			}
		};
	}
}
