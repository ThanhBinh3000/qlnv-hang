package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
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

public class PhieuKnghiemCluongHangRepositoryCustomImpl implements PhieuKnghiemCluongHangRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<PhieuKnghiemCluongHang> search(PhieuKnghiemCluongHangSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM PHIEU_KNGHIEM_CLUONG_HANG ");
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

		List<PhieuKnghiemCluongHang> response = data
				.stream()
				.map(res -> {
					Tuple item = (Tuple) res;
					PhieuKnghiemCluongHang phieu = new PhieuKnghiemCluongHang();
					phieu.setId(item.get("ID", Long.class));
					phieu.setSoPhieu(item.get("SO_PHIEU", String.class));
					phieu.setSluongBquan(item.get("SLUONG_BQUAN", BigDecimal.class));
					phieu.setTenHhoa(item.get("TEN_HHOA", String.class));
					phieu.setTenKho(item.get("TEN_KHO", String.class));
					phieu.setMaKho(item.get("MA_KHO", String.class));
					phieu.setTenNgan(item.get("TEN_NGAN", String.class));
					phieu.setMaNgan(item.get("MA_NGAN", String.class));
					phieu.setNgayKnghiem(item.get("NGAY_KNGHIEM", LocalDate.class));
					phieu.setSoBbanKthucNhap(item.get("SO_BBAN_KTHUC_NHAP", String.class));
					phieu.setNgayNhapDay(item.get("NGAY_NHAP_DAY", LocalDate.class));
					phieu.setHthucBquan(item.get("HTHUC_BQUAN", String.class));
					phieu.setDdiemBquan(item.get("DDIEM_BQUAN", String.class));
					phieu.setTrangThai(item.get("TRANG_THAI", String.class));

					return phieu;
				}).collect(Collectors.toList());

		return new PageImpl<>(response, pageable, this.countCtkhn(req));
	}


	private void setConditionSearchCtkhn(PhieuKnghiemCluongHangSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getMaDvi())) {
			builder.append("AND ").append("MA_HHOA = :maHhoa ");
		}

		if (!StringUtils.isEmpty(req.getSoPhieu())) {
			builder.append("AND ").append("SO_PHIEU = :soPhieu ");
		}
		if (!StringUtils.isEmpty(req.getMaDvi())) {
			builder.append("AND ").append("MA_DVI = :maDvi ");
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
		if (req.getNgayKnghiemTuNgay() != null) {
			builder.append("AND ").append("NGAY_KNGHIEM >= :ngayKnghiemTuNgay ");
		}

		if (req.getNgayKnghiemDenNgay() != null) {
			builder.append("AND ").append("NGAY_KNGHIEM <= :ngayKnghiemDenNgay ");
		}
	}

	private int countCtkhn(PhieuKnghiemCluongHangSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) AS totalRecord FROM PHIEU_KNGHIEM_CLUONG_HANG");

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

	private void setParameterSearchCtkhn(PhieuKnghiemCluongHangSearchReq req, Query query) {
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

		if (!StringUtils.isEmpty(req.getSoPhieu())) {
			query.setParameter("soPhieu", req.getSoPhieu());
		}

		if (!StringUtils.isEmpty(req.getMaDvi())) {
			query.setParameter("maDvi", req.getMaDvi());
		}

		if (req.getNgayKnghiemTuNgay() != null) {
			query.setParameter("ngayKnghiemTuNgay", req.getNgayKnghiemTuNgay());
		}

		if (req.getNgayKnghiemDenNgay() != null) {
			query.setParameter("ngayKnghiemDenNgay", req.getNgayKnghiemDenNgay());
		}
	}
}
