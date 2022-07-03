package com.tcdt.qlnvhang.service.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bbanbangiaomau.BienBanBanGiaoMauRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.phieuknghiemcluonghang.PhieuKnghiemCluongHangRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.KquaKnghiemRes;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.PhieuKnghiemCluongHangRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class PhieuKnghiemCluongHangServiceImpl extends BaseServiceImpl implements PhieuKnghiemCluongHangService {

	private static final String SHEET_PHIEU_KIEM_NGHIEM_CHAT_LUONG_HANG = "Phiếu kiểm nghiệm chất lượng hàng";
	private static final String STT = "STT";
	private static final String SO_PHIEU = "Số Phiếu";
	private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
	private static final String NGAY_BAN_GIAO_MAU = "Ngày Bàn Giao Mẫu";
	private static final String CUC_DU_TRU_NHA_NUOC_KHU_VUC = "Cục Dự Trữ Nhà Nước Khu Vực";
	private static final String DON_VI_TO_CHUC_THU_NGHIEM = "Đơn Vị Tổ Chức Thử Nghiệm";
	private static final String SO_LUONG_MAU_HANG_KIEM_TRA = "Số Lượng Mẫu Hàng Kiểm Tra";
	private static final String TRANG_THAI = "Trạng Thái";

	@Autowired
	private PhieuKnghiemCluongHangRepository phieuKnghiemCluongHangRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private KquaKnghiemService kquaKnghiemService;

	@Autowired
	private KtNganLoRepository ktNganLoRepository;

	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private BienBanBanGiaoMauRepository bienBanBanGiaoMauRepository;

	@Autowired
	private HttpServletRequest httpServletRequest;

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_INDEX = 0;

	@Override
	public Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		String dvql = userInfo.getDvql();

		QlnvDmDonvi donVi = getDviByMa(dvql, httpServletRequest);
		QlnvDmDonvi donViCha = Optional.ofNullable(donVi).map(QlnvDmDonvi::getParent).orElse(null);

		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),  req.getPaggingReq().getLimit());
		List<Object[]> list = phieuKnghiemCluongHangRepository.search(req, pageable);

		Set<Long> ids = list.stream().filter(o -> o[0] != null)
				.map(o -> ((PhieuKnghiemCluongHang) o[0]).getId())
				.collect(Collectors.toSet());
		Map<Long, Long> mapCount = kquaKnghiemService.countKqByPhieuKnghiemId(ids);

		List<PhieuKnghiemCluongHangRes> responses = new ArrayList<>();
		for (Object[] o : list) {
			PhieuKnghiemCluongHangRes response = new PhieuKnghiemCluongHangRes();
			PhieuKnghiemCluongHang item = (PhieuKnghiemCluongHang) o[0];
			KtNganLo nganLo = o[1] != null ? (KtNganLo) o[1] : null;
			Long qdNhapId = (Long) o[2];
			String soQdNhap = (String) o[3];
			Long bbBanGiaoId = (Long) o[4];
			String soBienBanBanGiao = (String) o[5];
			LocalDate ngayBanGiaoMau = (LocalDate) o[6];

			BeanUtils.copyProperties(item, response);
			response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(item.getTrangThai()));
			response.setTrangThaiDuyet(QlPhieuNhapKhoLtStatus.getTrangThaiDuyetById(item.getTrangThai()));
			this.thongTinNganLo(response, nganLo);
			response.setQdgnvnxId(qdNhapId);
			response.setSoQuyetDinhNhap(soQdNhap);
			if (donVi != null) {
				response.setMaDvi(donVi.getMaDvi());
				response.setTenDvi(donVi.getTenDvi());
			}
			if (donViCha != null) {
				response.setMaDviCha(donViCha.getMaDvi());
				response.setTenDviCha(donViCha.getTenDvi());
			}
			response.setBbBanGiaoMauId(bbBanGiaoId);
			response.setSoBbBanGiao(soBienBanBanGiao);
			response.setNgayBanGiaoMau(ngayBanGiaoMau);
			response.setSoLuongMauHangKt(mapCount.get(item.getId()));
			responses.add(response);
		}
		return new PageImpl<>(responses, pageable, phieuKnghiemCluongHangRepository.countCtkhn(req));
	}

	@Override
	public PhieuKnghiemCluongHangRes create(PhieuKnghiemCluongHangReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		this.validateSoPhieu(null, req);
		PhieuKnghiemCluongHang phieuKnclh = new PhieuKnghiemCluongHang();
		this.updateEntity(phieuKnclh, req);
		phieuKnclh.setNguoiTaoId(userInfo.getId());
		phieuKnclh.setNgayTao(LocalDate.now());
		phieuKnclh.setMaDvi(userInfo.getDvql());
		phieuKnclh.setCapDvi(userInfo.getCapDvi());
		phieuKnclh.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		phieuKnghiemCluongHangRepository.save(phieuKnclh);
		kquaKnghiemService.update(phieuKnclh.getId(), req.getKquaKnghiem());

		return this.detail(phieuKnclh);
	}

	@Override
	public PhieuKnghiemCluongHangRes update(PhieuKnghiemCluongHangReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		this.validateSoPhieu(optional.get(), req);
		PhieuKnghiemCluongHang phieuKnclh = optional.get();
		this.updateEntity(phieuKnclh, req);
		phieuKnclh.setNguoiSuaId(userInfo.getId());
		phieuKnclh.setNgaySua(LocalDate.now());

		phieuKnghiemCluongHangRepository.save(phieuKnclh);
		kquaKnghiemService.update(phieuKnclh.getId(), req.getKquaKnghiem());

		return this.detail(phieuKnclh);
	}

	@Override
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(stReq.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		PhieuKnghiemCluongHang phieu = optional.get();
		String trangThai = phieu.getTrangThai();
		if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO.getId().equals(trangThai))
				return false;

			phieu.setTrangThai(TrangThaiEnum.DU_THAO_TRINH_DUYET.getId());
			phieu.setNguoiGuiDuyetId(userInfo.getId());
			phieu.setNgayGuiDuyet(LocalDate.now());
		} else if (TrangThaiEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;
			phieu.setTrangThai(TrangThaiEnum.LANH_DAO_DUYET.getId());
			phieu.setNguoiPduyetId(userInfo.getId());
			phieu.setNgayPduyet(LocalDate.now());
		} else if (TrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.LANH_DAO_DUYET.getId().equals(trangThai))
				return false;

			phieu.setTrangThai(TrangThaiEnum.BAN_HANH.getId());
			phieu.setNguoiPduyetId(userInfo.getId());
			phieu.setNgayPduyet(LocalDate.now());
		} else if (TrangThaiEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;

			phieu.setTrangThai(TrangThaiEnum.TU_CHOI.getId());
			phieu.setNguoiPduyetId(userInfo.getId());
			phieu.setNgayPduyet(LocalDate.now());
			phieu.setLdoTchoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		phieuKnghiemCluongHangRepository.save(phieu);
		return false;
	}

	@Override
	public PhieuKnghiemCluongHangRes detail(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");
		return this.detail(optional.get());
	}

	private PhieuKnghiemCluongHangRes detail(PhieuKnghiemCluongHang phieuKnclh) throws Exception {
		return this.toResponse(phieuKnclh);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		PhieuKnghiemCluongHang phieu = optional.get();

		if (TrangThaiEnum.BAN_HANH.getId().equals(phieu.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã ban hành");
		}
		kquaKnghiemService.deleteByPhieuKnghiemId(phieu.getId());
		phieuKnghiemCluongHangRepository.delete(phieu);
		return true;
	}

	private PhieuKnghiemCluongHangRes toResponse(PhieuKnghiemCluongHang phieu) throws Exception {
		if (phieu == null)
			return null;

		QlnvDmDonvi donVi = getDviByMa(phieu.getMaDvi(), httpServletRequest);
		QlnvDmDonvi donViCha = Optional.ofNullable(donVi).map(QlnvDmDonvi::getParent).orElse(null);

		PhieuKnghiemCluongHangRes res = new PhieuKnghiemCluongHangRes();
		BeanUtils.copyProperties(phieu, res);
		res.setTenTrangThai(TrangThaiEnum.getTenById(res.getTrangThai()));
		res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(res.getTrangThai()));

		Set<String> maVatTus = Stream.of(phieu.getMaVatTu(), phieu.getMaVatTuCha()).collect(Collectors.toSet());
		if (!CollectionUtils.isEmpty(maVatTus)) {
			Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
			if (CollectionUtils.isEmpty(vatTus))
				throw new Exception("Không tìm thấy vật tư");
			vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(phieu.getMaVatTu())).findFirst()
					.ifPresent(v -> res.setTenVatTu(v.getTen()));
			vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(phieu.getMaVatTuCha())).findFirst()
					.ifPresent(v -> res.setTenVatTuCha(v.getTen()));
		}
		KtNganLo nganLo = null;
		if (StringUtils.hasText(phieu.getMaNganLo())) {
			nganLo = ktNganLoRepository.findFirstByMaNganlo(phieu.getMaNganLo());
		}
		this.thongTinNganLo(res, nganLo);

		if (phieu.getQdgnvnxId() != null) {
			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(phieu.getQdgnvnxId());
			if (!qdNhap.isPresent()) {
				throw new Exception("Không tìm thấy quyết định nhập");
			}
			res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
		}

		if (phieu.getBbBanGiaoMauId() != null) {
			Optional<BienBanBanGiaoMau> bbBanGiao = bienBanBanGiaoMauRepository.findById(phieu.getBbBanGiaoMauId());
			if (!bbBanGiao.isPresent()) {
				throw new Exception("Không tìm thấy biên bản bàn giao mẫu");
			}
			res.setSoBbBanGiao(bbBanGiao.get().getSoBienBan());
			res.setNgayBanGiaoMau(bbBanGiao.get().getNgayBanGiaoMau());
		}

		Page<KquaKnghiemRes> list = kquaKnghiemService.list(phieu.getId(), PageRequest.of(DEFAULT_PAGE_INDEX, Integer.MAX_VALUE));
		res.setKquaKnghiem(list.getContent());

		if (donVi != null) {
			res.setMaDvi(donVi.getMaDvi());
			res.setTenDvi(donVi.getTenDvi());
		}
		if (donViCha != null) {
			res.setMaDviCha(donViCha.getMaDvi());
			res.setTenDviCha(donViCha.getTenDvi());
		}
		return res;
	}

	private void updateEntity(PhieuKnghiemCluongHang phieuKnclh, PhieuKnghiemCluongHangReq req) {
		BeanUtils.copyProperties(req, phieuKnclh, "id");
	}

	private void thongTinNganLo(PhieuKnghiemCluongHangRes item, KtNganLo nganLo) {
		if (nganLo != null) {
			item.setTenNganLo(nganLo.getTenNganlo());
			KtNganKho nganKho = nganLo.getParent();
			if (nganKho == null)
				return;

			item.setTenNganKho(nganKho.getTenNgankho());
			item.setMaNganKho(nganKho.getMaNgankho());
			KtNhaKho nhaKho = nganKho.getParent();
			if (nhaKho == null)
				return;

			item.setTenNhaKho(nhaKho.getTenNhakho());
			item.setMaNhaKho(nhaKho.getMaNhakho());
			KtDiemKho diemKho = nhaKho.getParent();
			if (diemKho == null)
				return;

			item.setTenDiemKho(diemKho.getTenDiemkho());
			item.setMaDiemKho(diemKho.getMaDiemkho());
		}
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deleteMultiple(DeleteReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (CollectionUtils.isEmpty(req.getIds()))
			return false;


		kquaKnghiemService.deleteByPhieuKnghiemIdIn(req.getIds());
		phieuKnghiemCluongHangRepository.deleteByIdIn(req.getIds());
		return true;
	}

	@Override
	public boolean exportToExcel(PhieuKnghiemCluongHangSearchReq objReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<PhieuKnghiemCluongHangRes> list = this.search(objReq).get().collect(Collectors.toList());

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
			XSSFSheet sheet = workbook.createSheet(SHEET_PHIEU_KIEM_NGHIEM_CHAT_LUONG_HANG);
			Row row0 = sheet.createRow(0);
			//STT

			ExportExcel.createCell(row0, 0, STT, style, sheet);
			ExportExcel.createCell(row0, 1, SO_PHIEU, style, sheet);
			ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
			ExportExcel.createCell(row0, 3, NGAY_BAN_GIAO_MAU, style, sheet);
			ExportExcel.createCell(row0, 4, CUC_DU_TRU_NHA_NUOC_KHU_VUC, style, sheet);
			ExportExcel.createCell(row0, 5, DON_VI_TO_CHUC_THU_NGHIEM, style, sheet);
			ExportExcel.createCell(row0, 6, SO_LUONG_MAU_HANG_KIEM_TRA, style, sheet);
			ExportExcel.createCell(row0, 7, TRANG_THAI, style, sheet);

			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeight(11);
			style.setFont(font);

			Row row;
			int startRowIndex = 1;

			for (PhieuKnghiemCluongHangRes item : list) {
				row = sheet.createRow(startRowIndex);
				ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
				ExportExcel.createCell(row, 1, item.getSoPhieu(), style, sheet);
				ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
				ExportExcel.createCell(row, 3, LocalDateTimeUtils.localDateToString(item.getNgayBanGiaoMau()), style, sheet);
				ExportExcel.createCell(row, 4, item.getTenDviCha(), style, sheet);
				ExportExcel.createCell(row, 5, item.getTenDvi(), style, sheet);
				ExportExcel.createCell(row, 6, item.getSoLuongMauHangKt() != null ? item.getSoLuongMauHangKt().toString() : 0, style, sheet);
				ExportExcel.createCell(row, 7, TrangThaiEnum.getTenById(item.getTrangThai()), style, sheet);
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

	private void validateSoPhieu(PhieuKnghiemCluongHang update, PhieuKnghiemCluongHangReq req) throws Exception {
		String so = req.getSoPhieu();
		if (!StringUtils.hasText(so))
			return;

		if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
			Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findFirstBySoPhieu(so);
			Long updateId = Optional.ofNullable(update).map(PhieuKnghiemCluongHang::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số phiếu " + so + " đã tồn tại");
		}
	}
}
