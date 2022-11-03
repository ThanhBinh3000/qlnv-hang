package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieuktracl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuongCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl.QlpktclhKetQuaKiemTraRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTraResponseDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class NhPhieuKtChatLuongServiceImpl extends BaseServiceImpl implements NhPhieuKtChatLuongService {

	@Autowired
	private final NhPhieuKtChatLuongRepository qlpktclhPhieuKtChatLuongRepo;
	@Autowired
	private final QlpktclhKetQuaKiemTraRepository qlpktclhKetQuaKiemTraRepo;

	@Autowired
	private final NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;
	@Autowired
	private final DataUtils dataUtils;

	@Override
	public Page<NhPhieuKtChatLuong> searchPage(QlpktclhPhieuKtChatLuongRequestDto objReq) {
		Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<NhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongs = qlpktclhPhieuKtChatLuongRepo.selectPage(objReq.getSoPhieu(), objReq.getBienSoXe(), objReq.getNguoiGiaoHang(), pageable);
		qlpktclhPhieuKtChatLuongs.getContent().forEach(x -> {
			x.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(x.getTrangThai()));
		});
		return qlpktclhPhieuKtChatLuongs;
	}

	@Override
	public List<NhPhieuKtChatLuong> searchAll(QlpktclhPhieuKtChatLuongRequestDto req) {
		return null;
	}

	@Override
	public NhPhieuKtChatLuong create(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");

		NhPhieuKtChatLuong phieu = dataUtils.toObject(req, NhPhieuKtChatLuong.class);
		phieu.setNgayTao(LocalDate.now());
		phieu.setNguoiTaoId(userInfo.getId());
		phieu.setMaDvi(userInfo.getDvql());
		phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		phieu.setNam(LocalDate.now().getYear());
		phieu.setId(Long.valueOf(phieu.getSoPhieu().split("/")[0]));
		qlpktclhPhieuKtChatLuongRepo.save(phieu);

		//Kết quả kiểm tra
		Long phieuKiemTraChatLuongId = phieu.getId();

		List<NhPhieuKtChatLuongCt> ketQuaKiemTras = new ArrayList<>();
		req.getKetQuaKiemTra().forEach(item -> {
			NhPhieuKtChatLuongCt ketQuaKiemTra = dataUtils.toObject(item, NhPhieuKtChatLuongCt.class);
			ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
			ketQuaKiemTras.add(ketQuaKiemTra);
		});
		qlpktclhKetQuaKiemTraRepo.saveAll(ketQuaKiemTras);
		return phieu;
	}

	@Override
	public NhPhieuKtChatLuong update(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
			throw new Exception("Bad Request");

		}
		if (req.getId() == null){
			throw new Exception("Id is required in update");
		}

		Optional<NhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongOpt = qlpktclhPhieuKtChatLuongRepo.findById(req.getId());

		if (!qlpktclhPhieuKtChatLuongOpt.isPresent()){
			throw new Exception("Entity can not be found");
		}

		NhPhieuKtChatLuong nhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongOpt.get();

		BeanUtils.copyProperties(req, nhPhieuKtChatLuong);
		nhPhieuKtChatLuong.setNgaySua(LocalDate.now());
		nhPhieuKtChatLuong.setNguoiSuaId(userInfo.getId());
		nhPhieuKtChatLuong.setMaDvi(userInfo.getDvql());
		nhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongRepo.save(nhPhieuKtChatLuong);

		//Update kết quả kiểm tra
		Long phieuKiemTraChatLuongId = nhPhieuKtChatLuong.getId();
		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongId(phieuKiemTraChatLuongId);

		List<NhPhieuKtChatLuongCt> ketQuaKiemTras = new ArrayList<>();
		req.getKetQuaKiemTra().forEach(item -> {
			NhPhieuKtChatLuongCt ketQuaKiemTra = new NhPhieuKtChatLuongCt();
			BeanUtils.copyProperties(item, ketQuaKiemTra, "id");
			ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
			ketQuaKiemTras.add(ketQuaKiemTra);
		});
		qlpktclhKetQuaKiemTraRepo.saveAll(ketQuaKiemTras);
		nhPhieuKtChatLuong.setKetQuaKiemTra(ketQuaKiemTras);
		return nhPhieuKtChatLuong;
	}

	@Override
	public NhPhieuKtChatLuong detail(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (id == null){
			throw new Exception("Id is required in update");
		}

		Optional<NhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongOpt = qlpktclhPhieuKtChatLuongRepo.findById(id);

		if (!qlpktclhPhieuKtChatLuongOpt.isPresent()){
			throw new Exception("Entity can not be found");
		}
		Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
		Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");

		NhPhieuKtChatLuong data = qlpktclhPhieuKtChatLuongOpt.get();
		data.setKetQuaKiemTra(qlpktclhKetQuaKiemTraRepo.findAllByPhieuKtChatLuongId(data.getId()));
		data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
		data.setTenLoaiVthh(listDanhMucHangHoa.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(listDanhMucHangHoa.get(data.getCloaiVthh()));
		data.setTenDvi(listDanhMucDvi.get(data.getMaDvi()));
		data.setTenDiemKho(listDanhMucDvi.get(data.getMaDiemKho()));
		data.setTenNhaKho(listDanhMucDvi.get(data.getMaNhaKho()));
		data.setTenNganKho(listDanhMucDvi.get(data.getMaNganKho()));
		data.setTenLoKho(listDanhMucDvi.get(data.getMaLoKho()));
		return data;
	}

	@Override
	public NhPhieuKtChatLuong approve(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
			throw new Exception("Bad Request");
		}

		if (StringUtils.isEmpty(req.getId())){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		Optional<NhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findById(req.getId());
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		NhPhieuKtChatLuong phieu = optional.get();

		String status = req.getTrangThai() + phieu.getTrangThai();
		switch (status) {
			case Contains.CHODUYET_LDCC + Contains.DUTHAO:
			case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
				phieu.setNguoiGuiDuyetId(userInfo.getId());
				phieu.setNgayGuiDuyet(LocalDate.now());
				break;
			case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(LocalDate.now());
				phieu.setLyDoTuChoi(req.getLyDoTuChoi());
				break;
			case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(LocalDate.now());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		phieu.setTrangThai(req.getTrangThai());
		qlpktclhPhieuKtChatLuongRepo.save(phieu);
		return phieu;
	}

	@Override
	public void delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");

		Optional<NhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findById(id);
		if (!optional.isPresent())
			throw new Exception("Phiếu kiểm tra chất lượng không tồn tại");

		NhPhieuKtChatLuong phieu = optional.get();

		if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(phieu.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã đã duyệt");
		}

		Long phieuKiemTraChatLuongId = phieu.getId();
		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongId(phieuKiemTraChatLuongId);
		qlpktclhPhieuKtChatLuongRepo.delete(phieu);
	}

//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public QlpktclhPhieuKtChatLuong update(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
//			throw new Exception("Bad Request");
//		if (req.getId() == null) throw new Exception("Id is required in update");
//
//		Optional<QlpktclhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongOpt = qlpktclhPhieuKtChatLuongRepo.findById(req.getId());
//
//		if (!qlpktclhPhieuKtChatLuongOpt.isPresent()) throw new Exception("Entity can not be found");
//
//
//		QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongOpt.get();
//
//		this.validateSoPhieu(qlpktclhPhieuKtChatLuong, req);
//		qlpktclhPhieuKtChatLuong = dataUtils.toObject(req, QlpktclhPhieuKtChatLuong.class);
//		qlpktclhPhieuKtChatLuong.setNgaySua(LocalDate.now());
//		qlpktclhPhieuKtChatLuong.setNguoiSuaId(userInfo.getId());
//		qlpktclhPhieuKtChatLuong.setMaDvi(userInfo.getDvql());
//		qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongRepo.save(qlpktclhPhieuKtChatLuong);
//
//		//Update kết quả kiểm tra
//		Long phieuKiemTraChatLuongId = qlpktclhPhieuKtChatLuong.getId();
//		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongId(phieuKiemTraChatLuongId);
//
//		List<QlpktclhKetQuaKiemTra> ketQuaKiemTras = new ArrayList<>();
//		req.getKetQuaKiemTra().forEach(item -> {
//			QlpktclhKetQuaKiemTra ketQuaKiemTra = new QlpktclhKetQuaKiemTra();
//			BeanUtils.copyProperties(item, ketQuaKiemTra, "id");
//			ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
//			ketQuaKiemTra = qlpktclhKetQuaKiemTraRepo.save(ketQuaKiemTra);
//			ketQuaKiemTras.add(ketQuaKiemTra);
//		});
//		qlpktclhPhieuKtChatLuong.setKetQuaKiemTra(ketQuaKiemTras);
//	}

//	public Page<QlpktclhPhieuKtChatLuongResponseDto> filter(QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//		return qlpktclhPhieuKtChatLuongRepo.filter(req);
//	}


//	public Page<QlpktclhPhieuKtChatLuong> search(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
//		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
//		return qlpktclhPhieuKtChatLuongRepo.select(req.getSoPhieu(),req.getNgayLapPhieu(),req.getTenNguoiGiao(), pageable);
//	}

	private QlpktclhPhieuKtChatLuongResponseDto buildResponse(NhPhieuKtChatLuong nhPhieuKtChatLuong) throws Exception {
		QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(nhPhieuKtChatLuong, QlpktclhPhieuKtChatLuongResponseDto.class);
		response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(nhPhieuKtChatLuong.getTrangThai()));
		List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTraRes = nhPhieuKtChatLuong.getKetQuaKiemTra().stream()
						.map(item -> dataUtils.toObject(item, QlpktclhKetQuaKiemTraResponseDto.class))
						.collect(Collectors.toList());

//		if (qlpktclhPhieuKtChatLuong.getQuyetDinhNhapId() != null) {
//			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(qlpktclhPhieuKtChatLuong.getQuyetDinhNhapId());
//			if (!qdNhap.isPresent()) {
//				throw new Exception("Không tìm thấy quyết định nhập");
//			}
//			response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//		}
//
//		if (qlpktclhPhieuKtChatLuong.getHopDongId() != null) {
//			Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(qlpktclhPhieuKtChatLuong.getHopDongId());
//			if (!hopDong.isPresent()) {
//				throw new Exception("Không tìm thấy hợp đồng");
//			}
//			response.setSoHopDong(hopDong.get().getSoHd());
//			response.setNgayHopDong(hopDong.get().getDenNgayHluc());
//
//			response.setLoaiVthh(hopDong.get().getLoaiVthh());
//			response.setLoaiVthh(Contains.mpLoaiVthh.get(hopDong.get().getLoaiVthh()));
//		}

		response.setKetQuaKiemTra(ketQuaKiemTraRes);
		response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(nhPhieuKtChatLuong.getTrangThai()));
		response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(nhPhieuKtChatLuong.getTrangThai()));
//		QlnvDmDonvi donvi = getDviByMa(qlpktclhPhieuKtChatLuong.getMaDvi(), req);
//		response.setMaDvi(donvi.getMaDvi());
//		response.setTenDvi(donvi.getTenDvi());
//		response.setMaQhns(donvi.getMaQhns());
		return response;
	}

//	@Override
//	public QlpktclhPhieuKtChatLuong detail(Long id) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		if (id == null) throw new Exception("Id is required in update");
//
//		Optional<QlpktclhPhieuKtChatLuong> qlpktclhPhieuKtChatLuongOpt = qlpktclhPhieuKtChatLuongRepo.findById(id);
//
//		if (!qlpktclhPhieuKtChatLuongOpt.isPresent()) throw new Exception("Entity can not be found");
//		QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongOpt.get();
//		qlpktclhPhieuKtChatLuong.setKetQuaKiemTra(qlpktclhKetQuaKiemTraRepo.findAllByPhieuKtChatLuongId(qlpktclhPhieuKtChatLuong.getId()));
//		return this.buildResponse(qlpktclhPhieuKtChatLuong);
//	}

//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public boolean approve(StatusReq req) throws Exception {
//
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
//			throw new Exception("Bad Request");
//
//		if (StringUtils.isEmpty(stReq.getId()))
//			throw new Exception("Không tìm thấy dữ liệu");
//
//		Optional<QlpktclhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findById(stReq.getId());
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu");
//
//		QlpktclhPhieuKtChatLuong phieu = optional.get();
//
//		String trangThai = phieu.getTrangThai();
//		if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//				return false;
//
//			phieu.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//			phieu.setNguoiGuiDuyetId(userInfo.getId());
//			phieu.setNgayGuiDuyet(LocalDate.now());
//		} else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//			phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//			phieu.setNguoiPduyetId(userInfo.getId());
//			phieu.setNgayPduyet(LocalDate.now());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//
//			phieu.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//			phieu.setNguoiPduyetId(userInfo.getId());
//			phieu.setNgayPduyet(LocalDate.now());
//		} else {
//			throw new Exception("Bad request.");
//		}
//
//		return true;
//	}

//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public void delete(Long id) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
//			throw new Exception("Bad Request");
//
//		Optional<QlpktclhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findById(id);
//		if (!optional.isPresent())
//			throw new Exception("Phiếu kiểm tra chất lượng không tồn tại");
//
//		QlpktclhPhieuKtChatLuong phieu = optional.get();
//
//		if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(phieu.getTrangThai())) {
//			throw new Exception("Không thể xóa đề xuất điều chỉnh đã đã duyệt");
//		}
//
//		Long phieuKiemTraChatLuongId = phieu.getId();
//		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongId(phieuKiemTraChatLuongId);
//		qlpktclhPhieuKtChatLuongRepo.delete(phieu);
//		return true;
//	}

	@Override
	public void deleteMulti(List<Long> listMulti) {

	}

	@Override
	public boolean export(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		return false;
	}

//	public boolean export(QlpktclhPhieuKtChatLuongFilterRequestDto objReq) throws Exception {
//		HttpServletResponse response ;
//		UserInfo userInfo = UserUtils.getUserInfo();
//		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//		List<QlpktclhPhieuKtChatLuongResponseDto> list = this.filter(objReq).get().collect(Collectors.toList());
//
//		if (CollectionUtils.isEmpty(list))
//			return true;
//
//		String[] rowsName = new String[] { STT, SO_PHIEU, SO_QUYET_DINH_NHAP, DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO,
//				NGAY_GIAM_DINH, KET_QUA_DANH_GIA, TRANG_THAI};
//		String filename = "Danh_sach_phieu_kiem_tra_chat_luong_hang.xlsx";
//
//		List<Object[]> dataList = new ArrayList<Object[]>();
//		Object[] objs = null;
//
//		try {
//			for (int i = 0; i < list.size(); i++) {
//				QlpktclhPhieuKtChatLuongResponseDto item = list.get(i);
//				objs = new Object[rowsName.length];
//				objs[0] = i;
//				objs[1] = item.getSoPhieu();
//				objs[2] = item.getSoQuyetDinhNhap();
//				objs[3] = item.getTenDiemKho();
//				objs[4] = item.getTenNhaKho();
//				objs[5] = item.getTenNganKho();
//				objs[6] = item.getTenNganLo();
//				objs[7] = LocalDateTimeUtils.localDateToString(item.getNgayGdinh());
//				objs[8] = item.getKqDanhGia();
//				objs[9] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//				dataList.add(objs);
//			}
//
//			ExportExcel ex = new ExportExcel(SHEET_PHIEU_KIEM_TRA_CHAT_LUONG_HANG, filename, rowsName, dataList, response);
//			ex.export();
//		} catch (Exception e) {
//			log.error("Error export", e);
//			return false;
//		}
//		return true;
//	}

//	@Transactional
//	@Override
//	public boolean deleteMultiple(DeleteReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (CollectionUtils.isEmpty(req.getIds()))
//			return false;
//
//
//		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongIdIn(req.getIds());
//		qlpktclhPhieuKtChatLuongRepo.deleteByIdIn(req.getIds());
//		return true;
//	}

	private void validateSoPhieu(NhPhieuKtChatLuong update, QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		String so = req.getSoPhieu();
		if (!StringUtils.hasText(so))
			return;
		if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
			Optional<NhPhieuKtChatLuong> optional = qlpktclhPhieuKtChatLuongRepo.findFirstBySoPhieu(so);
			Long updateId = Optional.ofNullable(update).map(NhPhieuKtChatLuong::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số phiếu " + so + " đã tồn tại");
		}
	}

	@Override
	public List<NhPhieuKtChatLuong> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh) {
		List<NhPhieuKtChatLuong> list = qlpktclhPhieuKtChatLuongRepo.findAllByIdQdGiaoNvNhOrderById(idQdGiaoNvNh);
		return setDetailList(list);
	}

	@Override
	public List<NhPhieuKtChatLuong> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh) {
		List<NhPhieuKtChatLuong> list = qlpktclhPhieuKtChatLuongRepo.findByIdDdiemGiaoNvNhOrderById(idDdiemGiaoNvNh);
		return setDetailList(list);
	}

	List<NhPhieuKtChatLuong> setDetailList(List<NhPhieuKtChatLuong> list){
		list.forEach( item -> {
			item.setPhieuNhapKho(nhPhieuNhapKhoRepository.findBySoPhieuKtraCl(item.getSoPhieu()));
		});
		return list;
	}

//	@Override
//	public boolean export(QlpktclhPhieuKtChatLuongFilterRequestDto objReq) throws Exception {
//		return false;
//	}

//	@Override
//	public Integer getSo() throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		Integer so = qlpktclhPhieuKtChatLuongRepo.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//		so = Optional.ofNullable(so).orElse(0);
//		so = so + 1;
//		return so;
//	}
//
//	@Override
//	public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//		QlpktclhPhieuKtChatLuongFilterRequestDto countReq = new QlpktclhPhieuKtChatLuongFilterRequestDto();
//		countReq.setMaDvis(maDvis);
//		BaseNhapHangCount count = new BaseNhapHangCount();
//
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_THOC);
//		count.setThoc(qlpktclhPhieuKtChatLuongRepo.countPhieuKiemTraChatLuong(countReq));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_GAO);
//		count.setGao(qlpktclhPhieuKtChatLuongRepo.countPhieuKiemTraChatLuong(countReq));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_MUOI);
//		count.setMuoi(qlpktclhPhieuKtChatLuongRepo.countPhieuKiemTraChatLuong(countReq));
//		return count;
//	}

}
