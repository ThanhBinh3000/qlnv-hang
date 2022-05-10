package com.tcdt.qlnvhang.service.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTra;
import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTraRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.request.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTraResponseDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class QlpktclhPhieuKtChatLuongServiceImpl implements QlpktclhPhieuKtChatLuongService {
	private final QlpktclhPhieuKtChatLuongRepository qlpktclhPhieuKtChatLuongRepo;
	private final QlpktclhKetQuaKiemTraRepository qlpktclhKetQuaKiemTraRepo;
	private final DataUtils dataUtils;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public QlpktclhPhieuKtChatLuongResponseDto create(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		//Quản lý thông tin chủ đầu tư
		QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong = dataUtils.toObject(req, QlpktclhPhieuKtChatLuong.class);
		qlpktclhPhieuKtChatLuong.setNgayTao(LocalDate.now());
		qlpktclhPhieuKtChatLuong.setNguoiTaoId(userInfo.getId());
		qlpktclhPhieuKtChatLuong.setMaDonVi(userInfo.getDvql());
		qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongRepo.save(qlpktclhPhieuKtChatLuong);

		QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(qlpktclhPhieuKtChatLuong, QlpktclhPhieuKtChatLuongResponseDto.class);

		if (CollectionUtils.isEmpty(req.getKetQuaKiemTra())) return response;

		//Kết quả kiểm tra
		Long phieuKiemTraChatLuongId = qlpktclhPhieuKtChatLuong.getId();
		List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTraRes =
				req.getKetQuaKiemTra().stream()
						.map(item -> {
							QlpktclhKetQuaKiemTra ketQuaKiemTra = dataUtils.toObject(item, QlpktclhKetQuaKiemTra.class);
							ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
							ketQuaKiemTra = qlpktclhKetQuaKiemTraRepo.save(ketQuaKiemTra);

							return dataUtils.toObject(ketQuaKiemTra, QlpktclhKetQuaKiemTraResponseDto.class);
						}).collect(Collectors.toList());

		response.setKetQuaKiemTra(ketQuaKiemTraRes);

		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QlpktclhPhieuKtChatLuongResponseDto update(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (req.getId() == null) throw new Exception("Id is required in update");

		Optional<QlpktclhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongOpt = qlpktclhPhieuKtChatLuongRepo.findById(req.getId());

		if (!qlpktclhPhieuKtChatLuongOpt.isPresent()) throw new Exception("Entity can not be found");

		QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongOpt.get();

		qlpktclhPhieuKtChatLuong = dataUtils.toObject(req, QlpktclhPhieuKtChatLuong.class);

		qlpktclhPhieuKtChatLuong.setNgaySua(LocalDate.now());
		qlpktclhPhieuKtChatLuong.setNguoiSuaId(userInfo.getId());
		qlpktclhPhieuKtChatLuong.setMaDonVi(userInfo.getDvql());

		QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(qlpktclhPhieuKtChatLuong, QlpktclhPhieuKtChatLuongResponseDto.class);

		if (CollectionUtils.isEmpty(req.getKetQuaKiemTra())) return response;


		//Update kết quả kiểm tra
		Long phieuKiemTraChatLuongId = qlpktclhPhieuKtChatLuong.getId();
		List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTraRes =
				req.getKetQuaKiemTra().stream()
						.map(item -> {
							QlpktclhKetQuaKiemTra ketQuaKiemTra = dataUtils.toObject(item, QlpktclhKetQuaKiemTra.class);
							ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
							ketQuaKiemTra = qlpktclhKetQuaKiemTraRepo.save(ketQuaKiemTra);

							return dataUtils.toObject(ketQuaKiemTra, QlpktclhKetQuaKiemTraResponseDto.class);
						}).collect(Collectors.toList());

		response.setKetQuaKiemTra(ketQuaKiemTraRes);

		return response;
	}

	@Override
	public Page<QlpktclhPhieuKtChatLuongResponseDto> filter(QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception {

		return qlpktclhPhieuKtChatLuongRepo.filter(req);
	}

	@Override
	public Page<QlpktclhPhieuKtChatLuong> timKiem(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		return qlpktclhPhieuKtChatLuongRepo.select(req.getSoPhieu(),req.getNgayLapPhieu(),req.getTenNguoiGiao(), pageable);
	}
}
