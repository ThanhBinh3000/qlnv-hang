package com.tcdt.qlnvhang.service.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMauCt;
import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.enums.HhBbNghiemthuKlstStatusEnum;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauCtRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauCtReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauCtRes;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class BienBanLayMauServiceImpl extends BaseServiceImpl implements BienBanLayMauService {
	private static final String SHEET_BIEN_BAN_LAY_MAU = "Biên bản lấy mẫu";
	private static final String STT = "STT";
	private static final String SO_BIEN_BAN = "Số Biên Bản";
	private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
	private static final String NGAY_LAY_MAU = "Ngày Lấy Mẫu";
	private static final String SO_HOP_DONG = "Số Hợp Đồng";
	private static final String DIEM_KHO = "Điểm Kho";
	private static final String NHA_KHO = "Nhà Kho";
	private static final String NGAN_KHO = "Ngăn Kho";
	private static final String NGAN_LO = "Ngăn Lô";
	private static final String TRANG_THAI = "Trạng Thái";

	@Autowired
	private BienBanLayMauRepository bienBanLayMauRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private BienBanLayMauCtRepository bienBanLayMauCtRepository;

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private KtNganLoRepository ktNganLoRepository;

	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private QlBienBanNhapDayKhoLtRepository qlBienBanNhapDayKhoLtRepository;

	@Override
	public Page<BienBanLayMauRes> search(BienBanLayMauSearchReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
		List<Object[]> data = bienBanLayMauRepository.search(req, pageable);

		List<BienBanLayMauRes> responses = new ArrayList<>();
		for (Object[] o : data) {
			BienBanLayMauRes response = new BienBanLayMauRes();
			BienBanLayMau item = (BienBanLayMau) o[0];
			Long qdNhapId = (Long) o[1];
			String soQdNhap = (String) o[2];
			Long hopDongId = (Long) o[3];
			String soHopDong = (String) o[4];
			KtNganLo nganLo = o[5] != null ? (KtNganLo) o[5] : null;
			Long bbNhapDayKhoId = (Long) o[6];
			String soBbNhapDayKho = (String) o[7];

			BeanUtils.copyProperties(item, response);
			response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(item.getTrangThai()));
			response.setTrangThaiDuyet(QlPhieuNhapKhoLtStatus.getTrangThaiDuyetById(item.getTrangThai()));
			response.setQdgnvnxId(qdNhapId);
			response.setSoQuyetDinhNhap(soQdNhap);
			response.setHopDongId(hopDongId);
			response.setSoHopDong(soHopDong);
			response.setBbNhapDayKhoId(bbNhapDayKhoId);
			response.setSoBbNhapDayKho(soBbNhapDayKho);
			this.thongTinNganLo(response, nganLo);
			responses.add(response);
		}
		return new PageImpl<>(responses, pageable, bienBanLayMauRepository.countBienBan(req));
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BienBanLayMauRes create(BienBanLayMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		this.validateSoBb(null, req);
		BienBanLayMau bienBienLayMau = new BienBanLayMau();
		BeanUtils.copyProperties(req, bienBienLayMau, "id");
		bienBienLayMau.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		bienBienLayMau.setNguoiTaoId(userInfo.getId());
		bienBienLayMau.setNgayTao(LocalDate.now());
		bienBienLayMau.setMaDvi(userInfo.getDvql());
		bienBienLayMau.setCapDvi(userInfo.getCapDvi());
		bienBanLayMauRepository.save(bienBienLayMau);

		List<BienBanLayMauCt> chiTiets = this.saveListChiTiet(bienBienLayMau.getId(), req.getChiTiets(), new HashMap<>());
		bienBienLayMau.setChiTiets(chiTiets);

		return this.toResponse(bienBienLayMau);
	}

	private List<BienBanLayMauCt> saveListChiTiet(Long parentId, List<BienBanLayMauCtReq> chiTietReqs, Map<Long, BienBanLayMauCt> mapChiTiet) throws Exception {
		List<BienBanLayMauCt> chiTiets = new ArrayList<>();
		for (BienBanLayMauCtReq req : chiTietReqs) {
			Long id = req.getId();
			BienBanLayMauCt chiTiet = new BienBanLayMauCt();

			if (id != null) {
				chiTiet = mapChiTiet.get(id);
				if (chiTiet == null)
					throw new Exception("Biên bản lấy mẫu chi tiết không tồn tại.");
				mapChiTiet.remove(id);
			}

			BeanUtils.copyProperties(req, chiTiet, "id");
			chiTiet.setBbLayMauId(parentId);
			chiTiets.add(chiTiet);
		}

		if (!CollectionUtils.isEmpty(chiTiets))
			bienBanLayMauCtRepository.saveAll(chiTiets);

		return chiTiets;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BienBanLayMauRes update(BienBanLayMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(req.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		this.validateSoBb(optional.get(), req);
		BienBanLayMau bienBienLayMau = optional.get();
		BeanUtils.copyProperties(req, bienBienLayMau, "id");
		bienBienLayMau.setNguoiSuaId(userInfo.getId());
		bienBienLayMau.setNgaySua(LocalDate.now());
		bienBanLayMauRepository.save(bienBienLayMau);

		Map<Long, BienBanLayMauCt> mapChiTiet = bienBanLayMauCtRepository.findByBbLayMauIdIn(Collections.singleton(bienBienLayMau.getId()))
		.stream().collect(Collectors.toMap(BienBanLayMauCt::getId, Function.identity()));

		List<BienBanLayMauCt> chiTiets = this.saveListChiTiet(bienBienLayMau.getId(), req.getChiTiets(), mapChiTiet);
		bienBienLayMau.setChiTiets(chiTiets);

		if (!CollectionUtils.isEmpty(mapChiTiet.values()))
			bienBanLayMauCtRepository.deleteAll(mapChiTiet.values());

		return this.toResponse(bienBienLayMau);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(stReq.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		BienBanLayMau bb = optional.get();
		String trangThai = bb.getTrangThai();
		if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO.getId().equals(trangThai))
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
			bb.setLyDoTuChoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		bienBanLayMauRepository.save(bb);
		return false;
	}

	@Override
	public BienBanLayMauRes detail(Long id) throws Exception {
		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanLayMau item = optional.get();
		item.setChiTiets(bienBanLayMauCtRepository.findByBbLayMauIdIn(Collections.singleton(item.getId())));

		return this.toResponse(optional.get());
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanLayMau bb = optional.get();

		if (TrangThaiEnum.BAN_HANH.getId().equals(bb.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã ban hành");
		}
		bienBanLayMauCtRepository.deleteByBbLayMauIdIn(Collections.singleton(bb.getId()));
		bienBanLayMauRepository.deleteById(id);
		return true;
	}


	private BienBanLayMauRes toResponse(BienBanLayMau item) throws Exception {
		if (item == null)
			return null;

		BienBanLayMauRes res = new BienBanLayMauRes();
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

		if (item.getHopDongId() != null) {
			Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findById(item.getHopDongId());
			if (!qOpHdong.isPresent())
				throw new Exception("Hợp đồng không tồn tại");

			res.setSoHopDong(qOpHdong.get().getSoHd());
		}

		if (item.getBbNhapDayKhoId() != null) {
			Optional<QlBienBanNhapDayKhoLt> bbNhapDayKho = qlBienBanNhapDayKhoLtRepository.findById(item.getBbNhapDayKhoId());
			if (!bbNhapDayKho.isPresent())
				throw new Exception("Biên bản nhập đầy kho không tồn tại");

			res.setSoBienBan(bbNhapDayKho.get().getSoBienBan());
			if (StringUtils.hasText(bbNhapDayKho.get().getMaNganLo())) {
				KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(bbNhapDayKho.get().getMaNganLo());
				this.thongTinNganLo(res, nganLo);
			}
		}

		List<BienBanLayMauCtRes> chiTiets = item.getChiTiets().stream().map(BienBanLayMauCtRes::new).collect(Collectors.toList());
		res.setChiTiets(chiTiets);
		return res;
	}

	private void thongTinNganLo(BienBanLayMauRes item, KtNganLo nganLo) {
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


		bienBanLayMauCtRepository.deleteByBbLayMauIdIn(req.getIds());
		bienBanLayMauRepository.deleteByIdIn(req.getIds());
		return true;
	}

	@Override
	public boolean exportToExcel(BienBanLayMauSearchReq objReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BienBanLayMauRes> list = this.search(objReq).get().collect(Collectors.toList());

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
			XSSFSheet sheet = workbook.createSheet(SHEET_BIEN_BAN_LAY_MAU);
			Row row0 = sheet.createRow(0);
			//STT

			ExportExcel.createCell(row0, 0, STT, style, sheet);
			ExportExcel.createCell(row0, 1, SO_BIEN_BAN, style, sheet);
			ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
			ExportExcel.createCell(row0, 3, NGAY_LAY_MAU, style, sheet);
			ExportExcel.createCell(row0, 4, SO_HOP_DONG, style, sheet);
			ExportExcel.createCell(row0, 5, DIEM_KHO, style, sheet);
			ExportExcel.createCell(row0, 6, NHA_KHO, style, sheet);
			ExportExcel.createCell(row0, 7, NGAN_KHO, style, sheet);
			ExportExcel.createCell(row0, 8, NGAN_LO, style, sheet);
			ExportExcel.createCell(row0, 9, TRANG_THAI, style, sheet);

			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontHeight(11);
			style.setFont(font);

			Row row;
			int startRowIndex = 1;

			for (BienBanLayMauRes item : list) {
				row = sheet.createRow(startRowIndex);
				ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
				ExportExcel.createCell(row, 1, item.getSoBienBan(), style, sheet);
				ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
				ExportExcel.createCell(row, 3, item.getSoHopDong(), style, sheet);
				ExportExcel.createCell(row, 4, LocalDateTimeUtils.localDateToString(item.getNgayLayMau()), style, sheet);
				ExportExcel.createCell(row, 5, item.getTenDiemKho(), style, sheet);
				ExportExcel.createCell(row, 6, item.getTenNhaKho(), style, sheet);
				ExportExcel.createCell(row, 7, item.getTenNganKho(), style, sheet);
				ExportExcel.createCell(row, 8, item.getTenNganLo(), style, sheet);
				ExportExcel.createCell(row, 9, TrangThaiEnum.getTenById(item.getTrangThai()), style, sheet);
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

	private void validateSoBb(BienBanLayMau update, BienBanLayMauReq req) throws Exception {
		String soBB = req.getSoBienBan();
		if (!StringUtils.hasText(soBB))
			return;
		if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(soBB))) {
			Optional<BienBanLayMau> optional = bienBanLayMauRepository.findFirstBySoBienBan(soBB);
			Long updateId = Optional.ofNullable(update).map(BienBanLayMau::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số biên bản " + soBB + " đã tồn tại");
		}
	}
}
