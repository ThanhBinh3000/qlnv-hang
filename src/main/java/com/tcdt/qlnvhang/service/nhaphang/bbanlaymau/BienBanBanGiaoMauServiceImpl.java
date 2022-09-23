package com.tcdt.qlnvhang.service.nhaphang.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanBanGiaoMauCt;
import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
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
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauCtRes;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public Page<BienBanBanGiaoMauRes> search(BienBanBanGiaoMauSearchReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
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
			response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
			response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
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

		this.validateSoBb(null, req);
		BienBanBanGiaoMau bienBienBanGiaoMau = new BienBanBanGiaoMau();
		BeanUtils.copyProperties(req, bienBienBanGiaoMau, "id");
		bienBienBanGiaoMau.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		bienBienBanGiaoMau.setNguoiTaoId(userInfo.getId());
		bienBienBanGiaoMau.setNgayTao(LocalDate.now());
		bienBienBanGiaoMau.setMaDvi(userInfo.getDvql());
		bienBienBanGiaoMau.setCapDvi(userInfo.getCapDvi());
		bienBienBanGiaoMau.setSo(getSo());
		bienBienBanGiaoMau.setNam(LocalDate.now().getYear());
		bienBienBanGiaoMau.setSoBienBan(String.format("%s/%s/%s-%s", bienBienBanGiaoMau.getSo(), bienBienBanGiaoMau.getNam(), "BBBG", userInfo.getMaPbb()));

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

		this.validateSoBb(optional.get(), req);
		BienBanBanGiaoMau bienBienBanGiaoMau = optional.get();
		BeanUtils.copyProperties(req, bienBienBanGiaoMau, "id");
		bienBienBanGiaoMau.setNguoiSuaId(userInfo.getId());
		bienBienBanGiaoMau.setNgaySua(LocalDate.now());
		bienBanBanGiaoMauRepository.save(bienBienBanGiaoMau);

		Map<Long, BienBanBanGiaoMauCt> mapChiTiet = bienBanBanGiaoMauCtRepository.findByBbBanGiaoMauIdIn(Collections.singleton(bienBienBanGiaoMau.getId()))
				.stream().collect(Collectors.toMap(BienBanBanGiaoMauCt::getId, Function.identity()));

		List<BienBanBanGiaoMauCt> chiTiets = this.saveListChiTiet(bienBienBanGiaoMau.getId(), req.getChiTiets(), mapChiTiet);
		bienBienBanGiaoMau.setChiTiets(chiTiets);

		if (!CollectionUtils.isEmpty(mapChiTiet.values()))
			bienBanBanGiaoMauCtRepository.deleteAll(mapChiTiet.values());
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
		 if (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(stReq.getTrangThai())) {
			if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
				return false;
			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId());
			bb.setNguoiGuiDuyetId(userInfo.getId());
			bb.setNgayGuiDuyet(LocalDate.now());
		} else if (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId().equals(stReq.getTrangThai())) {
			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
				return false;
			bb.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId().equals(stReq.getTrangThai())) {
			if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
				return false;

			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId().equals(stReq.getTrangThai())) {
			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
				return false;

			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else {
			throw new Exception("Bad request.");
		}

		return true;
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

		if (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId().equals(bb.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã đã duyệt");
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
//			Long id = req.getId();
			BienBanBanGiaoMauCt chiTiet = new BienBanBanGiaoMauCt();

//			if (id != null && id > 0) {
//				chiTiet = mapChiTiet.get(id);
//				if (chiTiet == null)
//					throw new Exception("Biên bản bàn giao mẫu chi tiết không tồn tại.");
//				mapChiTiet.remove(id);
//			}

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
		res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
		res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
		QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
		res.setMaDvi(donvi.getMaDvi());
		res.setTenDvi(donvi.getTenDvi());
		res.setMaQhns(donvi.getMaQhns());
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

		if (item.getHopDongId() != null) {
			Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(item.getHopDongId());
			if (!hopDong.isPresent()) {
				throw new Exception("Không tìm thấy hợp đồng");
			}
			res.setSoHopDong(hopDong.get().getSoHd());
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
		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BienBanBanGiaoMauRes> list = this.search(objReq).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;


		String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_BAN_GIAO, CUC_DU_TRU_NHA_NUOC_KHU_VUC_BEN_GIAO,
				DON_VI_THU_NGHIEM_BEN_NHAN, SO_LUONG_MAU_HANG_KIEM_TRA, TRANG_THAI};
		String filename = "Danh_sach_bien_ban_ban_giao_mau.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;

		try {
			for (int i = 0; i < list.size(); i++) {
				BienBanBanGiaoMauRes item = list.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i;
				objs[1] = item.getSoBienBan();
				objs[2] = item.getSoQuyetDinhNhap();
				objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayBanGiaoMau());
				objs[4] = item.getTenDvi();
				objs[5] = item.getTenDviBenNhan();
				objs[6] = item.getSoLuongMau();
				objs[7] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
				dataList.add(objs);
			}

			ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_BAN_GIAO_MAU, filename, rowsName, dataList, response);
			ex.export();
		} catch (Exception e) {
			log.error("Error export", e);
			return false;
		}

		return true;
	}

	private void validateSoBb(BienBanBanGiaoMau update, BienBanBanGiaoMauReq req) throws Exception {
		String soBB = req.getSoBienBan();
		if (!StringUtils.hasText(soBB))
			return;
		if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(soBB))) {
			Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findFirstBySoBienBan(soBB);
			Long updateId = Optional.ofNullable(update).map(BienBanBanGiaoMau::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số biên bản " + soBB + " đã tồn tại");
		}
	}

	@Override
	public Integer getSo() throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Integer so = bienBanBanGiaoMauRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
		so = Optional.ofNullable(so).orElse(0);
		so = so + 1;
		return so;
	}

	@Override
	public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
		BienBanBanGiaoMauSearchReq countReq = new BienBanBanGiaoMauSearchReq();
		countReq.setMaDvis(maDvis);
		BaseNhapHangCount count = new BaseNhapHangCount();

		countReq.setMaVatTuCha(Contains.LOAI_VTHH_THOC);
		count.setThoc(bienBanBanGiaoMauRepository.countBienBan(countReq));
		countReq.setMaVatTuCha(Contains.LOAI_VTHH_GAO);
		count.setGao(bienBanBanGiaoMauRepository.countBienBan(countReq));
		countReq.setMaVatTuCha(Contains.LOAI_VTHH_MUOI);
		count.setMuoi(bienBanBanGiaoMauRepository.countBienBan(countReq));
		countReq.setMaVatTuCha(Contains.LOAI_VTHH_VATTU);
		count.setVatTu(bienBanBanGiaoMauRepository.countBienBan(countReq));
		return count;
	}
}
