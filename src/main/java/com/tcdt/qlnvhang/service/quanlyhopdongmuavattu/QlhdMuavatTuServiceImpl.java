package com.tcdt.qlnvhang.service.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.*;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.*;
import com.tcdt.qlnvhang.request.hopdongmuavattu.QlhdMuaVatTuRequestDTO;
import com.tcdt.qlnvhang.request.hopdongmuavattu.QlhdmvtFilterRequest;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.*;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
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

	private final QlnvDmDonviRepository qlnvDmDonviRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QlhdMuaVatTuResponseDTO create(QlhdMuaVatTuRequestDTO request) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		Optional<QlnvDmDonvi> donViOpt = qlnvDmDonviRepository.findById(request.getDonViId());

		if (!donViOpt.isPresent()) {
			throw new Exception("Đơn vị không tồn tại");
		}
		QlnvDmDonvi donvi = donViOpt.get();


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


		//Danh sach goi thau/ địa điểm nhập
		List<QlhdmvtDsGoiThau> dsGoiThauList = new ArrayList<>();
		for (QlhdmvtDsGoiThauResponseDTO dsGoiThauRequest : request.getDanhSachGoiThau()) {
			QlhdmvtDsGoiThau goiThau = dataUtils.toObject(dsGoiThauRequest, QlhdmvtDsGoiThau.class);
			goiThau.setNgayTao(LocalDate.now());
			goiThau.setNguoiTaoId(userInfo.getId());
			goiThau.setQlhdMuaVatTuId(qlhdMuaVatTu.getId());
			goiThau = qlhdmvtDsGoiThauRepository.save(goiThau);

			//Địa điểm nhập vật tư
			Long goiThauId = goiThau.getId();

			List<QlhdmvtDiaDiemNhapVt> diaDiemNhapVtList = dsGoiThauRequest.getDiaDiemNhapVtList()
					.stream()
					.map(item -> {
						QlhdmvtDiaDiemNhapVt diaDiemNhapVt = dataUtils.toObject(item, QlhdmvtDiaDiemNhapVt.class);
						diaDiemNhapVt.setQlhdmvtDsGoiThauId(goiThauId);
						return diaDiemNhapVt;
					}).collect(Collectors.toList());

			diaDiemNhapVtList = qlhdmvtDiaDiemNhapVtRepository.saveAll(diaDiemNhapVtList);

			goiThau.setDiaDiemNhapVtList(diaDiemNhapVtList);

			dsGoiThauList.add(goiThau);
		}

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

		phuLucHopDongList = qlhdmvtPhuLucHopDongRepository.saveAll(phuLucHopDongList);

		//Quản lý hợp đồng mua vật tư: set danh sách gói thầu, danh sách phụ lục hợp đồng
		qlhdMuaVatTu.setPhuLucHopDongList(phuLucHopDongList);
		qlhdMuaVatTu.setDanhSachGoiThau(dsGoiThauList);
		qlhdMuaVatTu.setMaDonVi(donvi.getMaDvi());
		qlhdMuaVatTu.setCapDonVi(donvi.getCapDvi());


		return buildQlhdMuaVatTuResponse(thongTinChuDauTu, thongTinDonViCungCap, thongTinChung, qlhdMuaVatTu);
	}

	private QlhdMuaVatTuResponseDTO buildQlhdMuaVatTuResponse(QlhdmvtTtChuDauTu ttChuDauTu,
															  QlhdmvtTtDonViCc ttDonViCc,
															  QlhdmvtThongTinChung thongTinChung,
															  QlhdMuaVatTu qlhdMuaVatTu) throws Exception {
		//Thông tin chủ đầu tư
		QlhdmvtTtChuDauTuResponseDTO chuDauTuResponse = dataUtils.toObject(ttChuDauTu, QlhdmvtTtChuDauTuResponseDTO.class);

		//Thông đơn vị cung cấp
		QlhdmvtTtDonViCcResponseDTO donViCungCapResponse = dataUtils.toObject(ttDonViCc, QlhdmvtTtDonViCcResponseDTO.class);

		//Thông tin chung
		QlhdmvtThongTinChungResponseDTO thongTinChungResponse = dataUtils.toObject(thongTinChung, QlhdmvtThongTinChungResponseDTO.class);
		thongTinChungResponse.setDonViCungCap(donViCungCapResponse);
		thongTinChungResponse.setThongTinChuDauTu(chuDauTuResponse);

		//Danh sách gói thầu
		List<QlhdmvtDsGoiThauResponseDTO> danhSachGoiThauResponse = new ArrayList<>();
		for (QlhdmvtDsGoiThau goiThau : qlhdMuaVatTu.getDanhSachGoiThau()) {
			QlhdmvtDsGoiThauResponseDTO goiThauRes = dataUtils.toObject(goiThau, QlhdmvtDsGoiThauResponseDTO.class);
			//Địa điểm nhập vật tư
			List<QlhdmvtDiaDiemNhapVtResponseDTO> diaDiemNhapVatTuRes =
					dataUtils.toListObject(goiThau.getDiaDiemNhapVtList(), QlhdmvtDiaDiemNhapVtResponseDTO.class);

			goiThauRes.setDiaDiemNhapVtList(diaDiemNhapVatTuRes);
			danhSachGoiThauResponse.add(goiThauRes);
		}

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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QlhdMuaVatTuResponseDTO update(QlhdMuaVatTuRequestDTO request) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		//Thông tin chủ đầu tư
		dataUtils.validateExits(qlhdmvtTtChuDauTuRepository, request.getThongTinChung().getThongTinChuDauTu().getId(), false);

		QlhdmvtTtChuDauTu chuDauTu = dataUtils.toObject(request.getThongTinChung().getThongTinChuDauTu(), QlhdmvtTtChuDauTu.class);
		chuDauTu = qlhdmvtTtChuDauTuRepository.save(chuDauTu);

		//Đơn vị cung cấp
		dataUtils.validateExits(qlhdmvtTtDonViCcRepository, request.getThongTinChung().getDonViCungCap().getId(), false);
		QlhdmvtTtDonViCc donViCc = dataUtils.toObject(request.getThongTinChung().getDonViCungCap().getId(), QlhdmvtTtDonViCc.class);

		//Update thông tin chung
		dataUtils.validateExits(qlhdmvtThongTinChungRepository, request.getThongTinChung().getId(), false);

		QlhdmvtThongTinChung thongTinChung = dataUtils.toObject(request.getThongTinChung(), QlhdmvtThongTinChung.class);
		thongTinChung.setNgaySua(LocalDate.now());
		thongTinChung.setNguoiSuaId(userInfo.getId());
		thongTinChung = qlhdmvtThongTinChungRepository.save(thongTinChung);

		//Update danh sách gói thầu
		Set<Long> goiThauIds = request.getDanhSachGoiThau()
				.stream()
				.map(QlhdmvtDsGoiThauResponseDTO::getId).collect(Collectors.toSet());

		List<QlhdmvtDsGoiThau> dsGoiThauList = dataUtils.findAllByIds(qlhdmvtDsGoiThauRepository, goiThauIds);

		Map<Long, QlhdmvtDsGoiThau> dsGoiThauMap = dsGoiThauList.stream()
				.collect(Collectors.toMap(QlhdmvtDsGoiThau::getId, Function.identity(), (o1, o2) -> o1));

		List<QlhdmvtDsGoiThau> goiThauList = new ArrayList<>();

		for (QlhdmvtDsGoiThauResponseDTO goiThauRequest : request.getDanhSachGoiThau()) {
			if (dsGoiThauMap.get(goiThauRequest.getId()) != null) continue;
			QlhdmvtDsGoiThau dsGoiThau = dataUtils.toObject(goiThauRequest, QlhdmvtDsGoiThau.class);
			dsGoiThau.setNgaySua(LocalDate.now());
			dsGoiThau.setNguoiSuaId(userInfo.getId());
			dsGoiThau = qlhdmvtDsGoiThauRepository.save(dsGoiThau);

			//Đia điểm nhập vật tư
			List<QlhdmvtDiaDiemNhapVt> diaDiemNhapVatTu =
					dataUtils.toListObject(goiThauRequest.getDiaDiemNhapVtList(), QlhdmvtDiaDiemNhapVt.class);

			diaDiemNhapVatTu = qlhdmvtDiaDiemNhapVtRepository.saveAll(diaDiemNhapVatTu);

			dsGoiThau.setDiaDiemNhapVtList(diaDiemNhapVatTu);
		}

		//Update phụ lục
		Set<Long> phuLucIds = request.getPhuLucHopDongList()
				.stream()
				.map(QlhdmvtPhuLucHopDongResponseDTO::getId).collect(Collectors.toSet());

		List<QlhdmvtPhuLucHopDong> dsPhuLucList = dataUtils.findAllByIds(qlhdmvtPhuLucHopDongRepository, phuLucIds);

		Map<Long, QlhdmvtPhuLucHopDong> dsPhuLucMap = dsPhuLucList.stream()
				.collect(Collectors.toMap(QlhdmvtPhuLucHopDong::getId, Function.identity(), (o1, o2) -> o1));


		//save danh sách phụ lục và convert to DTO
		List<QlhdmvtPhuLucHopDong> phuLucList = request.getPhuLucHopDongList()
				.stream()
				.filter(item -> dsPhuLucMap.get(item.getId()) != null)
				.map(item -> {
					QlhdmvtPhuLucHopDong phuLucHopDong = dataUtils.toObject(item, QlhdmvtPhuLucHopDong.class);
					phuLucHopDong.setNgaySua(LocalDate.now());
					phuLucHopDong.setNguoiSuaId(userInfo.getId());
					phuLucHopDong = qlhdmvtPhuLucHopDongRepository.save(phuLucHopDong);
					return phuLucHopDong;
				}).collect(Collectors.toList());

		//Update quản lý hợp đồng mua vật tư
		dataUtils.validateExits(qlhdMuaVatTuRepository, request.getId(), false);

		QlhdMuaVatTu qlhdMuaVatTu = dataUtils.toObject(request, QlhdMuaVatTu.class);
		qlhdMuaVatTu.setNgaySua(LocalDate.now());
		qlhdMuaVatTu.setNguoiSuaId(userInfo.getId());
		qlhdMuaVatTu = qlhdMuaVatTuRepository.save(qlhdMuaVatTu);

		qlhdMuaVatTu.setPhuLucHopDongList(phuLucList);
		qlhdMuaVatTu.setDanhSachGoiThau(goiThauList);

		return buildQlhdMuaVatTuResponse(chuDauTu, donViCc, thongTinChung, qlhdMuaVatTu);
	}

	@Override
	public Page<QlhdMuaVatTuResponseDTO> filter(QlhdmvtFilterRequest request) {
		return qlhdMuaVatTuRepository.filter(request);
	}
}
