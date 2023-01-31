package com.tcdt.qlnvhang.secification;

import com.tcdt.qlnvhang.request.search.banhang.BhHopDongSearchReq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public class BhHopDongSpecification {
    public static Specification<XhHopDongHdr> buildSearchQuery(final BhHopDongSearchReq objReq) {
        return new Specification<XhHopDongHdr>() {
            /**
             *
             */
            private static final long serialVersionUID = 3571167956165654062L;

            @SuppressWarnings({ "unused", "unchecked" })
            @Override
            public Predicate toPredicate(Root<XhHopDongHdr> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = builder.conjunction();
                if (ObjectUtils.isEmpty(objReq))
                    return predicate;

                String loaiVthh = objReq.getLoaiVthh();
                String maDvi = objReq.getMaDvi();
                String sohd = objReq.getSoHd();
//				String maDviB = objReq.getMaDviB();
                String trangThai = objReq.getTrangThai();
                Date tuNgayKy = objReq.getTuNgayKy();
                Date denNgayKy = objReq.getDenNgayKy();

                Join<Object, Object> fetchParent = (Join<Object, Object>) root.fetch("children1", JoinType.LEFT);

//				if (StringUtils.isNotEmpty(maDviB))
//					predicate.getExpressions()
//							.add(builder.like(builder.lower(fetchParent.get("ten")), "%" + maDviB.toLowerCase() + "%"));

                if (StringUtils.isNotEmpty(sohd))
                    predicate.getExpressions()
                            .add(builder.like(builder.lower(root.get("sohd")), "%" + sohd.toLowerCase() + "%"));

                if (StringUtils.isNotEmpty(maDvi))
                    predicate.getExpressions().add(builder.and(builder.equal(root.get("maDvi"), maDvi)));

                if (StringUtils.isNotEmpty(trangThai))
                    predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));

                if (ObjectUtils.isNotEmpty(tuNgayKy))
                    predicate.getExpressions()
                            .add(builder.and(builder.greaterThanOrEqualTo(root.get("ngayKy"), tuNgayKy)));

                if (ObjectUtils.isNotEmpty(tuNgayKy) && ObjectUtils.isNotEmpty(denNgayKy))
                    predicate.getExpressions().add(builder
                            .and(builder.lessThan(root.get("ngayKy"), new DateTime(denNgayKy).plusDays(1).toDate())));

                predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiVthh"), loaiVthh)));

                return predicate;
            }
        };
    }
}
