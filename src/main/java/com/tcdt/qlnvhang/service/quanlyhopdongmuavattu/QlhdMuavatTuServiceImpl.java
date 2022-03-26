package com.tcdt.qlnvhang.service.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.*;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.*;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuRequestDTO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.*;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class QlhdMuavatTuServiceImpl implements QlhdMuavatTuService {
	private DataUtils dataUtils;

	private final QlhdMuaVatTuRepository qlhdMuaVatTuRepository;

	private final QlhdmvtDsGoiThauRepository qlhdmvtDsGoiThauRepository;

	private final QlhdmvtThongTinChungRepository qlhdmvtThongTinChungRepository;

	private final QlhdmvtDiaDiemNhapVtRepository qlhdmvtDiaDiemNhapVtRepository;

	private final QlhdmvtTtChuDauTuRepository qlhdmvtTtChuDauTuRepository;

	private final QlhdmvtTtDonViCcRepository qlhdmvtTtDonViCcRepository;

	private final QlhdmvtPhuLucHopDongRepository qlhdmvtPhuLucHopDongRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QlhdMuaVatTuResponseDTO create(QlhdMuaVatTuRequestDTO request) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		//Quản lý thông tin chủ đầu tư
		QlhdmvtTtChuDauTu thongTinChuDauTu = dataUtils.toObject(request.getThongTinChung().getThongTinChuDauTu(), QlhdmvtTtChuDauTu.class);
		thongTinChuDauTu.setNgayTao(LocalDate.now());
		thongTinChuDauTu.setNguoiTaoId(userInfo.getId());
		thongTinChuDauTu = qlhdmvtTtChuDauTuRepository.save(thongTinChuDauTu);


		//Thông tin đơn vị cung cấp
		QlhdmvtTtDonViCc thongTinDonViCungCap = dataUtils.toObject(request.getThongTinChung().getDonViCungCap(), QlhdmvtTtDonViCc.class);
		thongTinDonViCungCap.setNgayTao(LocalDate.now());
		thongTinDonViCungCap.setNguoiTaoId(userInfo.getId());
		thongTinDonViCungCap = qlhdmvtTtDonViCcRepository.save(thongTinDonViCungCap);

		//Thông tin chung bao gồm: thông tin chung, thông tin chủ đầu tư, thông tin đơn vị cung cấp
		QlhdmvtThongTinChung thongTinChung = dataUtils.toObject(request.getThongTinChung(), QlhdmvtThongTinChung.class);
		thongTinChung.setNgayTao(LocalDate.now());
		thongTinChung.setNguoiTaoId(userInfo.getId());
		thongTinChung.setChuDauTuId(thongTinChuDauTu.getId());
		thongTinChung.setDvCungCapId(thongTinDonViCungCap.getId());
		thongTinChung = qlhdmvtThongTinChungRepository.save(thongTinChung);

		//Quản lý hợp đồng mua vật tư: set thông tin chung
		QlhdMuaVatTu qlhdMuaVatTu = dataUtils.toObject(request, QlhdMuaVatTu.class);
		qlhdMuaVatTu.setThongTinChungId(thongTinChung.getId());


		//Danh sach goi thau
		List<QlhdmvtDsGoiThau> danhSachGoiThau = request.getDanhSachGoiThau()
				.stream()
				.map(item -> {
					QlhdmvtDsGoiThau goiThau = dataUtils.toObject(item, QlhdmvtDsGoiThau.class);
					goiThau.setNgayTao(LocalDate.now());
					goiThau.setNguoiTaoId(userInfo.getId());
					goiThau.setQlhdMuaVatTuId(qlhdMuaVatTu.getId());
					return goiThau;

				}).collect(Collectors.toList());

		//Phụ Lục quản lý hợp đồng mua vật tư
		List<QlhdmvtPhuLucHopDong> phuLucHopDongList = request.getPhuLucHopDongList()
				.stream()
				.map(item -> {
					QlhdmvtPhuLucHopDong phuLucHopDong = dataUtils.toObject(item, QlhdmvtPhuLucHopDong.class);
					phuLucHopDong.setNgayTao(LocalDate.now());
					phuLucHopDong.setNguoiTaoId(userInfo.getId());
					phuLucHopDong.setQlhdMuaVatTuId(qlhdMuaVatTu.getId());
					return phuLucHopDong;
				}).collect(Collectors.toList());

		//Quản lý hợp đồng mua vật tư: set danh sách gói thầu, danh sách phụ lục hợp đồng
		qlhdMuaVatTu.setPhuLucHopDongList(phuLucHopDongList);
		qlhdMuaVatTu.setDanhSachGoiThau(danhSachGoiThau);


		return buildQlhdMuaVatTuResponse(thongTinChuDauTu, thongTinDonViCungCap, thongTinChung, qlhdMuaVatTu);
	}

	private QlhdMuaVatTuResponseDTO buildQlhdMuaVatTuResponse(QlhdmvtTtChuDauTu ttChuDauTu,
															  QlhdmvtTtDonViCc ttDonViCc,
															  QlhdmvtThongTinChung thongTinChung,
															  QlhdMuaVatTu qlhdMuaVatTu) {
		//Thông tin chủ đầu tư
		QlhdmvtTtChuDauTuResponseDTO chuDauTuResponse = dataUtils.toObject(ttChuDauTu, QlhdmvtTtChuDauTuResponseDTO.class);

		//Thông đơn vị cung cấp
		QlhdmvtTtDonViCcResponseDTO donViCungCapResponse = dataUtils.toObject(ttDonViCc, QlhdmvtTtDonViCcResponseDTO.class);

		//Thông tin chung
		QlhdmvtThongTinChungResponseDTO thongTinChungResponse = dataUtils.toObject(thongTinChung, QlhdmvtThongTinChungResponseDTO.class);
		thongTinChungResponse.setDonViCungCap(donViCungCapResponse);
		thongTinChungResponse.setThongTinChuDauTu(chuDauTuResponse);

		//Danh sách gói thầu
		List<QlhdmvtDsGoiThauResponseDTO> danhSachGoiThauResponse = qlhdMuaVatTu.getDanhSachGoiThau()
				.stream()
				.map(item -> dataUtils.toObject(item, QlhdmvtDsGoiThauResponseDTO.class)).collect(Collectors.toList());

		//Danh sách phụ lục hợp đồng
		List<QlhdmvtPhuLucHopDongResponseDTO> phuLucHopDongResponseList = qlhdMuaVatTu.getPhuLucHopDongList()
				.stream()
				.map(item -> dataUtils.toObject(item, QlhdmvtPhuLucHopDongResponseDTO.class)).collect(Collectors.toList());

		//Quản lý hợp đồng mua vật tư
		QlhdMuaVatTuResponseDTO qlhdMuaVatTuResponse = dataUtils.toObject(qlhdMuaVatTu, QlhdMuaVatTuResponseDTO.class);
		qlhdMuaVatTuResponse.setDanhSachGoiThau(danhSachGoiThauResponse);
		qlhdMuaVatTuResponse.setThongTinChung(thongTinChungResponse);
		qlhdMuaVatTuResponse.setPhuLucHopDongList(phuLucHopDongResponseList);

		return qlhdMuaVatTuResponse;
	}
}
