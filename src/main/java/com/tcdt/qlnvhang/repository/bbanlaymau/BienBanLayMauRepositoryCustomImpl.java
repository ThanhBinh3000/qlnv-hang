package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
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
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BienBanLayMauRepositoryCustomImpl implements BienBanLayMauRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<BienBanLayMau> search(BienBanLayMauSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM BB_LAY_MAU ");
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

		List<BienBanLayMau> response = data
				.stream()
				.map(res -> {
					Tuple item = (Tuple) res;
					BienBanLayMau bb = new BienBanLayMau();
					bb.setId(item.get("ID", Long.class));
					bb.setSoBban(item.get("SO_BBAN", String.class));
					bb.setNgayLapBban(item.get("NGAY_LAP_BBAN", LocalDate.class));
					bb.setMaKho(item.get("MA_KHO", String.class));
					bb.setMaNgan(item.get("MA_NGAN", String.class));
					bb.setMaLo(item.get("MA_LO", String.class));
					bb.setMaHhoa(item.get("MA_HHOA", String.class));
					bb.setPphapLayMau(item.get("PPHAP_LAY_MAU", String.class));
					bb.setSluongLMau(item.get("SLUONG_MAU", String.class));
					bb.setKquaNiemPhongMau(item.get("KQUA_NIEM_PHONG_MAU", String.class));
					bb.setDdiemKtra(item.get("DDIEM_KTRA", String.class));
					bb.setCcuQdinhGiaoNvuNhap(item.get("CCU_QDINH_GIAO_NVU_NHAP", String.class));
					bb.setCtieuKtra(item.get("CTIEU_KTRA", String.class));
					bb.setTenDdienNhan(item.get("TEN_DDIEN_NHAN", String.class));
					bb.setMaDviNhan(item.get("MA_DVI_NHAN", String.class));
					bb.setCvuDdienNhan(item.get("CVU_DDIEN_NHAN", String.class));
					bb.setTenDdienCcap(item.get("TEN_DDIEN_CCAP", String.class));
					bb.setMaDviCcap(item.get("MA_DVI_CCAP", String.class));
					bb.setTrangThai(item.get("TRANG_THAI", String.class));
					return bb;
				}).collect(Collectors.toList());

		return new PageImpl<>(response, pageable, this.countCtkhn(req));
	}


	private void setConditionSearchCtkhn(BienBanLayMauSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getMaHhoa())) {
			builder.append("AND ").append("MA_HHOA = :maHhoa ");
		}

		if (!StringUtils.isEmpty(req.getMaKho())) {
			builder.append("AND ").append("MA_KHO = :maKho ");
		}

		if (!StringUtils.isEmpty(req.getMaNgan())) {
			builder.append("AND ").append("MA_NGAN = :maNgan ");
		}

		if (!StringUtils.isEmpty(req.getMaLo())) {
			builder.append("AND ").append("MA_LO = :maLo ");
		}
		if (!StringUtils.isEmpty(req.getSoQdinhGiaoNvuNhap())) {
			builder.append("AND ").append("SO_QDINH_GIAO_NVU_NHAP = :soQdinhGiaoNvuNhap ");
		}
		if (req.getNgayLapBbanTuNgay() != null) {
			builder.append("AND ").append("NGAY_LAP_BBAN >= :ngayLapBbanTuNgay ");
		}
		if (req.getNgayLapBbanDenNgay() != null) {
			builder.append("AND ").append("NGAY_LAP_BBAN <= :ngayLapBbanDenNgay ");
		}
	}

	private int countCtkhn(BienBanLayMauSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) AS totalRecord FROM BIEN_BAN_LAY_MAU");

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

	private void setParameterSearchCtkhn(BienBanLayMauSearchReq req, Query query) {
		if (!StringUtils.isEmpty(req.getMaHhoa())) {
			query.setParameter("maHhoa", req.getMaHhoa());
		}

		if (!StringUtils.isEmpty(req.getMaKho())) {
			query.setParameter("maKho", req.getMaKho());
		}

		if (!StringUtils.isEmpty(req.getMaNgan())) {
			query.setParameter("maNgan", req.getMaNgan());
		}

		if (!StringUtils.isEmpty(req.getMaLo())) {
			query.setParameter("maLo", req.getMaLo());
		}

		if (!StringUtils.isEmpty(req.getSoQdinhGiaoNvuNhap())) {
			query.setParameter("soQdinhGiaoNvuNhap", req.getSoQdinhGiaoNvuNhap());
		}

		if (req.getNgayLapBbanTuNgay() != null) {
			query.setParameter("ngayLapBbanTuNgay", req.getNgayLapBbanTuNgay());
		}

		if (req.getNgayLapBbanDenNgay() != null) {
			query.setParameter("ngayLapBbanDenNgay", req.getNgayLapBbanDenNgay());
		}
	}
}
