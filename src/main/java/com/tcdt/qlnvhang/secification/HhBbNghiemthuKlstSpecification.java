package com.tcdt.qlnvhang.secification;

import java.util.Date;
import java.util.Set;

import javax.persistence.criteria.*;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.HhBbNghiemthuKlstSearchReq;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import org.springframework.util.CollectionUtils;

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

				Date tuNgayNghiemThu = objReq.getTuNgayNghiemThu();
				Date denNgayNghiemThu = objReq.getDenNgayNghiemThu();
				String loaiVthh = objReq.getLoaiVthh();
				Set<String> trangThais = objReq.getTrangThais();
				String soBb = objReq.getSoBb();
				Set<String> maDvis = objReq.getMaDvis();
				String maNganlo = objReq.getMaNganlo();
				String maVatTuCha = objReq.getMaVatTuCha();
				if (StringUtils.isNotEmpty(soBb))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soBb"), soBb)));

				if (ObjectUtils.isNotEmpty(tuNgayNghiemThu))
					predicate.getExpressions()
							.add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayNghiemThu"), tuNgayNghiemThu)));

				if (ObjectUtils.isNotEmpty(denNgayNghiemThu))
					predicate.getExpressions().add(builder
							.and(builder.lessThan(root.get("ngayNghiemThu"), new DateTime(denNgayNghiemThu).plusDays(1).toDate())));

				if (!CollectionUtils.isEmpty(trangThais))
					predicate.getExpressions().add(builder.and(root.get("trangThai").in(trangThais)));

				if (!CollectionUtils.isEmpty(maDvis))
					predicate.getExpressions().add(builder.and(root.get("maDvi").in(maDvis)));

				if (StringUtils.isNotEmpty(maNganlo))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maNganLo"), maNganlo)));

				if (StringUtils.isNotEmpty(loaiVthh)) {
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));
				} else if (StringUtils.isNotEmpty(maVatTuCha)) {
					predicate.getExpressions().add(builder.and(builder.equal(root.get("maVatTuCha"), maVatTuCha)));
				}

				return predicate;
			}
		};
	}
}
