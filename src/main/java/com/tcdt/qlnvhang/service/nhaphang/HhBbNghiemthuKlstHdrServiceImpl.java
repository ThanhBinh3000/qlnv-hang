package com.tcdt.qlnvhang.service.nhaphang;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcdt.qlnvhang.enums.HhBbNghiemthuKlstStatusEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongResponseDto;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
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

	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");
		this.validateSoBb(null, objReq);

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

		dataMap.setSo(getSo());
		dataMap.setNam(LocalDate.now().getYear());
		dataMap.setSoBb(String.format("%s/%s/%s-%s", dataMap.getSo(), dataMap.getNam(), "BBNTBQ", userInfo.getMaPbb()));

		hhBbNghiemthuKlstRepository.save(dataMap);

		return this.buildResponse(dataMap);

	}

	private void validateSoBb(HhBbNghiemthuKlstHdr update, HhBbNghiemthuKlstHdrReq req) throws Exception {
		String soBB = req.getSoBb();
		if (!StringUtils.hasText(soBB))
			return;

		if (update == null || (StringUtils.hasText(update.getSoBb()) && !update.getSoBb().equalsIgnoreCase(soBB))) {
			Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findFirstBySoBb(soBB);
			Long updateId = Optional.ofNullable(update).map(HhBbNghiemthuKlstHdr::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số biên bản " + soBB + " đã tồn tại");
		}
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

		this.validateSoBb(qOptional.get(), objReq);

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
		dataMap.setSo(dataDTB.getSo());
		dataMap.setNam(dataDTB.getNam());

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
		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
		Page<HhBbNghiemthuKlstHdr> qhKho = hhBbNghiemthuKlstRepository
				.findAll(HhBbNghiemthuKlstSpecification.buildSearchQuery(objReq), pageable);

		List<HhBbNghiemthuKlstHdr> data = qhKho.getContent();
		if (CollectionUtils.isEmpty(data))
			return qhKho;

		// Quyet dinh giao nhiem vu nhap hang
		Set<Long> qdNhapIds = data.stream()
				.map(HhBbNghiemthuKlstHdr::getQdgnvnxId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Map<Long, HhQdGiaoNvuNhapxuatHdr> mapQdNhap = new HashMap<>();
		if (!CollectionUtils.isEmpty(qdNhapIds)) {
			mapQdNhap = hhQdGiaoNvuNhapxuatRepository.findByIdIn(qdNhapIds)
					.stream().collect(Collectors.toMap(HhQdGiaoNvuNhapxuatHdr::getId, Function.identity()));
		}

		// Ngan lo
		Set<String> maNganLos = data.stream()
				.map(HhBbNghiemthuKlstHdr::getMaNganlo)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Map<String, KtNganLo> mapNganLo = new HashMap<>();
		if (!CollectionUtils.isEmpty(maNganLos)) {
			mapNganLo = ktNganLoRepository.findByMaNganloIn(maNganLos)
					.stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));
		}

		Set<Long> hopDongIds = data.stream()
				.map(HhBbNghiemthuKlstHdr::getHopDongId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		Map<Long, HhHopDongHdr> mapHopDong = new HashMap<>();
		if (!CollectionUtils.isEmpty(hopDongIds)) {
			mapHopDong = hhHopDongRepository.findByIdIn(hopDongIds)
					.stream().collect(Collectors.toMap(HhHopDongHdr::getId, Function.identity()));
		}

		for (HhBbNghiemthuKlstHdr hdr : data) {
			HhQdGiaoNvuNhapxuatHdr qdNhap = hdr.getQdgnvnxId() != null ? mapQdNhap.get(hdr.getQdgnvnxId()) : null;
			KtNganLo nganLo = StringUtils.hasText(hdr.getMaNganlo()) ? mapNganLo.get(hdr.getMaNganlo()) : null;
			HhHopDongHdr hopDong = hdr.getHopDongId() != null ? mapHopDong.get(hdr.getHopDongId()) : null;
			this.buildResponseForList(hdr, qdNhap, nganLo, hopDong);
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

		String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_NGHIEM_THU, DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO,
				CHI_PHI_THUC_HIEN_TRONG_NAM, CHI_PHI_THUC_HIEN_NAM_TRUOC, TONG_GIA_TRI, TRANG_THAI};
		String filename = "Danh_sach_bien_ban_nghiem_thu_bao_quan_lan_dau.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;

		try {
			for (int i = 0; i < list.size(); i++) {
				HhBbNghiemthuKlstHdr item = list.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i;
				objs[1] = item.getSoBb();
				objs[2] = item.getSoQuyetDinhNhap();
				objs[3] = convertDateToString(item.getNgayNghiemThu());
				objs[4] = item.getTenDiemkho();
				objs[5] = item.getTenNhakho();
				objs[6] = item.getTenNgankho();
				objs[7] = item.getTenNganlo();
				objs[8] = Optional.ofNullable(item.getChiPhiThucHienTrongNam()).orElse(BigDecimal.valueOf(0D));
				objs[9] = Optional.ofNullable(item.getChiPhiThucHienNamTruoc()).orElse(BigDecimal.valueOf(0D));
				objs[10] = item.getTongGiaTri();
				objs[11] = TrangThaiEnum.getTenById(item.getTrangThai());
				dataList.add(objs);
			}

			ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_NGHIEM_THU_BAO_QUAN_LAN_DAU_NHAP, filename, rowsName, dataList, response);
			ex.export();
		} catch (Exception e) {
			log.error("Error export", e);
			return false;
		}
		return true;
	}

	private void buildResponseForList(HhBbNghiemthuKlstHdr bb, HhQdGiaoNvuNhapxuatHdr qdNhap,
									  KtNganLo ktNganLo, HhHopDongHdr hopDong) {
		this.baseResponse(bb);
		if (qdNhap != null) {
			bb.setSoQuyetDinhNhap(qdNhap.getSoQd());
		}
		this.thongTinNganLo(bb, ktNganLo);
		if (hopDong != null) {
			bb.setSoHopDong(hopDong.getSoHd());
		}

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

		if (bb.getHopDongId() != null) {
			Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(bb.getHopDongId());
			if (!hopDong.isPresent()) {
				throw new Exception("Không tìm thấy hợp đồng");
			}
			bb.setSoHopDong(hopDong.get().getSoHd());
		}

		if (!StringUtils.hasText(bb.getMaNganlo()))
			return bb;

		KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(bb.getMaNganlo());
		this.thongTinNganLo(bb, nganLo);
		QlnvDmDonvi donvi = getDviByMa(bb.getMaDvi(), req);
		bb.setMaDvi(donvi.getMaDvi());
		bb.setTenDvi(donvi.getTenDvi());
		bb.setMaQhns(donvi.getMaQhns());
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

	@Override
	public Integer getSo() throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Integer so = hhBbNghiemthuKlstRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
		so = Optional.ofNullable(so).orElse(0);
		so = so + 1;
		return so;
	}
}
