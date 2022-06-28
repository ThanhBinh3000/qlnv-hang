package com.tcdt.qlnvhang.service.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMauCt;
import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bbanbangiaomau.BienBanBanGiaoMauCtRepository;
import com.tcdt.qlnvhang.repository.bbanbangiaomau.BienBanBanGiaoMauRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanBanGiaoMauReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauCtReq;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauCtRes;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class BienBanBanGiaoMauServiceImpl extends BaseServiceImpl implements BienBanBanGiaoMauService {

	private static final String SHEET_BIEN_BAN_BAN_GIAO_MAU = "Biên bản bàn giao mẫu";
	private static final String STT = "STT";
	private static final String SO_BIEN_BAN = "Số Biên Bản";
	private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
	private static final String NGAY_BAN_GIAO = "Ngày Bàn Giao";
	private static final String CUC_DU_TRU_NHA_NUOC_KHU_VUC_BEN_GIAO = "Cục Dự Trữ Nhà Nước Khu Vực (Bên Giao)";
	private static final String DON_VI_THU_NGHIEM_BEN_NHAN = "Đơn Vị Thử Nghiệm (Bên Nhận)";
	private static final String SO_LUONG_MAU_HANG_KIEM_TRA = "Số Lượng Mẫu Hàng Kiểm Tra";
	private static final String TRANG_THAI = "Trạng Thái";

	@Autowired
	private BienBanBanGiaoMauRepository bienBanBanGiaoMauRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private BienBanBanGiaoMauCtRepository bienBanBanGiaoMauCtRepository;

	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private BienBanLayMauRepository bienBanLayMauRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public Page<BienBanBanGiaoMauRes> search(BienBanBanGiaoMauSearchReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
		req.setMaDvi(userInfo.getDvql());
		List<Object[]> data = bienBanBanGiaoMauRepository.search(req, pageable);

		List<BienBanBanGiaoMauRes> responses = new ArrayList<>();
		for (Object[] o : data) {
			BienBanBanGiaoMauRes response = new BienBanBanGiaoMauRes();
			BienBanBanGiaoMau item = (BienBanBanGiaoMau) o[0];
			Long qdNhapId = (Long) o[1];
			String soQdNhap = (String) o[2];
			Long bbLayMauId = (Long) o[3];
			String soBbLayMau = (String) o[4];

			BeanUtils.copyProperties(item, response);
			response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(item.getTrangThai()));
			response.setTrangThaiDuyet(QlPhieuNhapKhoLtStatus.getTrangThaiDuyetById(item.getTrangThai()));
			response.setQdgnvnxId(qdNhapId);
			response.setSoQuyetDinhNhap(soQdNhap);
			response.setBbLayMauId(bbLayMauId);
			response.setSoBbLayMau(soBbLayMau);
			response.setTenDvi(userInfo.getTenDvi());
			responses.add(response);
		}

		return new PageImpl<>(responses, pageable, bienBanBanGiaoMauRepository.countBienBan(req));
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BienBanBanGiaoMauRes create(BienBanBanGiaoMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		BienBanBanGiaoMau bienBienBanGiaoMau = new BienBanBanGiaoMau();
		BeanUtils.copyProperties(req, bienBienBanGiaoMau, "id");
		bienBienBanGiaoMau.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		bienBienBanGiaoMau.setNguoiTaoId(userInfo.getId());
		bienBienBanGiaoMau.setNgayTao(LocalDate.now());
		bienBienBanGiaoMau.setMaDvi(userInfo.getDvql());
		bienBienBanGiaoMau.setCapDvi(userInfo.getCapDvi());
		bienBanBanGiaoMauRepository.save(bienBienBanGiaoMau);

		List<BienBanBanGiaoMauCt> chiTiets = this.saveListChiTiet(bienBienBanGiaoMau.getId(), req.getChiTiets(), new HashMap<>());
		bienBienBanGiaoMau.setChiTiets(chiTiets);

		return this.toResponse(bienBienBanGiaoMau);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BienBanBanGiaoMauRes update(BienBanBanGiaoMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findById(req.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		BienBanBanGiaoMau bienBienBanGiaoMau = optional.get();
		BeanUtils.copyProperties(req, bienBienBanGiaoMau, "id");
		bienBienBanGiaoMau.setNguoiSuaId(userInfo.getId());
		bienBienBanGiaoMau.setNgaySua(LocalDate.now());
		bienBanBanGiaoMauRepository.save(bienBienBanGiaoMau);

		Map<Long, BienBanBanGiaoMauCt> mapChiTiet = bienBanBanGiaoMauCtRepository.findByBbBanGiaoMauIdIn(Collections.singleton(bienBienBanGiaoMau.getId()))
				.stream().collect(Collectors.toMap(BienBanBanGiaoMauCt::getId, Function.identity()));

		List<BienBanBanGiaoMauCt> chiTiets = this.saveListChiTiet(bienBienBanGiaoMau.getId(), req.getChiTiets(), mapChiTiet);
		bienBienBanGiaoMau.setChiTiets(chiTiets);
		return this.toResponse(bienBienBanGiaoMau);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findById(stReq.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		BienBanBanGiaoMau bb = optional.get();
		String trangThai = bb.getTrangThai();
		if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO.getId().equals(trangThai))
				return false;

			bb.setTrangThai(TrangThaiEnum.DU_THAO_TRINH_DUYET.getId());
			bb.setNguoiGuiDuyetId(userInfo.getId());
			bb.setNgayGuiDuyet(LocalDate.now());
		} else if (TrangThaiEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;
			bb.setTrangThai(TrangThaiEnum.LANH_DAO_DUYET.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else if (TrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.LANH_DAO_DUYET.getId().equals(trangThai))
				return false;

			bb.setTrangThai(TrangThaiEnum.BAN_HANH.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else if (TrangThaiEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;

			bb.setTrangThai(TrangThaiEnum.TU_CHOI.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
			bb.setLdoTchoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		bienBanBanGiaoMauRepository.save(bb);
		return false;
	}

	@Override
	public BienBanBanGiaoMauRes detail(Long id) throws Exception {
		Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanBanGiaoMau item = optional.get();
		item.setChiTiets(bienBanBanGiaoMauCtRepository.findByBbBanGiaoMauIdIn(Collections.singleton(item.getId())));

		return this.toResponse(item);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanBanGiaoMau bb = optional.get();

		if (TrangThaiEnum.BAN_HANH.getId().equals(bb.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã ban hành");
		}
		bienBanBanGiaoMauCtRepository.deleteByBbBanGiaoMauIdIn(Collections.singleton(bb.getId()));
		bienBanBanGiaoMauRepository.deleteById(id);
		return true;
	}

	private List<BienBanBanGiaoMauCt> saveListChiTiet(Long parentId,
													  List<BienBanLayMauCtReq> chiTietReqs,
													  Map<Long, BienBanBanGiaoMauCt> mapChiTiet) throws Exception {
		List<BienBanBanGiaoMauCt> chiTiets = new ArrayList<>();
		for (BienBanLayMauCtReq req : chiTietReqs) {
			Long id = req.getId();
			BienBanBanGiaoMauCt chiTiet = new BienBanBanGiaoMauCt();

			if (id != null) {
				chiTiet = mapChiTiet.get(id);
				if (chiTiet == null)
					throw new Exception("Biên bản bàn giao mẫu chi tiết không tồn tại.");
				mapChiTiet.remove(id);
			}

			BeanUtils.copyProperties(req, chiTiet, "id");
			chiTiet.setBbBanGiaoMauId(parentId);
			chiTiets.add(chiTiet);
		}

		if (!CollectionUtils.isEmpty(chiTiets))
			bienBanBanGiaoMauCtRepository.saveAll(chiTiets);

		return chiTiets;
	}


	private BienBanBanGiaoMauRes toResponse(BienBanBanGiaoMau item) throws Exception {
		if (item == null)
			return null;

		BienBanBanGiaoMauRes res = new BienBanBanGiaoMauRes();
		BeanUtils.copyProperties(item, res);

		res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
		res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
		QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
		res.setMaDvi(donvi.getMaDvi());
		res.setTenDvi(donvi.getTenDvi());

		Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
		if (!CollectionUtils.isEmpty(maVatTus)) {
			Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
			if (CollectionUtils.isEmpty(vatTus))
				throw new Exception("Không tìm thấy vật tư");
			vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
					.ifPresent(v -> res.setTenVatTu(v.getTen()));
			vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
					.ifPresent(v -> res.setTenVatTuCha(v.getTen()));
		}


		if (item.getQdgnvnxId() != null) {
			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
			if (!qdNhap.isPresent()) {
				throw new Exception("Không tìm thấy quyết định nhập");
			}
			res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
		}

		if (item.getBbLayMauId() != null) {
			Optional<BienBanLayMau> bbLayMau = bienBanLayMauRepository.findById(item.getBbLayMauId());
			if (!bbLayMau.isPresent()) {
				throw new Exception("Không tìm thấy biên bản lấy mẫu");
			}
			res.setSoBbLayMau(bbLayMau.get().getSoBienBan());
		}

		List<BienBanBanGiaoMauCtRes> chiTiets = item.getChiTiets().stream().map(BienBanBanGiaoMauCtRes::new).collect(Collectors.toList());
		res.setChiTiets(chiTiets);
		return res;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deleteMultiple(DeleteReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (CollectionUtils.isEmpty(req.getIds()))
			return false;


		bienBanBanGiaoMauCtRepository.deleteByBbBanGiaoMauIdIn(req.getIds());
		bienBanBanGiaoMauRepository.deleteByIdIn(req.getIds());
		return true;
	}

	@Override
	public boolean exportToExcel(BienBanBanGiaoMauSearchReq objReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		objReq.setMaDvi(userInfo.getDvql());
		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BienBanBanGiaoMauRes> list = this.search(objReq).get().collect(Collectors.toList());

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
			XSSFSheet sheet = workbook.createSheet(SHEET_BIEN_BAN_BAN_GIAO_MAU);
			Row row0 = sheet.createRow(0);
			//STT

			ExportExcel.createCell(row0, 0, STT, style, sheet);
			ExportExcel.createCell(row0, 1, SO_BIEN_BAN, style, sheet);
			ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
			ExportExcel.createCell(row0, 3, NGAY_BAN_GIAO, style, sheet);
			ExportExcel.createCell(row0, 4, CUC_DU_TRU_NHA_NUOC_KHU_VUC_BEN_GIAO, style, sheet);
			ExportExcel.createCell(row0, 5, DON_VI_THU_NGHIEM_BEN_NHAN, style, sheet);
			ExportExcel.createCell(row0, 6, SO_LUONG_MAU_HANG_KIEM_TRA, style, sheet);
			ExportExcel.createCell(row0, 7, TRANG_THAI, style, sheet);

			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeight(11);
			style.setFont(font);

			Row row;
			int startRowIndex = 1;

			for (BienBanBanGiaoMauRes item : list) {
				row = sheet.createRow(startRowIndex);
				ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
				ExportExcel.createCell(row, 1, item.getSoBienBan(), style, sheet);
				ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
				ExportExcel.createCell(row, 3, LocalDateTimeUtils.localDateToString(item.getNgayBanGiaoMau()), style, sheet);
				ExportExcel.createCell(row, 4, item.getTenDvi(), style, sheet);
				ExportExcel.createCell(row, 5, item.getTenDviBenNhan(), style, sheet);
				ExportExcel.createCell(row, 6, item.getSoLuongMau(), style, sheet);
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
}
