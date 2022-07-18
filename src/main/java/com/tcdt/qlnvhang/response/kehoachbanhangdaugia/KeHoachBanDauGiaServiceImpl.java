package com.tcdt.qlnvhang.response.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach_;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.enums.QlpktclhPhieuKtChatLuongStatusEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.*;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKehoachRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.kehoachbanhangdaugia.KeHoachBanDauGiaService;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class KeHoachBanDauGiaServiceImpl extends BaseServiceImpl implements KeHoachBanDauGiaService {
	private final BhDgKehoachRepository bhDgKehoachRepository;
	private final BhDgKhDiaDiemGiaoNhanRepository diaDiemGiaoNhanRepository;
	private final BhDgKhPhanLoTaiSanRepository phanLoTaiSanRepository;
	private final FileDinhKemService fileDinhKemService;

	private final BhDgKehoachRequestMapper kehoachRequestMapper;
	private final BhDgKehoachResponseMapper kehoachResponseMapper;
	private final BhDgKhDiaDiemGiaoNhanRequestMapper diaDiemGiaoNhanRequestMapper;
	private final BhDgKhPhanLoTaiSanRequestMapper phanLoTaiSanRequestMapper;
	private static final String SHEET_NAME = "Danh sách kế hoạch bán đấu giá";
	private static final String STT = "STT";
	private static final String SO_KE_HOACH = "Số kế hoạch";
	private static final String NGAY_LAP_KE_HOACH = "Ngày lập kế hoạch";
	private static final String NGAY_KY = "Ngày ký";
	private static final String TRICH_YEU = "Trích yếu";
	private static final String LOAI_HANG_HOA = "Loại hàng hóa";
	private static final String SO_QD_GIAO_CHI_TIEU = "Số quyết định giao chỉ tiêu";
	private static final String SO_QD_PHE_DUYET_KH_BAN_DAU_GIA = "Số QĐ phê duyệt KH bán đấu giá";
	private static final String NAM_KE_HOACH = "Năm kế hoạch";
	private static final String TRANG_THAI = "Trạng thái";


	@Override
	@Transactional(rollbackFor = Exception.class)
	public BhDgKehoachRes create(BhDgKehoachReq req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		log.info("Save ke hoach ban dau gia");
		BhDgKehoach keHoachDauGia = kehoachRequestMapper.toEntity(req);
		keHoachDauGia.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		keHoachDauGia.setNgayTao(LocalDate.now());
		keHoachDauGia.setNguoiTaoId(userInfo.getId());
		keHoachDauGia.setMaDv(userInfo.getDvql());
		keHoachDauGia.setCapDv(userInfo.getCapDvi());
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), BhDgKehoach.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);

		log.info("Save dia diem giao nhan");
		BhDgKehoach finalKeHoachDauGia = keHoachDauGia;
		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = req.getDiaDiemGiaoNhanList().stream().map(it -> {
			BhDgKhDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = diaDiemGiaoNhanRequestMapper.toEntity(it);
			dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return dgKhDiaDiemGiaoNhan;
		}).collect(Collectors.toList());
		diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
		keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);


		log.info("Save phan lo tai san");
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = req.getPhanLoTaiSanList().stream().map(it -> {
			BhDgKhPhanLoTaiSan phanLoTaiSan = phanLoTaiSanRequestMapper.toEntity(it);
			phanLoTaiSan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return phanLoTaiSan;
		}).collect(Collectors.toList());

		phanLoTaiSanList = phanLoTaiSanRepository.saveAll(phanLoTaiSanList);
		keHoachDauGia.setPhanLoTaiSanList(phanLoTaiSanList);

		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public BhDgKehoachRes update(BhDgKehoachReq req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<BhDgKehoach> optional = bhDgKehoachRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhDgKehoach keHoachDauGia = optional.get();

		log.info("Update ke hoach ban dau gia");
		kehoachRequestMapper.partialUpdate(keHoachDauGia, req);

		keHoachDauGia.setNgaySua(LocalDate.now());
		keHoachDauGia.setNguoiSuaId(userInfo.getId());
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);

		BhDgKehoach finalKeHoachDauGia = keHoachDauGia;

		log.info("Update file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), BhDgKehoach.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);


		log.info("Update dia diem giao nhan");
		if (!CollectionUtils.isEmpty(req.getDiaDiemGiaoNhanList())) {
			//Clean data
			List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
			if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
				diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
			}
			//Save data
			diaDiemGiaoNhanList = req.getDiaDiemGiaoNhanList().stream().map(it -> {
				BhDgKhDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = diaDiemGiaoNhanRequestMapper.toEntity(it);
				dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
				return dgKhDiaDiemGiaoNhan;
			}).collect(Collectors.toList());
			diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
			keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);
		}

		log.info("Update phan lo tai san");
		if (!CollectionUtils.isEmpty(req.getPhanLoTaiSanList())) {
			//Clean data
			List<BhDgKhPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
			if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
				phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
			}

			List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = req.getPhanLoTaiSanList().stream().map(it -> {
				BhDgKhPhanLoTaiSan phanLoTaiSan = phanLoTaiSanRequestMapper.toEntity(it);
				phanLoTaiSan.setBhDgKehoachId(finalKeHoachDauGia.getId());
				return phanLoTaiSan;
			}).collect(Collectors.toList());

			phanLoTaiSanList = phanLoTaiSanRepository.saveAll(phanLoTaiSanList);
			keHoachDauGia.setPhanLoTaiSanList(phanLoTaiSanList);
		}

		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		if (id == null) throw new Exception("Bad request.");
		Optional<BhDgKehoach> optional = bhDgKehoachRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhDgKehoach keHoachDauGia = optional.get();

		log.info("Delete file dinh kem");
		fileDinhKemService.delete(keHoachDauGia.getId(), Collections.singleton(BhDgKehoach.TABLE_NAME));
		log.info("Delete địa điểm giao nhận");
		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
		if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
			diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
		}

		log.info("Delete phan lo tai san");
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
		if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
			phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
		}

		log.info("Delete ke hoach ban dau gia");
		bhDgKehoachRepository.delete(keHoachDauGia);
		return true;
	}

	@Override
	public Page<BhDgKehoachRes> search(BhDgKehoachSearchReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Specification<BhDgKehoach> specs = SpecUtils.build(BhDgKehoach.class);
		specs.and(SpecUtils.ge(BhDgKehoach_.ID, 0));
		if (Objects.nonNull(req.getId())) {
			specs.and(SpecUtils.equal(BhDgKehoach_.ID, req.getId()));
		}

		if (Objects.nonNull(req.getNamKeHoach())) {
			specs.and(SpecUtils.equal(BhDgKehoach_.NAM_KE_HOACH, req.getNamKeHoach()));
		}
		if (Objects.nonNull(req.getSoKeHoach())) {
			specs.and(SpecUtils.like(BhDgKehoach_.SO_KE_HOACH, req.getSoKeHoach()));
		}
		if (Objects.nonNull(req.getTrichYeu())) {
			specs.and(SpecUtils.like(BhDgKehoach_.TRICH_YEU, req.getTrichYeu()));
		}
		if (Objects.nonNull(req.getNgayKyTuNgay())) {
			specs.and(SpecUtils.greaterThanOrEqualTo(BhDgKehoach_.NGAY_KY, req.getNgayKyTuNgay()));
		}
		if (Objects.nonNull(req.getNgayKyTuNgay())) {
			specs.and(SpecUtils.lessThanOrEqualTo(BhDgKehoach_.NGAY_KY, req.getNgayKyDenNgay()));
		}
		if (!CollectionUtils.isEmpty(req.getCapDvis())) {
			specs.and(SpecUtils.in(BhDgKehoach_.CAP_DV, req.getCapDvis()));
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			specs.and(SpecUtils.in(BhDgKehoach_.MA_DV, req.getMaDvis()));
		}

		if (Objects.nonNull(req.getLoaiVatTuHangHoa())) {
			specs.and(SpecUtils.equal(BhDgKehoach_.LOAI_VAT_TU_HANG_HOA, req.getLoaiVatTuHangHoa()));
		}

		Page<BhDgKehoach> page = bhDgKehoachRepository.findAll(specs, req.getPageable());

		List<BhDgKehoach> response = page.get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(response)) page.map(kehoachResponseMapper::toDto);

		List<Long> ids = response.stream().map(BhDgKehoach::getId).collect(Collectors.toList());

		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachIdIn(ids);
		//Map: Key-value = BhDgKehoachId-list BhDgKhDiaDiemGiaoNhan
		Map<Long, List<BhDgKhDiaDiemGiaoNhan>> diaDiemGiaoNhanMap = diaDiemGiaoNhanList.stream()
				.collect(Collectors.groupingBy(BhDgKhDiaDiemGiaoNhan::getBhDgKehoachId));

		//Map: Key-value = BhDgKehoachId- list BhDgKhPhanLoTaiSan
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = phanLoTaiSanRepository.findByBhDgKehoachIdIn(ids);
		Map<Long, List<BhDgKhPhanLoTaiSan>> phanLoTaiSanMap = phanLoTaiSanList.stream()
				.collect(Collectors.groupingBy(BhDgKhPhanLoTaiSan::getBhDgKehoachId));

		List<BhDgKehoachRes> responseDto = response.stream().map(it -> {
			if (Objects.nonNull(diaDiemGiaoNhanMap.get(it.getId()))) {
				it.setDiaDiemGiaoNhanList(diaDiemGiaoNhanMap.get(it.getId()));
			}

			if (Objects.nonNull(phanLoTaiSanMap.get(it.getId()))) {
				it.setPhanLoTaiSanList(phanLoTaiSanMap.get(it.getId()));
			}
			return kehoachResponseMapper.toDto(it);
		}).collect(Collectors.toList());

		return new PageImpl<>(responseDto);
	}
	@Override
	public BhDgKehoachRes updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<BhDgKehoach> optional = bhDgKehoachRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhDgKehoach keHoachDauGia = optional.get();
		//validate Trạng Thái
		String trangThai = TrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		keHoachDauGia.setTrangThai(trangThaiId);
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);
		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public boolean exportToExcel(BhDgKehoachSearchReq req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BhDgKehoachRes> list = this.search(req).get().collect(Collectors.toList());

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
			XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
			Row row0 = sheet.createRow(0);

			ExportExcel.createCell(row0, 0, STT, style, sheet);
			ExportExcel.createCell(row0, 1, SO_KE_HOACH, style, sheet);
			ExportExcel.createCell(row0, 2, NGAY_LAP_KE_HOACH, style, sheet);
			ExportExcel.createCell(row0, 3, NGAY_KY, style, sheet);
			ExportExcel.createCell(row0, 5, TRICH_YEU, style, sheet);
			ExportExcel.createCell(row0, 6, LOAI_HANG_HOA, style, sheet);
			ExportExcel.createCell(row0, 7, SO_QD_GIAO_CHI_TIEU, style, sheet);
			ExportExcel.createCell(row0, 8, SO_QD_PHE_DUYET_KH_BAN_DAU_GIA, style, sheet);
			ExportExcel.createCell(row0, 9, NAM_KE_HOACH, style, sheet);
			ExportExcel.createCell(row0, 10, TRANG_THAI, style, sheet);

			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeight(11);
			style.setFont(font);

			Row row;
			int startRowIndex = 1;

			for (BhDgKehoachRes item : list) {
				row = sheet.createRow(startRowIndex);
				ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
				ExportExcel.createCell(row, 1, item.getSoKeHoach(), style, sheet);
				ExportExcel.createCell(row, 2, LocalDateTimeUtils.localDateToString(item.getNgayLapKeHoach()), style, sheet);
				ExportExcel.createCell(row, 3, LocalDateTimeUtils.localDateToString(item.getNgayKy()), style, sheet);
				ExportExcel.createCell(row, 4, item.getTrichYeu(), style, sheet);
				ExportExcel.createCell(row, 5, item.getLoaiVatTuHangHoa(), style, sheet);
				ExportExcel.createCell(row, 6, item.getQdGiaoChiTieuId(), style, sheet);
//				ExportExcel.createCell(row, 7, item.getTrichYeu(), style, sheet);
				ExportExcel.createCell(row, 8, item.getNamKeHoach(), style, sheet);
				ExportExcel.createCell(row, 9, item.getTenTrangThai(), style, sheet);
				ExportExcel.createCell(row, 10, item.getTrichYeu(), style, sheet);
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

	@Override
	public boolean deleteMultiple(List<Long> ids) throws Exception {
//		UserInfo userInfo = SecurityContextService.getUser();
//		if (userInfo == null) throw new Exception("Bad request.");
//
//		if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");
//		List<BhDgKehoach> kehoachList = bhDgKehoachRepository.findByIdIn(ids);
//		if (CollectionUtils.isEmpty(kehoachList)) throw new Exception("Kế hoạch bán đấu giá không tồn tại");
//
//		log.info("Delete file dinh kem");
//		fileDinhKemService.delete(keHoachDauGia.getId(), Collections.singleton(BhDgKehoach.TABLE_NAME));
//		log.info("Delete địa điểm giao nhận");
//		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
//		if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
//			diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
//		}
//
//		log.info("Delete phan lo tai san");
//		List<BhDgKhPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
//		if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
//			phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
//		}
//
//		log.info("Delete ke hoach ban dau gia");
//		bhDgKehoachRepository.delete(keHoachDauGia);
		return true;
	}
}
