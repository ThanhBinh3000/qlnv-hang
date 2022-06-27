package com.tcdt.qlnvhang.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcdt.qlnvhang.enums.HhBbNghiemthuKlstStatusEnum;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinKeLot;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.request.search.HhBbNghiemthuKlstSearchReq;
import com.tcdt.qlnvhang.secification.HhBbNghiemthuKlstSpecification;
import com.tcdt.qlnvhang.service.HhBbNghiemthuKlstHdrService;
import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstDtl;
import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;

@Service
@Log4j2
public class HhBbNghiemthuKlstHdrServiceImpl extends BaseServiceImpl implements HhBbNghiemthuKlstHdrService {

	private static final String SHEET_BIEN_BAN_NGHIEM_THU_BAO_QUAN_LAN_DAU_NHAP = "Biên bản nghiệm thu bảo quản lần đầu nhập";
	private static final String STT = "STT";
	private static final String SO_BIEN_BAN = "Số Biên Bản";
	private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
	private static final String NGAY_NGHIEM_THU = "Ngày Nghiệm Thu";
	private static final String DIEM_KHO = "Điểm Kho";
	private static final String NHA_KHO = "Nhà Kho";
	private static final String NGAN_KHO = "Ngăn Kho";
	private static final String NGAN_LO = "Ngăn Lô";
	private static final String CHI_PHI_THUC_HIEN_TRONG_NAM = "Chi Phí Thực Hiện Trong Năm";
	private static final String CHI_PHI_THUC_HIEN_NAM_TRUOC = "Chi Phí Thực Hiện Năm Trước";
	private static final String TONG_GIA_TRI = "Tổng Giá Trị";
	private static final String TRANG_THAI = "Trạng Thái";

	@Autowired
	private HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;

	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private KtNganLoRepository ktNganLoRepository;

	@Override
	public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findBySoBb(objReq.getSoBb());
		if (qOptional.isPresent())
			throw new Exception("Số biên bản " + objReq.getSoBb() + " đã tồn tại");

		Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getQdgnvnxId());
		if (!qdNxOptional.isPresent())
			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhBbNghiemthuKlstHdr dataMap = new ModelMapper().map(objReq, HhBbNghiemthuKlstHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(HhBbNghiemthuKlstStatusEnum.DU_THAO.getId());
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setChildren1(fileDinhKemList);

		// Add thong tin chung
		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
		dataMap.setChildren(dtls1);

		dataMap.setNam(qdNxOptional.get().getNamNhap());
		dataMap.setMaDvi(userInfo.getDvql());
		dataMap.setCapDvi(userInfo.getCapDvi());
		hhBbNghiemthuKlstRepository.save(dataMap);

		return this.buildResponse(dataMap);

	}

	@Override
	public HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		if (!qOptional.get().getSoBb().equals(objReq.getSoBb())) {
			Optional<HhBbNghiemthuKlstHdr> qOpBban = hhBbNghiemthuKlstRepository.findBySoBb(objReq.getSoBb());
			if (qOpBban.isPresent())
				throw new Exception("Số biên bản " + objReq.getSoBb() + " đã tồn tại");
		}

		Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getQdgnvnxId());
		if (!qdNxOptional.isPresent())
			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhBbNghiemthuKlstHdr dataDTB = qOptional.get();
		HhBbNghiemthuKlstHdr dataMap = ObjectMapperUtils.map(objReq, HhBbNghiemthuKlstHdr.class);

		updateObjectToObject(dataDTB, dataMap);

		dataDTB.setNgaySua(getDateTimeNow());
		dataDTB.setNguoiSua(getUser().getUsername());
		dataDTB.setChildren1(fileDinhKemList);

		// Add thong tin chung
		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
		dataDTB.setChildren(dtls1);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		dataDTB.setNam(qdNxOptional.get().getNamNhap());
		dataDTB.setMaDvi(userInfo.getDvql());
		dataDTB.setCapDvi(userInfo.getCapDvi());
		hhBbNghiemthuKlstRepository.save(dataDTB);

		return this.buildResponse(dataDTB);

	}

	@Override
	public HhBbNghiemthuKlstHdr detail(String ids) throws Exception {

		UserInfo userInfo = UserUtils.getUserInfo();


		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhBbNghiemthuKlstDtl> dtls = ObjectMapperUtils.mapAll(qOptional.get().getChildren(),
				HhBbNghiemthuKlstDtl.class);
		UnitScaler.formatList(dtls, Contains.DVT_TAN);

		return this.buildResponse(qOptional.get());
	}

	@Override
	public Page<HhBbNghiemthuKlstHdr> colection(HhBbNghiemthuKlstSearchReq objReq, HttpServletRequest req)
			throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		int page = objReq.getPaggingReq().getPage();
		int limit = objReq.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit);

		Page<HhBbNghiemthuKlstHdr> qhKho = hhBbNghiemthuKlstRepository
				.findAll(HhBbNghiemthuKlstSpecification.buildSearchQuery(objReq), pageable);

		List<HhBbNghiemthuKlstHdr> data = qhKho.getContent();
		if (CollectionUtils.isEmpty(data))
			return qhKho;

		// Quyet dinh giao nhiem vu nhap hang
		Set<Long> qdNhapIds = data.stream().map(HhBbNghiemthuKlstHdr::getQdgnvnxId).collect(Collectors.toSet());
		Map<Long, HhQdGiaoNvuNhapxuatHdr> mapQdNhap = new HashMap<>();
		if (!CollectionUtils.isEmpty(qdNhapIds)) {
			mapQdNhap = hhQdGiaoNvuNhapxuatRepository.findByIdIn(qdNhapIds)
					.stream().collect(Collectors.toMap(HhQdGiaoNvuNhapxuatHdr::getId, Function.identity()));
		}

		// Ngan lo
		Set<String> maNganLos = data.stream().map(HhBbNghiemthuKlstHdr::getMaNganlo).collect(Collectors.toSet());
		Map<String, KtNganLo> mapNganLo = new HashMap<>();
		if (!CollectionUtils.isEmpty(maNganLos)) {
			mapNganLo = ktNganLoRepository.findByMaNganloIn(maNganLos)
					.stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));
		}

		for (HhBbNghiemthuKlstHdr hdr : data) {
			HhQdGiaoNvuNhapxuatHdr qdNhap = hdr.getQdgnvnxId() != null ? mapQdNhap.get(hdr.getQdgnvnxId()) : null;
			KtNganLo nganLo = StringUtils.hasText(hdr.getMaNganlo()) ? mapNganLo.get(hdr.getMaNganlo()) : null;
			this.buildResponseForList(hdr, qdNhap, nganLo);
		}

		return qhKho;
	}

	@Override
	public boolean approve(StatusReq stReq) throws Exception {

		UserInfo userInfo = UserUtils.getUserInfo();

		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		HhBbNghiemthuKlstHdr bb = optional.get();
		String trangThai = bb.getTrangThai();
		if (HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO.getId().equals(trangThai))
				return false;

			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId());
			bb.setNguoiGuiDuyet(userInfo.getUsername());
			bb.setNgayGuiDuyet(getDateTimeNow());
		} else if (HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;
			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId());
			bb.setNguoiPduyet(userInfo.getUsername());
			bb.setNgayPduyet(getDateTimeNow());
		} else if (HhBbNghiemthuKlstStatusEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId().equals(trangThai))
				return false;

			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.BAN_HANH.getId());
			bb.setNguoiPduyet(userInfo.getUsername());
			bb.setNgayPduyet(getDateTimeNow());
		} else if (HhBbNghiemthuKlstStatusEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;

			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.TU_CHOI.getId());
			bb.setNguoiPduyet(userInfo.getUsername());
			bb.setNgayPduyet(getDateTimeNow());
			bb.setLdoTuchoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		hhBbNghiemthuKlstRepository.save(bb);
		return true;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(idSearchReq.getId());

		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa với biên bản ở trạng thái bản nháp hoặc từ chối");

		hhBbNghiemthuKlstRepository.delete(optional.get());
	}

	@Override
	public boolean exportToExcel(HhBbNghiemthuKlstSearchReq objReq, HttpServletResponse response) throws Exception {
		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<HhBbNghiemthuKlstHdr> list = this.colection(objReq, null).get().collect(Collectors.toList());

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
			XSSFSheet sheet = workbook.createSheet(SHEET_BIEN_BAN_NGHIEM_THU_BAO_QUAN_LAN_DAU_NHAP);
			Row row0 = sheet.createRow(0);
			//STT

			ExportExcel.createCell(row0, 0, STT, style, sheet);
			ExportExcel.createCell(row0, 1, SO_BIEN_BAN, style, sheet);
			ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
			ExportExcel.createCell(row0, 3, NGAY_NGHIEM_THU, style, sheet);
			ExportExcel.createCell(row0, 4, DIEM_KHO, style, sheet);
			ExportExcel.createCell(row0, 5, NHA_KHO, style, sheet);
			ExportExcel.createCell(row0, 6, NGAN_KHO, style, sheet);
			ExportExcel.createCell(row0, 7, NGAN_LO, style, sheet);
			ExportExcel.createCell(row0, 8, CHI_PHI_THUC_HIEN_TRONG_NAM, style, sheet);
			ExportExcel.createCell(row0, 9, CHI_PHI_THUC_HIEN_NAM_TRUOC, style, sheet);
			ExportExcel.createCell(row0, 10, TONG_GIA_TRI, style, sheet);
			ExportExcel.createCell(row0, 11, TRANG_THAI, style, sheet);

			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeight(11);
			style.setFont(font);

			Row row;
			int startRowIndex = 1;

			for (HhBbNghiemthuKlstHdr item : list) {
				row = sheet.createRow(startRowIndex);
				ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
				ExportExcel.createCell(row, 1, item.getSoBb(), style, sheet);
				ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
				ExportExcel.createCell(row, 3, convertDateToString(item.getNgayNghiemThu()), style, sheet);
				ExportExcel.createCell(row, 4, item.getTenDiemkho(), style, sheet);
				ExportExcel.createCell(row, 5, item.getTenNhakho(), style, sheet);
				ExportExcel.createCell(row, 6, item.getTenNgankho(), style, sheet);
				ExportExcel.createCell(row, 7, item.getTenNganlo(), style, sheet);
				ExportExcel.createCell(row, 8,  Optional.ofNullable(item.getChiPhiThucHienTrongNam()).orElse(BigDecimal.valueOf(0D)), style, sheet);
				ExportExcel.createCell(row, 9, Optional.ofNullable(item.getChiPhiThucHienNamTruoc()).orElse(BigDecimal.valueOf(0D)), style, sheet);
				ExportExcel.createCell(row, 10, item.getTongGiaTri(), style, sheet);
				ExportExcel.createCell(row, 11, HhBbNghiemthuKlstStatusEnum.getTenById(item.getTrangThai()), style, sheet);
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

	private void buildResponseForList(HhBbNghiemthuKlstHdr bb, HhQdGiaoNvuNhapxuatHdr qdNhap, KtNganLo ktNganLo) {
		this.baseResponse(bb);
		if (qdNhap != null) {
			bb.setSoQuyetDinhNhap(qdNhap.getSoQd());
		}
		this.thongTinNganLo(bb, ktNganLo);

	}

	private HhBbNghiemthuKlstHdr buildResponse(HhBbNghiemthuKlstHdr bb) throws Exception {

		this.baseResponse(bb);
		if (bb.getQdgnvnxId() != null) {
			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(bb.getQdgnvnxId());
			if (!qdNhap.isPresent()) {
				throw new Exception("Không tìm thấy quyết định nhập");
			}
			bb.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
		}

		if (!StringUtils.hasText(bb.getMaNganlo()))
			return bb;

		KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(bb.getMaNganlo());
		this.thongTinNganLo(bb, nganLo);
		return bb;
	}

	private void baseResponse(HhBbNghiemthuKlstHdr bb) {
		bb.setTenTrangThai(HhBbNghiemthuKlstStatusEnum.getTenById(bb.getTrangThai()));
		bb.setTrangThaiDuyet(HhBbNghiemthuKlstStatusEnum.getTrangThaiDuyetById(bb.getTrangThai()));
		BigDecimal chiPhiTn = bb.getChildren().stream()
				.map(HhBbNghiemthuKlstDtl::getThanhTienTn)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal chiPhiNt = bb.getChildren().stream()
				.map(HhBbNghiemthuKlstDtl::getThanhTienQt)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal tongGiaTri = bb.getChildren().stream()
				.map(HhBbNghiemthuKlstDtl::getTongGtri)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		bb.setChiPhiThucHienTrongNam(chiPhiTn);
		bb.setChiPhiThucHienNamTruoc(chiPhiNt);
		bb.setTongGiaTri(tongGiaTri);
		bb.setTongGiaTriBangChu(MoneyConvert.doctienBangChu(tongGiaTri.toString(), null));
	}

	private void thongTinNganLo(HhBbNghiemthuKlstHdr bb, KtNganLo nganLo) {
		if (nganLo != null) {
			bb.setTenNganlo(nganLo.getTenNganlo());
			KtNganKho nganKho = nganLo.getParent();
			if (nganKho == null)
				return;

			bb.setTenNgankho(nganKho.getTenNgankho());
			bb.setMaNgankho(nganKho.getMaNgankho());
			KtNhaKho nhaKho = nganKho.getParent();
			if (nhaKho == null)
				return;

			bb.setTenNhakho(nhaKho.getTenNhakho());
			bb.setMaNhakho(nhaKho.getMaNhakho());
			KtDiemKho diemKho = nhaKho.getParent();
			if (diemKho == null)
				return;

			bb.setTenDiemkho(diemKho.getTenDiemkho());
			bb.setMaDiemkho(diemKho.getMaDiemkho());
		}
	}

	@Transactional
	@Override
	public boolean deleteMultiple(DeleteReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (CollectionUtils.isEmpty(req.getIds()))
			return false;
		hhBbNghiemthuKlstRepository.deleteByIdIn(req.getIds());
		return true;
	}
}
