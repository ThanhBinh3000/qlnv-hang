package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class BienBanLayMauRepositoryCustomImpl implements BienBanLayMauRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object[]> search(BienBanLayMauSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT bb, nx.id, nx.soQd, hopDong.id, hopDong.soHd, nganLo, bbNhapDayKho.id, bbNhapDayKho.soBienBan FROM BienBanLayMau bb ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON bb.qdgnvnxId = nx.id ");
		builder.append("INNER JOIN HhHopDongHdr hopDong ON bb.hopDongId = hopDong.id ");
		builder.append("INNER JOIN QlBienBanNhapDayKhoLt bbNhapDayKho ON bb.bbNhapDayKhoId = bbNhapDayKho.id ");
		builder.append("LEFT JOIN KtNganLo nganLo ON bbNhapDayKho.maNganLo = nganLo.maNganlo ");
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


	private void setConditionSearchCtkhn(BienBanLayMauSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");


		if (!StringUtils.isEmpty(req.getSoBienBan())) {
			builder.append("AND ").append("bb.soBienBan LIKE :soBienBan ");
		}

		if (!StringUtils.isEmpty(req.getSoQuyetDinhNhap())) {
			builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
		}
		if (req.getNgayLayMauTu() != null) {
			builder.append("AND ").append("bb.ngayLayMau >= :ngayLayMauTu ");
		}
		if (req.getNgayLayMauDen() != null) {
			builder.append("AND ").append("bb.ngayLayMau <= :ngayLayMauDen ");
		}

		if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
			builder.append("AND ").append("bb.maVatTuCha <= :maVatTuCha ");
		}

		if (!StringUtils.isEmpty(req.getMaDvi())) {
			builder.append("AND ").append("bb.maDvi <= :maDvi ");
		}
	}

	@Override
	public int countBienBan(BienBanLayMauSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(DISTINCT bb.id) FROM BienBanLayMau bb ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON bb.qdgnvnxId = nx.id ");
		builder.append("INNER JOIN HhHopDongHdr hopDong ON bb.hopDongId = hopDong.id ");
		builder.append("LEFT JOIN KtNganLo nganLo ON bb.maNganLo = nganLo.maNganlo ");
		this.setConditionSearchCtkhn(req, builder);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearchCtkhn(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearchCtkhn(BienBanLayMauSearchReq req, Query query) {

		if (!StringUtils.isEmpty(req.getSoBienBan())) {
			query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
		}

		if (!StringUtils.isEmpty(req.getSoQuyetDinhNhap())) {
			query.setParameter("soQdNhap", "%" + req.getSoQuyetDinhNhap() + "%");
		}
		if (req.getNgayLayMauTu() != null) {
			query.setParameter("ngayLayMauTu", req.getNgayLayMauTu());
		}
		if (req.getNgayLayMauDen() != null) {
			query.setParameter("ngayLayMauDen", req.getNgayLayMauDen());
		}

		if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
			query.setParameter("maVatTuCha", req.getMaVatTuCha());
		}

		if (!StringUtils.isEmpty(req.getMaDvi())) {
			query.setParameter("maDvi", req.getMaDvi());
		}
	}
}
