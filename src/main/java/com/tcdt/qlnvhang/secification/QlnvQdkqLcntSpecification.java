package com.tcdt.qlnvhang.secification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.tcdt.qlnvhang.request.search.QlnvQdkqLcntSearchReq;
import com.tcdt.qlnvhang.table.QlnvQdkqLcnt;

public class QlnvQdkqLcntSpecification {
	public static Specification<QlnvQdkqLcnt> buildSearchQuery(final QlnvQdkqLcntSearchReq objReq) {
		return new Specification<QlnvQdkqLcnt>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7310167218440158646L;

			@Override
			public Predicate toPredicate(Root<QlnvQdkqLcnt> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (ObjectUtils.isEmpty(objReq))
					return predicate;

				String loaiHanghoa = objReq.getLoaiHanghoa();
				String soQdinhKh = objReq.getSoQdinhKh();
				Date ngayQdinhKh = objReq.getNgayQdinhKh();
				String soQdinhKq = objReq.getSoQdinhKq();
				Date ngayQdinhKq = objReq.getNgayQdinhKq();
				String trangThai = objReq.getTrangThai();

				if (ObjectUtils.isNotEmpty(ngayQdinhKh)) {
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("ngayQdKh"), ngayQdinhKh)));
				}

				if (ObjectUtils.isNotEmpty(ngayQdinhKq)) {
					predicate.getExpressions()
							.add(builder.and(builder.equal(root.get("ngayQd"), ngayQdinhKq)));
				}
				
				if (StringUtils.isNotBlank(loaiHanghoa))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("loaiHanghoa"), loaiHanghoa)));
				
				if (StringUtils.isNotBlank(trangThai))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("trangThai"), trangThai)));
				
				if (StringUtils.isNotBlank(soQdinhKh))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinhKh"), soQdinhKh)));
				
				if (StringUtils.isNotBlank(soQdinhKq))
					predicate.getExpressions().add(builder.and(builder.equal(root.get("soQdinh"), soQdinhKq)));
			
				return predicate;
			}
		};
	}
}
