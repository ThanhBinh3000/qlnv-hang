package com.tcdt.qlnvhang.service.quanlyphieukiemtrachatluonghangluongthuc;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTra;
import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.enums.QlpktclhPhieuKtChatLuongStatusEnum;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTraRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
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
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
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
		qlpktclhPhieuKtChatLuong.setTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO.getId());
		qlpktclhPhieuKtChatLuong.setCapDvi(userInfo.getCapDvi());
		qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongRepo.save(qlpktclhPhieuKtChatLuong);

		//Kết quả kiểm tra
		Long phieuKiemTraChatLuongId = qlpktclhPhieuKtChatLuong.getId();

		List<QlpktclhKetQuaKiemTra> ketQuaKiemTras = new ArrayList<>();
		req.getKetQuaKiemTra().forEach(item -> {
			QlpktclhKetQuaKiemTra ketQuaKiemTra = dataUtils.toObject(item, QlpktclhKetQuaKiemTra.class);
			ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
			ketQuaKiemTra = qlpktclhKetQuaKiemTraRepo.save(ketQuaKiemTra);
			ketQuaKiemTras.add(ketQuaKiemTra);
		});
		qlpktclhPhieuKtChatLuong.setKetQuaKiemTra(ketQuaKiemTras);

		return this.buildResponse(qlpktclhPhieuKtChatLuong);
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
		qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongRepo.save(qlpktclhPhieuKtChatLuong);

		//Update kết quả kiểm tra
		Long phieuKiemTraChatLuongId = qlpktclhPhieuKtChatLuong.getId();
		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongId(phieuKiemTraChatLuongId);

		List<QlpktclhKetQuaKiemTra> ketQuaKiemTras = new ArrayList<>();
		req.getKetQuaKiemTra().forEach(item -> {
			QlpktclhKetQuaKiemTra ketQuaKiemTra = dataUtils.toObject(item, QlpktclhKetQuaKiemTra.class);
			ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
			ketQuaKiemTra = qlpktclhKetQuaKiemTraRepo.save(ketQuaKiemTra);
			ketQuaKiemTras.add(ketQuaKiemTra);
		});
		qlpktclhPhieuKtChatLuong.setKetQuaKiemTra(ketQuaKiemTras);
		return this.buildResponse(qlpktclhPhieuKtChatLuong);
	}

	@Override
	public Page<QlpktclhPhieuKtChatLuongResponseDto> filter(QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception {

		return qlpktclhPhieuKtChatLuongRepo.filter(req);
	}

	@Override
	public Page<QlpktclhPhieuKtChatLuong> search(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		return qlpktclhPhieuKtChatLuongRepo.select(req.getSoPhieu(),req.getNgayLapPhieu(),req.getTenNguoiGiao(), pageable);
	}

	private QlpktclhPhieuKtChatLuongResponseDto buildResponse(QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong) {
		QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(qlpktclhPhieuKtChatLuong, QlpktclhPhieuKtChatLuongResponseDto.class);
		List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTraRes = qlpktclhPhieuKtChatLuong.getKetQuaKiemTra().stream()
						.map(item -> dataUtils.toObject(item, QlpktclhKetQuaKiemTraResponseDto.class))
						.collect(Collectors.toList());

		response.setKetQuaKiemTra(ketQuaKiemTraRes);
		return response;
	}

	@Override
	public QlpktclhPhieuKtChatLuongResponseDto detail(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (id == null) throw new Exception("Id is required in update");

		Optional<QlpktclhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongOpt = qlpktclhPhieuKtChatLuongRepo.findById(id);

		if (!qlpktclhPhieuKtChatLuongOpt.isPresent()) throw new Exception("Entity can not be found");
		QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongOpt.get();
		qlpktclhPhieuKtChatLuong.setKetQuaKiemTra(qlpktclhKetQuaKiemTraRepo.findAllByPhieuKtChatLuongId(qlpktclhPhieuKtChatLuong.getId()));
		return this.buildResponse(qlpktclhPhieuKtChatLuong);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean approve(StatusReq req) throws Exception {

		UserInfo userInfo = UserUtils.getUserInfo();
		if (StringUtils.isEmpty(req.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<QlpktclhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		QlpktclhPhieuKtChatLuong phieu = optional.get();
		String trangThai = phieu.getTrangThai();
		if (QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(req.getTrangThai())) {
			if (!QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO.getId().equals(trangThai))
				return false;

			phieu.setTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO_TRINH_DUYET.getId());
			phieu.setNguoiGuiDuyetId(userInfo.getId());
			phieu.setNgayGuiDuyet(LocalDate.now());

		} else if (QlpktclhPhieuKtChatLuongStatusEnum.LANH_DAO_DUYET.getId().equals(req.getTrangThai())) {
			if (!QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;
			phieu.setTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.LANH_DAO_DUYET.getId());
			phieu.setNguoiPheDuyetId(userInfo.getId());
			phieu.setNgayPheDuyet(LocalDate.now());
		} else if (QlpktclhPhieuKtChatLuongStatusEnum.BAN_HANH.getId().equals(req.getTrangThai())) {
			if (!QlpktclhPhieuKtChatLuongStatusEnum.LANH_DAO_DUYET.getId().equals(trangThai))
				return false;

			phieu.setTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.BAN_HANH.getId());
			phieu.setNguoiPheDuyetId(userInfo.getId());
			phieu.setNgayPheDuyet(LocalDate.now());
		} else if (QlpktclhPhieuKtChatLuongStatusEnum.TU_CHOI.getId().equals(req.getTrangThai())) {
			if (!QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;

			phieu.setTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.TU_CHOI.getId());
			phieu.setNguoiPheDuyetId(userInfo.getId());
			phieu.setNgayPheDuyet(LocalDate.now());
			phieu.setLyDoTuChoi(req.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		qlpktclhPhieuKtChatLuongRepo.save(phieu);

		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<QlpktclhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findById(id);
		if (!optional.isPresent())
			throw new Exception("Phiếu kiểm tra chất lượng không tồn tại");

		QlpktclhPhieuKtChatLuong phieu = optional.get();

		if (QlpktclhPhieuKtChatLuongStatusEnum.BAN_HANH.getId().equals(phieu.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã ban hành");
		} else if (QlpktclhPhieuKtChatLuongStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(phieu.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh trình duyệt");
		}

		Long phieuKiemTraChatLuongId = phieu.getId();
		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongId(phieuKiemTraChatLuongId);
		qlpktclhPhieuKtChatLuongRepo.delete(phieu);
		return true;
	}
}
