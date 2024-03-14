package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemtracl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuongCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl.QlpktclhKetQuaKiemTraRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNxDdiemRepository;
import com.tcdt.qlnvhang.request.object.NnPhieuKtChatLuongPreview;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
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

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

	@Autowired
	private final HhQdGiaoNvuNxDdiemRepository hhQdGiaoNvuNxDdiemRepository;
	@Autowired
	private FileDinhKemService fileDinhKemService;
	@Autowired
	private HhHopDongRepository hhHopDongRepository;

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
	public NhPhieuKtChatLuong create(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");

		NhPhieuKtChatLuong phieu = dataUtils.toObject(req, NhPhieuKtChatLuong.class);
		phieu.setNgayTao(new Date());
		phieu.setNguoiTaoId(userInfo.getId());
		phieu.setMaDvi(userInfo.getDvql());
		phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		phieu.setNam(LocalDate.now().getYear());
		phieu.setId(Long.valueOf(phieu.getSoPhieu().split("/")[0]));
		this.validateData(phieu);
		qlpktclhPhieuKtChatLuongRepo.save(phieu);
		updateFile(req, phieu);
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

	private void updateFile(QlpktclhPhieuKtChatLuongRequestDto req, NhPhieuKtChatLuong phieu) {
		fileDinhKemService.delete(phieu.getId(), Lists.newArrayList("NH_PHIEU_KT_CHAT_LUONG_KTCL"));
		fileDinhKemService.delete(phieu.getId(), Lists.newArrayList("NH_PHIEU_KT_CHAT_LUONG"));
		if (!DataUtils.isNullOrEmpty(req.getFileDinhKemsKtcl())) {
			fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemsKtcl(), phieu.getId(), "NH_PHIEU_KT_CHAT_LUONG_KTCL");
		}
		if (!DataUtils.isNullObject(req.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), phieu.getId(), "NH_PHIEU_KT_CHAT_LUONG");
		}
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
		nhPhieuKtChatLuong.setNgaySua(new Date());
		nhPhieuKtChatLuong.setNguoiSuaId(userInfo.getId());
		nhPhieuKtChatLuong.setMaDvi(userInfo.getDvql());
		this.validateData(nhPhieuKtChatLuong);
		nhPhieuKtChatLuong = qlpktclhPhieuKtChatLuongRepo.save(nhPhieuKtChatLuong);
		updateFile(req, nhPhieuKtChatLuong);
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


	void validateData(NhPhieuKtChatLuong obj) throws Exception {
		BigDecimal soLuongNhapKho = this.getSoLuongNhapKho(obj.getIdDdiemGiaoNvNh());
		Optional<NhQdGiaoNvuNxDdiem> byId = hhQdGiaoNvuNxDdiemRepository.findById(obj.getIdDdiemGiaoNvNh());
		if(byId.isPresent()){
			BigDecimal soLuong = byId.get().getSoLuong().multiply(new BigDecimal(1000));
			if(soLuongNhapKho.add(obj.getSoLuongNhapKho()).compareTo(soLuong) > 0){
				throw new Exception("Số lượng nhập kho đã vượt quá số lượng cho phép, xin vui lòng nhập lại");
			}
		}else{
			throw new Exception("Không tìm thấy địa điểm nhập kho");
		}
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
		data.setFileDinhKems(fileDinhKemService.search(data.getId(), Collections.singletonList("NH_PHIEU_KT_CHAT_LUONG")));
		data.setFileDinhKemsKtcl(fileDinhKemService.search(data.getId(), Collections.singletonList("NH_PHIEU_KT_CHAT_LUONG_KTCL")));
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
			case Contains.CHODUYET_TK + Contains.DUTHAO:
			case Contains.CHODUYET_TK + Contains.TUCHOI_LDCC:
			case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
				phieu.setNguoiGuiDuyetId(userInfo.getId());
				phieu.setNgayGuiDuyet(new Date());
				break;
			case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
			case Contains.TUCHOI_TK + Contains.CHODUYET_TK:
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(new Date());
				phieu.setLyDoTuChoi(req.getLyDoTuChoi());
				break;
			case Contains.CHODUYET_LDCC + Contains.CHODUYET_TK:
				this.validateData(phieu);
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(new Date());
				break;
			case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
				this.validateData(phieu);
				phieu.setTenLdcc(userInfo.getFullName());
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(new Date());
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

	@Override
	public void deleteMulti(List<Long> listMulti) {

	}

	@Override
	public void export(QlpktclhPhieuKtChatLuongRequestDto req, HttpServletResponse response) throws Exception {
//		return false;
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

	@Override
	public BigDecimal getSoLuongNhapKho(Long idDdiemGiaoNvNh) {
		BigDecimal bigDecimal = qlpktclhPhieuKtChatLuongRepo.soLuongNhapKho(idDdiemGiaoNvNh,NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
		return bigDecimal;
	}

	List<NhPhieuKtChatLuong> setDetailList(List<NhPhieuKtChatLuong> list){
		list.forEach( item -> {
			item.setPhieuNhapKho(nhPhieuNhapKhoRepository.findBySoPhieuKtraCl(item.getSoPhieu()));
		});
		return list;
	}

	@Override
	public ReportTemplateResponse preview(QlpktclhPhieuKtChatLuongRequestDto objReq) throws Exception {
		Optional<NhPhieuKtChatLuong> qOptional = qlpktclhPhieuKtChatLuongRepo.findById(objReq.getId());
		if (!qOptional.isPresent()) {
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}
		ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		NnPhieuKtChatLuongPreview object = new NnPhieuKtChatLuongPreview();
		NhPhieuKtChatLuong nhPhieuKtChatLuong = this.detail(objReq.getId());
		BeanUtils.copyProperties(nhPhieuKtChatLuong, object);
		Optional<HhHopDongHdr> hhHopDongHdr = hhHopDongRepository.findBySoHd(nhPhieuKtChatLuong.getSoHd());
		if (hhHopDongHdr.isPresent()) {
			object.setNgayKyHd(convertDate(hhHopDongHdr.get().getNgayKy()));
		}
		Map<String, String> dmucDanhGia = getListDanhMucChung("KET_QUA_DANH_GIA");
		object.getKetQuaKiemTra().forEach(item -> {
			item.setDanhGia(dmucDanhGia.get(item.getDanhGia()));
		});
		return docxToPdfConverter.convertDocxToPdf(inputStream, object);
	}

}
