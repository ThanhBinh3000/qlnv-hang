package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdMuaVatTu;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtFilterRequest;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdMuaVatTuResponseDTO;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class QlhdMuaVatTuRepositoryCustomImpl implements QlhdMuaVatTuRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DataUtils dataUtils;

	@Override
	public Page<QlhdMuaVatTuResponseDTO> filter(QlhdmvtFilterRequest req) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT qd FROM QlhdMuaVatTu qd ");
		builder.append("INNER JOIN QlhdmvtThongTinChung ttc ON ttc.id = qd.thongTinChungId ");

		builder.append("INNER JOIN QlhdmvtTtDonViCc dvcc ON dvcc.id = ttc.dvCungCapId ");

		setConditionFilter(req, builder);
//		builder.append("ORDER BY qd.ngayQuyetDinh DESC");

		TypedQuery<QlhdMuaVatTu> query = em.createQuery(builder.toString(), QlhdMuaVatTu.class);

		//Set params
		this.setParameterFilter(req, query);

		Pageable pageable = req.getPageable();
		//Set pageable
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		List<QlhdMuaVatTu> data = query.getResultList();

		List<QlhdMuaVatTuResponseDTO> responses = new ArrayList<>();
		for (QlhdMuaVatTu qd : data) {
			QlhdMuaVatTuResponseDTO response = dataUtils.toObject(qd, QlhdMuaVatTuResponseDTO.class);
			response.setTenTrangThai(QdPheDuyetKqlcntVtStatus.getTenById(qd.getTrangThai()));
			responses.add(response);
		}

		return new PageImpl<>(responses, pageable, this.countPhieuKiemTraChatLuong(req));
	}


	private void setConditionFilter(QlhdmvtFilterRequest req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (req.getSoHopDong() != null) {
			builder.append("AND ").append("qd.soHopDong = :soHopDong ");
		}
		if (req.getHopDongKyTuNgay() != null) {
			builder.append("AND ").append("qd.ngayKy >= :hopDongKyTuNgay ");
		}
		if (req.getDenNgay() != null) {
			builder.append("AND ").append("qd.ngayKy <= :denNgay ");
		}
		if (req.getLoaiHangId() != null) {
			builder.append("AND ").append("ttc.loaiHangId = :loaiHangId ");
		}
		if (req.getCanCuId() != null) {
			builder.append("AND ").append("qd.canCuId = :canCuId ");
		}

		if (req.getBenBId() != null) {
			builder.append("AND ").append("dvcc.id = :benBId ");
		}
	}

	private int countPhieuKiemTraChatLuong(QlhdmvtFilterRequest req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(qd.id) AS totalRecord FROM QlpktclhPhieuKtChatLuong qd");

		this.setConditionFilter(req, builder);

		Query query = em.createNativeQuery(builder.toString(), Tuple.class);

		this.setParameterFilter(req, query);

		List<?> dataCount = query.getResultList();

		if (!CollectionUtils.isEmpty(dataCount)) {
			return total;
		}
		Tuple result = (Tuple) dataCount.get(0);
		return result.get("totalRecord", BigInteger.class).intValue();
	}

	private void setParameterFilter(QlhdmvtFilterRequest req, Query query) {
		if (req.getSoHopDong() != null) {
			query.setParameter("soHopDong", req.getSoHopDong());
		}
		if (req.getHopDongKyTuNgay() != null) {
			query.setParameter("hopDongKyTuNgay", req.getHopDongKyTuNgay());
		}
		if (req.getDenNgay() != null) {
			query.setParameter("denNgay", req.getDenNgay());
		}
		if (req.getLoaiHangId() != null) {
			query.setParameter("loaiHangId", req.getLoaiHangId());
		}
		if (req.getCanCuId() != null) {
			query.setParameter("canCuId", req.getCanCuId());
		}

		if (req.getBenBId() != null) {
			query.setParameter("benBId", req.getBenBId());
		}
	}
}
