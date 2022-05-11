package com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QlpktclhPhieuKtChatLuongRepositoryCustomImpl implements QlpktclhPhieuKtChatLuongRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DataUtils dataUtils;

	@Override
	public Page<QlpktclhPhieuKtChatLuongResponseDto> filter(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT qd FROM QlpktclhPhieuKtChatLuong qd ");
		setConditionFilter(req, builder);
		builder.append("ORDER BY qd.ngayKiemTra DESC");

		TypedQuery<QlpktclhPhieuKtChatLuong> query = em.createQuery(builder.toString(), QlpktclhPhieuKtChatLuong.class);

		//Set params
		this.setParameterFilter(req, query);

		//Set pageable
		query.setFirstResult(req.getPaggingReq().getPage() * req.getPaggingReq().getLimit()).setMaxResults(req.getPaggingReq().getLimit());

		List<QlpktclhPhieuKtChatLuong> data = query.getResultList();

		List<QlpktclhPhieuKtChatLuongResponseDto> responses = new ArrayList<>();
		for (QlpktclhPhieuKtChatLuong qd : data) {
			QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(qd, QlpktclhPhieuKtChatLuongResponseDto.class);
			response.setTenTrangThai(QdPheDuyetKqlcntVtStatus.getTenById(qd.getTrangThai()));
			responses.add(response);
		}

		int page = Optional.ofNullable(req.getPaggingReq()).map(PaggingReq::getPage).orElse(BaseRequest.DEFAULT_PAGE);
		int limit = Optional.ofNullable(req.getPaggingReq()).map(PaggingReq::getLimit).orElse(BaseRequest.DEFAULT_LIMIT);

		return new PageImpl<>(responses, PageRequest.of(page, limit), this.countPhieuKiemTraChatLuong(req));
	}


	private void setConditionFilter(QlpktclhPhieuKtChatLuongFilterRequestDto req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (req.getSoPhieu() != null) {
			builder.append("AND ").append("qd.soPhieu = :soPhieu ");
		}
		if (req.getNgayKiemTraTuNgay() != null) {
			builder.append("AND ").append("qd.ngayKiemTra >= :ngayKiemTraTuNgay ");
		}
		if (req.getNgayKiemTraDenNgay() != null) {
			builder.append("AND ").append("qd.ngayKiemTra <= :ngayKiemTraDenNgay ");
		}
		if (req.getMaNganKho() != null) {
			builder.append("AND ").append("qd.maNganKho = :maNganKho ");
		}
		if (req.getMaHangHoa() != null) {
			builder.append("AND ").append("qd.maHangHoa = :maHangHoa ");
		}

		if (req.getMaDonVi() != null) {
			builder.append("AND ").append("qd.maDonVi = :maDonVi ");
		}
	}

	private int countPhieuKiemTraChatLuong(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(qd.id) AS totalRecord FROM QlpktclhPhieuKtChatLuong qd ");

		this.setConditionFilter(req, builder);

		Query query = em.createQuery(builder.toString(), Tuple.class);

		this.setParameterFilter(req, query);

		List<?> dataCount = query.getResultList();

		if (CollectionUtils.isEmpty(dataCount)) {
			return total;
		}
		Tuple result = (Tuple) dataCount.get(0);
		return result.get(0, Long.class).intValue();
	}

	private void setParameterFilter(QlpktclhPhieuKtChatLuongFilterRequestDto req, Query query) {
		if (req.getSoPhieu() != null) {
			query.setParameter("soPhieu", req.getSoPhieu());
		}
		if (req.getNgayKiemTraTuNgay() != null) {
			query.setParameter("ngayKiemTraTuNgay", req.getNgayKiemTraTuNgay());
		}
		if (req.getNgayKiemTraDenNgay() != null) {
			query.setParameter("ngayKiemTraDenNgay", req.getNgayKiemTraDenNgay());
		}
		if (req.getMaNganKho() != null) {
			query.setParameter("maNganKho", req.getMaNganKho());
		}
		if (req.getMaHangHoa() != null) {
			query.setParameter("maHangHoa", req.getMaHangHoa());
		}

		if (req.getMaDonVi() != null) {
			query.setParameter("maDonVi", req.getMaDonVi());
		}
	}
}
