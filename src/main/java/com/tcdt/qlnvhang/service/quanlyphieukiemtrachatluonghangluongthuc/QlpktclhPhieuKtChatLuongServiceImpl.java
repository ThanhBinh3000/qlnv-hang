package com.tcdt.qlnvhang.service.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTra;
import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.enums.QlpktclhPhieuKtChatLuongStatusEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTraRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTraResponseDto;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class QlpktclhPhieuKtChatLuongServiceImpl implements QlpktclhPhieuKtChatLuongService {
	private static final String SHEET_PHIEU_KIEM_TRA_CHAT_LUONG_HANG = "Phiếu kiểm tra chất lượng hàng";
	private static final String STT = "STT";
	private static final String SO_PHIEU = "Số Phiếu";
	private static final String KET_QUA_DANH_GIA = "Kết Quả Đánh Giá";
	private static final String NGAY_GIAM_DINH = "Ngày Giám Định";
	private static final String TRANG_THAI = "Trạng Thái";
	private final QlpktclhPhieuKtChatLuongRepository qlpktclhPhieuKtChatLuongRepo;
	private final QlpktclhKetQuaKiemTraRepository qlpktclhKetQuaKiemTraRepo;
	private final DataUtils dataUtils;
	private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
	private final HhHopDongRepository hhHopDongRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QlpktclhPhieuKtChatLuongResponseDto create(QlpktclhPhieuKtChatLuongRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");
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

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");
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
			QlpktclhKetQuaKiemTra ketQuaKiemTra = new QlpktclhKetQuaKiemTra();
			BeanUtils.copyProperties(item, ketQuaKiemTra, "id");
			ketQuaKiemTra.setPhieuKtChatLuongId(phieuKiemTraChatLuongId);
			ketQuaKiemTra = qlpktclhKetQuaKiemTraRepo.save(ketQuaKiemTra);
			ketQuaKiemTras.add(ketQuaKiemTra);
		});
		qlpktclhPhieuKtChatLuong.setKetQuaKiemTra(ketQuaKiemTras);
		return this.buildResponse(qlpktclhPhieuKtChatLuong);
	}

	@Override
	public Page<QlpktclhPhieuKtChatLuongResponseDto> filter(QlpktclhPhieuKtChatLuongFilterRequestDto req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		req.setMaDvi(userInfo.getDvql());
		return qlpktclhPhieuKtChatLuongRepo.filter(req);
	}

	@Override
	public BaseNhapHangCount count() throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		QlpktclhPhieuKtChatLuongFilterRequestDto req = new QlpktclhPhieuKtChatLuongFilterRequestDto();
		req.setMaDvi(userInfo.getDvql());
		BaseNhapHangCount count = new BaseNhapHangCount();

		count.setTatCa(qlpktclhPhieuKtChatLuongRepo.countPhieuKiemTraChatLuong(req));
		return count;
	}

	@Override
	public Page<QlpktclhPhieuKtChatLuong> search(QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		return qlpktclhPhieuKtChatLuongRepo.select(req.getSoPhieu(),req.getNgayLapPhieu(),req.getTenNguoiGiao(), pageable);
	}

	private QlpktclhPhieuKtChatLuongResponseDto buildResponse(QlpktclhPhieuKtChatLuong qlpktclhPhieuKtChatLuong) throws Exception {
		QlpktclhPhieuKtChatLuongResponseDto response = dataUtils.toObject(qlpktclhPhieuKtChatLuong, QlpktclhPhieuKtChatLuongResponseDto.class);
		response.setTenTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.getTenById(qlpktclhPhieuKtChatLuong.getTrangThai()));
		List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTraRes = qlpktclhPhieuKtChatLuong.getKetQuaKiemTra().stream()
						.map(item -> dataUtils.toObject(item, QlpktclhKetQuaKiemTraResponseDto.class))
						.collect(Collectors.toList());

		if (qlpktclhPhieuKtChatLuong.getQuyetDinhNhapId() != null) {
			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(qlpktclhPhieuKtChatLuong.getQuyetDinhNhapId());
			if (!qdNhap.isPresent()) {
				throw new Exception("Không tìm thấy quyết định nhập");
			}
			response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
		}

		if (qlpktclhPhieuKtChatLuong.getHopDongId() != null) {
			Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(qlpktclhPhieuKtChatLuong.getHopDongId());
			if (!hopDong.isPresent()) {
				throw new Exception("Không tìm thấy hợp đồng");
			}
			response.setSoHopDong(hopDong.get().getSoHd());
			response.setNgayHopDong(hopDong.get().getDenNgayHluc());

			response.setLoaiVthh(hopDong.get().getLoaiVthh());
			response.setLoaiVthh(Contains.mpLoaiVthh.get(hopDong.get().getLoaiVthh()));
		}

		response.setKetQuaKiemTra(ketQuaKiemTraRes);
		response.setTenTrangThai(QlpktclhPhieuKtChatLuongStatusEnum.getTenById(qlpktclhPhieuKtChatLuong.getTrangThai()));
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

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");

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
		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi()))
			throw new Exception("Bad Request");

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

	@Override
	public boolean exportToExcel(QlpktclhPhieuKtChatLuongFilterRequestDto objReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		objReq.setMaDvi(userInfo.getDvql());
		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<QlpktclhPhieuKtChatLuongResponseDto> list = this.filter(objReq).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		try {
			XSSFWorkbook workbook = new XSSFWorkbook();

			//STYLE
			CellStyle style = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontHeight(11);
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFSheet sheet = workbook.createSheet(SHEET_PHIEU_KIEM_TRA_CHAT_LUONG_HANG);
			Row row0 = sheet.createRow(0);
			//STT

			ExportExcel.createCell(row0, 0, STT, style, sheet);
			ExportExcel.createCell(row0, 1, SO_PHIEU, style, sheet);
			ExportExcel.createCell(row0, 2, NGAY_GIAM_DINH, style, sheet);
			ExportExcel.createCell(row0, 3, KET_QUA_DANH_GIA, style, sheet);
			ExportExcel.createCell(row0, 4, TRANG_THAI, style, sheet);

			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeight(11);
			style.setFont(font);

			Row row;
			int startRowIndex = 1;

			for (QlpktclhPhieuKtChatLuongResponseDto item : list) {
				row = sheet.createRow(startRowIndex);
				ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
				ExportExcel.createCell(row, 1, item.getSoPhieu(), style, sheet);
				ExportExcel.createCell(row, 2, LocalDateTimeUtils.localDateToString(item.getNgayGdinh()), style, sheet);
				ExportExcel.createCell(row, 3, item.getKetLuan(), style, sheet);
				ExportExcel.createCell(row, 4, QlpktclhPhieuKtChatLuongStatusEnum.getTenById(item.getTrangThai()), style, sheet);
				startRowIndex++;
			}

			ServletOutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			log.error("Error export", e);
			return false;
		}
		return true;
	}

	@Transactional
	@Override
	public boolean deleteMultiple(DeleteReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (CollectionUtils.isEmpty(req.getIds()))
			return false;


		qlpktclhKetQuaKiemTraRepo.deleteByPhieuKtChatLuongIdIn(req.getIds());
		qlpktclhPhieuKtChatLuongRepo.deleteByIdIn(req.getIds());
		return true;
	}
}
