package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class BienBanBanGiaoMauRepositoryCustomImpl implements BienBanBanGiaoMauRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object[]> search(BienBanBanGiaoMauSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT bb, nx.id, nx.soQd, bbLayMau.id, bbLayMau.soBienBan FROM BienBanBanGiaoMau bb ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON bb.qdgnvnxId = nx.id ");
		builder.append("INNER JOIN BienBanLayMau bbLayMau ON bb.bbLayMauId = bbLayMau.id ");
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


	private void setConditionSearchCtkhn(BienBanBanGiaoMauSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getSoBienBan())) {
			builder.append("AND ").append("bb.soBienBan LIKE :soBienBan ");
		}

		if (!StringUtils.isEmpty(req.getSoQuyetDinhNhap())) {
			builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
		}
		if (req.getNgayBanGiaoMauTu() != null) {
			builder.append("AND ").append("bb.ngayBanGiaoMau >= :ngayBanGiaoMauTu ");
		}
		if (req.getNgayBanGiaoMauDen() != null) {
			builder.append("AND ").append("bb.ngayBanGiaoMau <= :ngayBanGiaoMauDen ");
		}

		if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
			builder.append("AND ").append("bb.maVatTuCha = :maVatTuCha ");
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			builder.append("AND ").append("bb.maDvi IN :maDvis ");
		}

		if (!CollectionUtils.isEmpty(req.getTrangThais())) {
			builder.append("AND ").append("bb.trangThai IN :trangThais ");
		}
	}

	@Override
	public int countBienBan(BienBanBanGiaoMauSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) FROM BienBanBanGiaoMau bb ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON bb.qdgnvnxId = nx.id ");
		builder.append("INNER JOIN BienBanLayMau bbLayMau ON bb.bbLayMauId = bbLayMau.id ");
		this.setConditionSearchCtkhn(req, builder);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearchCtkhn(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearchCtkhn(BienBanBanGiaoMauSearchReq req, Query query) {
		if (!StringUtils.isEmpty(req.getSoBienBan())) {
			query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
		}

		if (!StringUtils.isEmpty(req.getSoQuyetDinhNhap())) {
			query.setParameter("soQdNhap", "%" + req.getSoQuyetDinhNhap() + "%");
		}
		if (req.getNgayBanGiaoMauTu() != null) {
			query.setParameter("ngayBanGiaoMauTu", req.getNgayBanGiaoMauTu());
		}
		if (req.getNgayBanGiaoMauDen() != null) {
			query.setParameter("ngayBanGiaoMauDen", req.getNgayBanGiaoMauDen());
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
