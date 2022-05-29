package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
	public List<BienBanBanGiaoMau> search(BienBanBanGiaoMauSearchReq req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT bb FROM BienBanBanGiaoMau bb ");
		setConditionSearchCtkhn(req, builder);

		//Sort
		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			builder.append("ORDER BY ").append(sort.get()
					.map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
		}

		TypedQuery<BienBanBanGiaoMau> query = em.createQuery(builder.toString(), BienBanBanGiaoMau.class);

		//Set params
		this.setParameterSearchCtkhn(req, query);
		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		return query.getResultList();
	}


	private void setConditionSearchCtkhn(BienBanBanGiaoMauSearchReq req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (!StringUtils.isEmpty(req.getMaHhoa())) {
			builder.append("AND ").append("bb.maHhoa = :maHhoa ");
		}

		if (!StringUtils.isEmpty(req.getMaKho())) {
			builder.append("AND ").append("bb.maKho = :maKho ");
		}

		if (!StringUtils.isEmpty(req.getMaNgan())) {
			builder.append("AND ").append("bb.maNgan = :maNgan ");
		}

		if (!StringUtils.isEmpty(req.getMaLo())) {
			builder.append("AND ").append("bb.maLo = :maLo ");
		}
		if (!StringUtils.isEmpty(req.getSoQdinhGiaoNvuNhap())) {
			builder.append("AND ").append("bb.soQdinhGiaoNvuNhap = :soQdinhGiaoNvuNhap ");
		}
		if (req.getNgayLapBbanTuNgay() != null) {
			builder.append("AND ").append("bb.ngayLapBban >= :ngayLapBbanTuNgay ");
		}
		if (req.getNgayLapBbanDenNgay() != null) {
			builder.append("AND ").append("bb.ngayLapBban <= :ngayLapBbanDenNgay ");
		}
	}

	@Override
	public int countBienBan(BienBanBanGiaoMauSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) FROM BienBanBanGiaoMau bb ");

		this.setConditionSearchCtkhn(req, builder);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearchCtkhn(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearchCtkhn(BienBanBanGiaoMauSearchReq req, Query query) {
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
