package com.tcdt.qlnvhang.repository.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.ThongTinDauThauVT;
import com.tcdt.qlnvhang.request.search.ThongTinDauThauVTSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ThongTinDauThauVTRepositoryCustomImpl implements ThongTinDauThauVTRepositoryCustom{
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<ThongTinDauThauVT> search(ThongTinDauThauVTSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * THONG_TIN_DAU_THAU_VT ");
		setConditionSearchCtkhn(req, builder);

		//Sort
		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			builder.append("ORDER BY ").append(sort.get()
					.map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
		}

		Query query = em.createNativeQuery(builder.toString(), Tuple.class);

		//Set params
		this.setParameterSearchCtkhn(req, query);

		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		List<?> data = query.getResultList();

		List<ThongTinDauThauVT> response = data
				.stream()
				.map(res -> {
					Tuple item = (Tuple) res;
					ThongTinDauThauVT ttdt = new ThongTinDauThauVT();
					ttdt.setId(item.get("id", Long.class));
					ttdt.setQdKhlcntId(item.get("id", Long.class));
					ttdt.setSoDxuat(item.get("soDxuat", String.class));
					ttdt.setNgayDxuat(item.get("ngayDxuat", LocalDate.class));
					ttdt.setTongMucDtu(item.get("tongMucDtu", BigDecimal.class));
					return ttdt;
				}).collect(Collectors.toList());

		return new PageImpl<>(response, pageable, this.countCtkhn(req));
	}


	private void setConditionSearchCtkhn(ThongTinDauThauVTSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getSoDxuat())) {
			builder.append("AND ").append("SO_DXUAT = :soDxuat ");
		}
		if (req.getNgayKyTuNgay() != null) {
			builder.append("AND ").append("NGAY_KY >= :ngayKyTuNgay ");
		}
		if (req.getNgayKyDenNgay() != null) {
			builder.append("AND ").append("NGAY_KY <= :ngayKyDenNgay ");
		}
		if (!StringUtils.isEmpty(req.getSoQdinh())) {
			builder.append("AND ").append("SO_QDINH = :soQdinh ");
		}
		if (req.getNgayQdinhTuNgay() != null) {
			builder.append("AND ").append("NGAY_QDINH >= :ngayQdinhTuNgay ");
		}
		if (req.getNgayQdinhDenNgay() != null) {
			builder.append("AND ").append("NGAY_QDINH <= :ngayQdinhDenNgay ");
		}
		if (req.getTenDuAn() != null) {
			builder.append("AND ").append("TEN_DU_AN like :tenDuAn ");
		}
	}

	private int countCtkhn(ThongTinDauThauVTSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) AS totalRecord FROM THONG_TIN_DAU_THAU_VT");

		this.setConditionSearchCtkhn(req, builder);

		Query query = em.createNativeQuery(builder.toString(), Tuple.class);

		this.setParameterSearchCtkhn(req, query);

		List<?> dataCount = query.getResultList();

		if (!CollectionUtils.isEmpty(dataCount)) {
			return total;
		}
		Tuple result = (Tuple) dataCount.get(0);
		return result.get("totalRecord", BigInteger.class).intValue();
	}

	private void setParameterSearchCtkhn(ThongTinDauThauVTSearchReq req, Query query) {
		if (!StringUtils.isEmpty(req.getSoDxuat())) {
			query.setParameter("soDxuat", req.getSoDxuat());
		}
		if (req.getNgayKyTuNgay() != null) {
			query.setParameter("ngayKyTuNgay", req.getNgayKyTuNgay());
		}
		if (req.getNgayKyDenNgay() != null) {
			query.setParameter("ngayKyDenNgay", req.getNgayKyDenNgay());
		}
		if (!StringUtils.isEmpty(req.getSoQdinh())) {
			query.setParameter("soQdinh", req.getSoQdinh());
		}
		if (req.getNgayQdinhTuNgay() != null) {
			query.setParameter("ngayQdinhTuNgay", req.getNgayQdinhTuNgay());
		}
		if (req.getNgayQdinhDenNgay() != null) {
			query.setParameter("ngayQdinhDenNgay", req.getNgayQdinhDenNgay());
		}
		if (!StringUtils.isEmpty(req.getTenDuAn())) {
			query.setParameter("tenDuAn", "%" + req.getTenDuAn() + "%");
		}
	}
}
