package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.kehoachluachonnhathau.KhLuaChonNhaThau;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class KhLuaChonNhaThauRepositoryCustomImpl implements KhLuaChonNhaThauRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<KhLuaChonNhaThau> search(KhLuaChonNhaThauSearchReq req) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM KH_LCNT_VT WHERE id > 0 ");

		setSearchCondition(req, builder);
		PaggingReq paggingReq = req.getPaggingReq();
		Pageable pageable;
		if (paggingReq == null) {
			pageable = PageRequest.of(0, 10);
		} else {
			pageable = PageRequest.of(paggingReq.getPage(), paggingReq.getLimit());
		}

		//Sort
		Sort sort = Sort.by(Sort.Direction.ASC, "stt");
		if (sort.isSorted()) {
			builder.append("ORDER BY ").append(sort.get()
					.map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
		}

		Query query = em.createNativeQuery(builder.toString(), Tuple.class);

		//Set params
		this.setParameters(req, query);

		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		List<?> data = query.getResultList();

		List<KhLuaChonNhaThau> response = data
				.stream()
				.map(res -> {
					Tuple item = (Tuple) res;
					KhLuaChonNhaThau kh = new KhLuaChonNhaThau();
					kh.setId(item.get("id", BigDecimal.class).longValue());
					kh.setSoQdinh(item.get("soQdinh", String.class));
					kh.setNamKhoach(item.get("namKeHoach", Integer.class));
					kh.setMaVatTu(item.get("maVatTu", String.class));
					kh.setTenVatTu(item.get("tenVatTu", String.class));
					kh.setTenChuDtu(item.get("tenChuDtu", String.class));
					kh.setTenDuAn(item.get("tenDuAn", String.class));
					kh.setDonViTien(item.get("donViTien", String.class));
					kh.setDienGiai(item.get("dienGiai", String.class));
					kh.setHthucLcnt(item.get("hthucLcnt", String.class));
					kh.setNguonVon(item.get("nguonVon", String.class));
					kh.setQchuanKthuat(item.get("qchuanKthuat", String.class));
					kh.setTgianThienDuAn(item.get("tgianThienDuAn", String.class));
					kh.setTongMucDtu(item.get("tongMucDtu", Double.class));
					kh.setNguoiTaoId(item.get("nguoiTaoId", Long.class));
					kh.setNgayTao(item.get("ngayTao", LocalDate.class));
					kh.setTrangThai(item.get("trangThai", String.class));
					kh.setLdoTchoi(item.get("ldoTchoi", String.class));
					kh.setNgayGuiDuyet(item.get("ngayGuiDuyet", LocalDate.class));
					kh.setNguoiGuiDuyetId(item.get("nguoiGuiDuyetId", Long.class));
					kh.setNguoiPduyetId(item.get("nguoiPduyetId", Long.class));
					kh.setNgayPduyet(item.get("ngayPduyet", LocalDate.class));
					return kh;
				}).collect(Collectors.toList());


		return new PageImpl<>(response, pageable, this.count(req));
	}


	private void setSearchCondition(KhLuaChonNhaThauSearchReq req, StringBuilder builder) {
		if (!StringUtils.isEmpty(req.getSoQdinh())) {
			builder.append("AND ").append("so_qdinh = :soQdinh ");
		}
		if (req.getTuNgay() != null) {
			builder.append("AND ").append("ngay_tao >= :tuNgay ");
		}
		if (req.getDenNgay() != null) {
			builder.append("AND ").append("ngay_tao <= :denNgay ");
		}

		if (!StringUtils.isEmpty(req.getNoiDung())) {
			builder.append("AND ").append("dien_giai like :dienGiai ");
		}
	}

	private long count(KhLuaChonNhaThauSearchReq req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) AS totalRecord FROM KH_LCNT_VT WHERE id > 0 ");

		this.setSearchCondition(req, builder);

		Query query = em.createNativeQuery(builder.toString(), Tuple.class);

		this.setParameters(req, query);

		List<?> dataCount = query.getResultList();

		if (!CollectionUtils.isEmpty(dataCount)) {
			return total;
		}
		Tuple result = (Tuple) dataCount.get(0);
		return result.get("totalRecord", Long.class).intValue();
	}

	private void setParameters(KhLuaChonNhaThauSearchReq req, Query query) {
		if (!StringUtils.isEmpty(req.getSoQdinh())) {
			query.setParameter("soQdinh", req.getSoQdinh());
		}

		if (req.getTuNgay() != null) {
			query.setParameter("tuNgay", req.getTuNgay());
		}

		if (req.getDenNgay() != null) {
			query.setParameter("denNgay", req.getDenNgay());
		}

		if (!StringUtils.isEmpty(req.getNoiDung())) {
			query.setParameter("dienGiai", "%" + req.getNoiDung() + "%");
		}
	}
}
