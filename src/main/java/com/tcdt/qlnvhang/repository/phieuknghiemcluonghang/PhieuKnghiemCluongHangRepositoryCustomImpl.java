package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuKnghiemCluongHangRepositoryCustomImpl implements PhieuKnghiemCluongHangRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object[]> search(PhieuKnghiemCluongHangSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT phieu, nganLo, nx.id, nx.soQd, bbBanGiao.id, bbBanGiao.soBienBan FROM PhieuKnghiemCluongHang phieu ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON phieu.qdgnvnxId = nx.id ");
		builder.append("INNER JOIN BienBanBanGiaoMau bbBanGiao ON phieu.bbBanGiaoMauId = bbBanGiao.id ");
		builder.append("LEFT JOIN KtNganLo nganLo ON phieu.maNganLo = nganLo.maNganlo ");
		setConditionSearchCtkhn(req, builder);

		//Sort
		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			builder.append("ORDER BY ").append(sort.get()
					.map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
		}

		TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

		//Set params
		this.setParameterSearchCtkhn(req, query);
		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		return query.getResultList();
	}


	private void setConditionSearchCtkhn(PhieuKnghiemCluongHangSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getSoBbBanGiao())) {
			builder.append("AND ").append("bbBanGiao.soBienBan LIKE :soBbBanGiao ");
		}
		if (!StringUtils.isEmpty(req.getSoPhieu())) {
			builder.append("AND ").append("phieu.soPhieu LIKE :soPhieu ");
		}

		if (req.getNgayKnghiemTu() != null) {
			builder.append("AND ").append("phieu.ngayKnghiem >= :ngayKnghiemTu ");
		}

		if (req.getNgayKnghiemDen() != null) {
			builder.append("AND ").append("phieu.ngayKnghiem <= :ngayKnghiemDen ");
		}

		if (req.getNgayLayMauTu() != null) {
			builder.append("AND ").append("phieu.ngayLayMau >= :ngayLayMauTu ");
		}

		if (req.getNgayLayMauDen() != null) {
			builder.append("AND ").append("phieu.ngayLayMau <= :ngayLayMauDen ");
		}

		if (!StringUtils.isEmpty(req.getSoQdNhap())) {
			builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
		}

		if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
			builder.append("AND ").append("phieu.maVatTuCha = :maVatTuCha ");
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			builder.append("AND ").append("phieu.maDvi IN :maDvis ");
		}

		if (!CollectionUtils.isEmpty(req.getTrangThais())) {
			builder.append("AND ").append("phieu.trangThai IN :trangThais ");
		}
	}

	@Override
	public int countCtkhn(PhieuKnghiemCluongHangSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) FROM PhieuKnghiemCluongHang phieu ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON phieu.qdgnvnxId = nx.id ");
		builder.append("INNER JOIN BienBanBanGiaoMau bbBanGiao ON phieu.bbBanGiaoMauId = bbBanGiao.id ");
		builder.append("LEFT JOIN KtNganLo nganLo ON phieu.maNganLo = nganLo.maNganlo ");
		this.setConditionSearchCtkhn(req, builder);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearchCtkhn(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearchCtkhn(PhieuKnghiemCluongHangSearchReq req, Query query) {

		if (!StringUtils.isEmpty(req.getSoBbBanGiao())) {
			query.setParameter("soBbBanGiao", "%" + req.getSoBbBanGiao() + "%");
		}

		if (!StringUtils.isEmpty(req.getSoPhieu())) {
			query.setParameter("soPhieu", "%" + req.getSoPhieu() + "%");
		}

		if (req.getNgayKnghiemTu() != null) {
			query.setParameter("ngayKnghiemTu", req.getNgayKnghiemTu());
		}

		if (req.getNgayKnghiemDen() != null) {
			query.setParameter("ngayKnghiemDen", req.getNgayKnghiemDen());
		}

		if (req.getNgayLayMauTu() != null) {
			query.setParameter("ngayLayMauTu", req.getNgayLayMauTu());
		}

		if (req.getNgayLayMauDen() != null) {
			query.setParameter("ngayLayMauDen", req.getNgayLayMauDen());
		}

		if (!StringUtils.isEmpty(req.getSoQdNhap())) {
			query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
		}

		if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
			query.setParameter("maVatTuCha", req.getMaVatTuCha());
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			query.setParameter("maDvis", req.getMaDvis());
		}

		if (!CollectionUtils.isEmpty(req.getTrangThais())) {
			query.setParameter("trangThais", req.getTrangThais());
		}
	}
}
