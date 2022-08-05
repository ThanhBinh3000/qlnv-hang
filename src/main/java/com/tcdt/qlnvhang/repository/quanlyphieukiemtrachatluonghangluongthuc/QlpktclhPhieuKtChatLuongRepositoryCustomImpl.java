package com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
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
import org.springframework.util.StringUtils;

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
		builder.append("SELECT qd, nx.id, nx.soQd FROM QlpktclhPhieuKtChatLuong qd ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON qd.quyetDinhNhapId = nx.id ");
		setConditionFilter(req, builder);
		builder.append("ORDER BY qd.ngayKiemTra DESC");

		TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

		//Set params
		this.setParameterFilter(req, query);

		//Set pageable
		query.setFirstResult(req.getPaggingReq().getPage() * req.getPaggingReq().getLimit()).setMaxResults(req.getPaggingReq().getLimit());

		List<Object[]> data = query.getResultList();

		List<QlpktclhPhieuKtChatLuongResponseDto> responses = new ArrayList<>();
		for (Object[] o : data) {
			QlpktclhPhieuKtChatLuong qd = (QlpktclhPhieuKtChatLuong) o[0];
			Long quyetDinhNhapId = (Long) o[1];
			String soQdNhap = (String) o[2];
			QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(qd, QlpktclhPhieuKtChatLuongResponseDto.class);
			response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qd.getTrangThai()));
			response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(qd.getTrangThai()));
			response.setQuyetDinhNhapId(quyetDinhNhapId);
			response.setSoQuyetDinhNhap(soQdNhap);
			response.setMaDiemKho(qd.getMaDiemKho());
			response.setMaNhaKho(qd.getMaNhaKho());
			response.setMaNganKho(qd.getMaNganKho());
			response.setMaNganLo(qd.getMaNganLo());
			responses.add(response);
		}

		int page = Optional.ofNullable(req.getPaggingReq()).map(PaggingReq::getPage).orElse(BaseRequest.DEFAULT_PAGE);
		int limit = Optional.ofNullable(req.getPaggingReq()).map(PaggingReq::getLimit).orElse(BaseRequest.DEFAULT_LIMIT);

		return new PageImpl<>(responses, PageRequest.of(page, limit), this.countPhieuKiemTraChatLuong(req));
	}


	private void setConditionFilter(QlpktclhPhieuKtChatLuongFilterRequestDto req, StringBuilder builder) {
		builder.append("WHERE 1 = 1 ");

		if (StringUtils.hasText(req.getSoQd())) {
			builder.append("AND ").append("LOWER(nx.soQd) LIKE :soQd ");
		}
		if (StringUtils.hasText(req.getSoPhieu())) {
			builder.append("AND ").append("LOWER(qd.soPhieu) LIKE :soPhieu ");
		}
		if (req.getNgayKiemTraTuNgay() != null) {
			builder.append("AND ").append("qd.ngayKiemTra >= :ngayKiemTraTuNgay ");
		}
		if (req.getNgayKiemTraDenNgay() != null) {
			builder.append("AND ").append("qd.ngayKiemTra <= :ngayKiemTraDenNgay ");
		}
		if (StringUtils.hasText(req.getMaNganKho())) {
			builder.append("AND ").append("qd.maNganKho = :maNganKho ");
		}
		if (StringUtils.hasText(req.getMaVatTu())) {
			builder.append("AND ").append("qd.maVatTu = :maVatTu ");
		}

		if (StringUtils.hasText(req.getLoaiVthh())) {
			builder.append("AND ").append("qd.loaiVthh = :loaiVthh ");
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			builder.append("AND ").append("qd.maDvi IN :maDvis ");
		}

		if (!CollectionUtils.isEmpty(req.getTrangThais())) {
			builder.append("AND ").append("qd.trangThai IN :trangThais ");
		}

		if (StringUtils.hasText(req.getKqDanhGia())) {
			builder.append("AND ").append("LOWER(qd.kqDanhGia) LIKE :kqDanhGia ");
		}
	}

	@Override
	public int countPhieuKiemTraChatLuong(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		int total = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(DISTINCT qd.id) AS totalRecord FROM QlpktclhPhieuKtChatLuong qd ");
		builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON qd.quyetDinhNhapId = nx.id ");

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

		if (StringUtils.hasText(req.getSoQd())) {
			query.setParameter("soQd", "%" + req.getSoQd().toLowerCase() + "%");
		}

		if (StringUtils.hasText(req.getSoPhieu())) {
			query.setParameter("soPhieu", "%" + req.getSoPhieu().toLowerCase() + "%");
		}
		if (req.getNgayKiemTraTuNgay() != null) {
			query.setParameter("ngayKiemTraTuNgay", req.getNgayKiemTraTuNgay());
		}
		if (req.getNgayKiemTraDenNgay() != null) {
			query.setParameter("ngayKiemTraDenNgay", req.getNgayKiemTraDenNgay());
		}
		if (StringUtils.hasText(req.getMaNganKho())) {
			query.setParameter("maNganKho", req.getMaNganKho());
		}
		if (StringUtils.hasText(req.getMaVatTu())) {
			query.setParameter("maVatTu", req.getMaVatTu());
		}

		if (StringUtils.hasText(req.getLoaiVthh())) {
			query.setParameter("loaiVthh", req.getLoaiVthh());
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			query.setParameter("maDvis", req.getMaDvis());
		}

		if (!CollectionUtils.isEmpty(req.getTrangThais())) {
			query.setParameter("trangThais", req.getTrangThais());
		}

		if (StringUtils.hasText(req.getKqDanhGia())) {
			query.setParameter("kqDanhGia", "%" + req.getKqDanhGia().toLowerCase() + "%");
		}
	}
}
