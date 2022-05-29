package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuKnghiemCluongHangRepositoryCustomImpl implements PhieuKnghiemCluongHangRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<PhieuKnghiemCluongHang> search(PhieuKnghiemCluongHangSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT phieu FROM PhieuKnghiemCluongHang phieu ");
		setConditionSearchCtkhn(req, builder);

		//Sort
		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			builder.append("ORDER BY ").append(sort.get()
					.map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
		}

		TypedQuery<PhieuKnghiemCluongHang> query = em.createQuery(builder.toString(), PhieuKnghiemCluongHang.class);

		//Set params
		this.setParameterSearchCtkhn(req, query);
		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		return query.getResultList();
	}


	private void setConditionSearchCtkhn(PhieuKnghiemCluongHangSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getMaDvi())) {
			builder.append("AND ").append("phieu.maHhoa = :maHhoa ");
		}

		if (!StringUtils.isEmpty(req.getSoPhieu())) {
			builder.append("AND ").append("phieu.soPhieu = :soPhieu ");
		}
		if (!StringUtils.isEmpty(req.getMaDvi())) {
			builder.append("AND ").append("phieu.maDvi = :maDvi ");
		}

		if (!StringUtils.isEmpty(req.getMaKho())) {
			builder.append("AND ").append("phieu.maKho = :maKho ");
		}

		if (!StringUtils.isEmpty(req.getMaNgan())) {
			builder.append("AND ").append("phieu.maNgan = :maNgan ");
		}

		if (!StringUtils.isEmpty(req.getMaLo())) {
			builder.append("AND ").append("phieu.maLo = :maLo ");
		}
		if (req.getNgayKnghiemTuNgay() != null) {
			builder.append("AND ").append("phieu.ngayKnghiem >= :ngayKnghiemTuNgay ");
		}

		if (req.getNgayKnghiemDenNgay() != null) {
			builder.append("AND ").append("phieu.ngayKnghiem <= :ngayKnghiemDenNgay ");
		}
	}

	@Override
	public int countCtkhn(PhieuKnghiemCluongHangSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) FROM PhieuKnghiemCluongHang phieu ");
		this.setConditionSearchCtkhn(req, builder);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearchCtkhn(req, query);
		return query.getSingleResult().intValue();
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
