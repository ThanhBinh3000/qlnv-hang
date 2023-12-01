package com.tcdt.qlnvhang.service.nhaphang.bbanlaymau;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMauCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauCtRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong.BienBanLayMauPreview;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauCtReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class BienBanLayMauServiceImpl extends BaseServiceImpl implements BienBanLayMauService {

	@Autowired
	private BienBanLayMauRepository bienBanLayMauRepository;

	@Autowired
	private BienBanLayMauCtRepository bienBanLayMauCtRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private NhBbNhapDayKhoRepository nhBbNhapDayKhoRepository;

	@Autowired
	private NhBienBanGuiHangRepository nhBienBanGuiHangRepository;
	@Autowired
	private FileDinhKemService fileDinhKemService;

	@Override
	public Page<BienBanLayMau> searchPage(BienBanLayMauReq objReq) {
		Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<BienBanLayMau> bienBanLayMaus = bienBanLayMauRepository.selectPage(pageable);
		Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
		Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
		bienBanLayMaus.getContent().forEach(x -> {
			x.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(x.getTrangThai()));
			x.setTenLoaiVthh(listDanhMucHangHoa.get(x.getLoaiVthh()));
			x.setTenCloaiVthh(listDanhMucHangHoa.get(x.getCloaiVthh()));
			x.setTenDvi(listDanhMucDvi.get(x.getMaDvi()));
			x.setTenDiemKho(listDanhMucDvi.get(x.getMaDiemKho()));
			x.setTenNhaKho(listDanhMucDvi.get(x.getMaNhaKho()));
			x.setTenNganKho(listDanhMucDvi.get(x.getMaNganKho()));
			x.setTenLoKho(listDanhMucDvi.get(x.getMaLoKho()));
		});
		return bienBanLayMaus;
	}

	@Override
	public BienBanLayMau create(BienBanLayMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null){
			throw new Exception("Bad request.");
		}

		BienBanLayMau bienBienLayMau = new BienBanLayMau();
		BeanUtils.copyProperties(req, bienBienLayMau, "id");
		bienBienLayMau.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		bienBienLayMau.setNguoiTaoId(userInfo.getId());
		bienBienLayMau.setNgayTao(new Date());
		bienBienLayMau.setMaDvi(userInfo.getDvql());
		bienBienLayMau.setId(Long.valueOf(req.getSoBienBan().split("/")[0]));
		bienBanLayMauRepository.save(bienBienLayMau);
		setFileDinhKem(req, bienBienLayMau.getId());
		List<BienBanLayMauCt> chiTiets = this.saveListChiTiet(bienBienLayMau.getId(), req.getChiTiets());
		bienBienLayMau.setChiTiets(chiTiets);

		return bienBienLayMau;
	}

	private void setFileDinhKem (BienBanLayMauReq req, Long id){
		fileDinhKemService.delete(id, Lists.newArrayList("NH_BB_LAY_MAU"));
		if (!DataUtils.isNullOrEmpty(req.getListFileDinhKemBb())) {
			fileDinhKemService.saveListFileDinhKem(req.getListFileDinhKemBb(), id, "NH_BB_LAY_MAU");
		}
		fileDinhKemService.delete(id, Lists.newArrayList("NH_BB_LAY_MAU_ANH"));
		if (!DataUtils.isNullOrEmpty(req.getListFileDinhKemAnh())) {
			fileDinhKemService.saveListFileDinhKem(req.getListFileDinhKemAnh(), id, "NH_BB_LAY_MAU_ANH");
		}
		fileDinhKemService.delete(id, Lists.newArrayList("NH_BB_LAY_MAU_CAN_CU"));
		if (!DataUtils.isNullOrEmpty(req.getListCcPhapLy())) {
			fileDinhKemService.saveListFileDinhKem(req.getListCcPhapLy(), id, "NH_BB_LAY_MAU_CAN_CU");
		}
	}

	@Override
	public BienBanLayMau update(BienBanLayMauReq req) throws Exception {
				UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(req.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		BienBanLayMau bienBienLayMau = optional.get();
		BeanUtils.copyProperties(req, bienBienLayMau, "id");
		bienBienLayMau.setNguoiSuaId(userInfo.getId());
		bienBienLayMau.setNgaySua(new Date());
		bienBanLayMauRepository.save(bienBienLayMau);
		setFileDinhKem(req, bienBienLayMau.getId());

		bienBanLayMauCtRepository.deleteByBbLayMauId(bienBienLayMau.getId());
		this.saveListChiTiet(bienBienLayMau.getId(), req.getChiTiets());

		return bienBienLayMau;
	}

	@Override
	public BienBanLayMau detail(Long id) throws Exception {
		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanLayMau item = optional.get();
		Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
		Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
		item.setChiTiets(bienBanLayMauCtRepository.findByBbLayMauIdIn(Collections.singleton(item.getId())));
		item.setTenLoaiVthh(listDanhMucHangHoa.get(item.getLoaiVthh()));
		item.setTenCloaiVthh(listDanhMucHangHoa.get(item.getCloaiVthh()));
		item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
		item.setTenDiemKho(listDanhMucDvi.get(item.getMaDiemKho()));
		item.setTenNhaKho(listDanhMucDvi.get(item.getMaNhaKho()));
		item.setTenNganKho(listDanhMucDvi.get(item.getMaNganKho()));
		item.setTenLoKho(listDanhMucDvi.get(item.getMaLoKho()));
		item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? null : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
		item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? null : userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
		item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
		if(!ObjectUtils.isEmpty(item.getIdBbNhapDayKho())){
			Optional<NhBbNhapDayKho> byId = nhBbNhapDayKhoRepository.findById(item.getIdBbNhapDayKho());
			byId.ifPresent(item::setBbNhapDayKho);
		}
		if(!ObjectUtils.isEmpty(item.getIdBbGuiHang())){
			Optional<NhBienBanGuiHang> byId = nhBienBanGuiHangRepository.findById(item.getIdBbGuiHang());
			byId.ifPresent(item::setBbGuiHang);
		}
		List<FileDinhKem> fileDinhKem = fileDinhKemService.search(item.getId(), Collections.singletonList("NH_BB_LAY_MAU"));
		item.setListFileDinhKemBb(fileDinhKem);
		List<FileDinhKem> fileDinhKemBb = fileDinhKemService.search(item.getId(), Collections.singletonList("NH_BB_LAY_MAU_ANH"));
		item.setListFileDinhKemAnh(fileDinhKemBb);
		List<FileDinhKem> fileDinhKemCc = fileDinhKemService.search(item.getId(), Collections.singletonList("NH_BB_LAY_MAU_CAN_CU"));
		item.setListCcPhapLy(fileDinhKemCc);
		return item;
	}

	@Override
	public BienBanLayMau approve(BienBanLayMauReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
			throw new Exception("Bad Request");
		}

		if (StringUtils.isEmpty(req.getId())){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(req.getId());
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		BienBanLayMau phieu = optional.get();

		String status = req.getTrangThai() + phieu.getTrangThai();
		switch (status) {
			case Contains.CHODUYET_LDCC + Contains.DUTHAO:
			case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
				phieu.setNguoiGuiDuyetId(userInfo.getId());
				phieu.setNgayGuiDuyet(new Date());
				break;
			case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(new Date());
				phieu.setLyDoTuChoi(req.getLyDoTuChoi());
				break;
			case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
				phieu.setNguoiPduyetId(userInfo.getId());
				phieu.setNgayPduyet(new Date());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		phieu.setTrangThai(req.getTrangThai());
		bienBanLayMauRepository.save(phieu);
		return phieu;
	}

	@Override
	public void delete(Long id) throws Exception {
		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanLayMau bb = optional.get();

		if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(bb.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã đã duyệt");
		}
		bienBanLayMauCtRepository.deleteByBbLayMauIdIn(Collections.singleton(bb.getId()));
		bienBanLayMauRepository.deleteById(id);
	}

	@Override
	public void deleteMulti(List<Long> listMulti) {

	}

	@Override
	public void export(BienBanLayMauReq req, HttpServletResponse response) throws Exception {
	}

//	@Override
//	public Page<BienBanLayMauRes> search(BienBanLayMauSearchReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//		List<Object[]> data = bienBanLayMauRepository.search(req, pageable);
//
//		List<BienBanLayMauRes> responses = new ArrayList<>();
//		for (Object[] o : data) {
//			BienBanLayMauRes response = new BienBanLayMauRes();
//			BienBanLayMau item = (BienBanLayMau) o[0];
//			Long qdNhapId = (Long) o[1];
//			String soQdNhap = (String) o[2];
//			Long hopDongId = (Long) o[3];
//			String soHopDong = (String) o[4];
//			KtNganLo nganLo = o[5] != null ? (KtNganLo) o[5] : o[6] != null ? (KtNganLo) o[6] : null;
//
//			BeanUtils.copyProperties(item, response);
//			response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//			response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//			response.setQdgnvnxId(qdNhapId);
//			response.setSoQuyetDinhNhap(soQdNhap);
//			response.setHopDongId(hopDongId);
//			response.setSoHopDong(soHopDong);
//			this.thongTinNganLo(response, nganLo);
//			responses.add(response);
//		}
//		return new PageImpl<>(responses, pageable, bienBanLayMauRepository.countBienBan(req));
//	}
//
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public BienBanLayMau create(BienBanLayMauReq req) throws Exception {
//		UserInfo userInfo = SecurityContextService.getUser();
//		if (userInfo == null)
//			throw new Exception("Bad request.");
//
//		this.validateSoBb(null, req);
//		BienBanLayMau bienBienLayMau = new BienBanLayMau();
//		BeanUtils.copyProperties(req, bienBienLayMau, "id");
//		bienBienLayMau.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//		bienBienLayMau.setNguoiTaoId(userInfo.getId());
//		bienBienLayMau.setNgayTao(LocalDate.now());
//		bienBienLayMau.setMaDvi(userInfo.getDvql());
//		bienBanLayMauRepository.save(bienBienLayMau);
//
//		List<BienBanLayMauCt> chiTiets = this.saveListChiTiet(bienBienLayMau.getId(), req.getChiTiets(), new HashMap<>());
//		bienBienLayMau.setChiTiets(chiTiets);
//
//		return bienBienLayMau;
//	}
//
	private List<BienBanLayMauCt> saveListChiTiet(Long parentId, List<BienBanLayMauCtReq> chiTietReqs) throws Exception {
		List<BienBanLayMauCt> chiTiets = new ArrayList<>();
		for (BienBanLayMauCtReq req : chiTietReqs) {
			BienBanLayMauCt chiTiet = new BienBanLayMauCt();
			BeanUtils.copyProperties(req, chiTiet, "id");
			chiTiet.setBbLayMauId(parentId);
			chiTiets.add(chiTiet);
		}

		if (!CollectionUtils.isEmpty(chiTiets)){
			bienBanLayMauCtRepository.saveAll(chiTiets);
		}
		return chiTiets;
	}

	@Override
	public ReportTemplateResponse preview(BienBanLayMauReq req) throws Exception {
		BienBanLayMau bienBanLayMau = detail(req.getId());
		if (bienBanLayMau == null) {
			throw new Exception("Biên bản lấy mẫu không tồn tại.");
		}
		if (bienBanLayMau.getCloaiVthh() == null) {
			bienBanLayMau.setTenLoaiVthh(bienBanLayMau.getTenCloaiVthh().toUpperCase());
		} else {
			bienBanLayMau.setTenCloaiVthh(bienBanLayMau.getTenCloaiVthh().toUpperCase());
		}
		BienBanLayMauPreview object = new BienBanLayMauPreview();
		ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		object.setHdr(bienBanLayMau);
		if (bienBanLayMau.getBbNhapDayKho() != null && bienBanLayMau.getBbNhapDayKho().getNgayHd()!= null ) {
			object.setNgayHd(convertDate(bienBanLayMau.getBbNhapDayKho().getNgayHd()));
		}
		if (bienBanLayMau.getNgayTao() != null) {
			object.setNgayTao(convertDate(bienBanLayMau.getNgayTao()));
		}
		object.setPpLayMau(bienBanLayMau.getPpLayMau().split("-")[1]);
		if (bienBanLayMau.getChiTieuKiemTra() != null) {
			List<String> listChiTieu = Arrays.asList(bienBanLayMau.getChiTieuKiemTra().split(";"));
			object.setChiTieuKiemTra(new ArrayList<>());
			listChiTieu.forEach(item -> {
				object.getChiTieuKiemTra().add(extractTextAfterDash(item));
			});
		}
		object.setTenDviUpper(bienBanLayMau.getTenDvi().toUpperCase());
		return docxToPdfConverter.convertDocxToPdf(inputStream, object);
	}

	private static String extractTextAfterDash(String input) {
		String regex = "\\-(.+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1).trim();
		} else {
			return "";
		}
	}
//
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public BienBanLayMau update(BienBanLayMauReq req) throws Exception {
//		UserInfo userInfo = SecurityContextService.getUser();
//		if (userInfo == null)
//			throw new Exception("Bad request.");
//
//		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(req.getId());
//		if (!optional.isPresent()) {
//			throw new Exception("Không tìm thấy dữ liệu.");
//		}
//
//		this.validateSoBb(optional.get(), req);
//		BienBanLayMau bienBienLayMau = optional.get();
//		BeanUtils.copyProperties(req, bienBienLayMau, "id");
//		bienBienLayMau.setNguoiSuaId(userInfo.getId());
//		bienBienLayMau.setNgaySua(LocalDate.now());
//		bienBanLayMauRepository.save(bienBienLayMau);
//
//		Map<Long, BienBanLayMauCt> mapChiTiet = bienBanLayMauCtRepository.findByBbLayMauIdIn(Collections.singleton(bienBienLayMau.getId()))
//		.stream().collect(Collectors.toMap(BienBanLayMauCt::getId, Function.identity()));
//
//		List<BienBanLayMauCt> chiTiets = this.saveListChiTiet(bienBienLayMau.getId(), req.getChiTiets(), mapChiTiet);
//		bienBienLayMau.setChiTiets(chiTiets);
//
//		if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//			bienBanLayMauCtRepository.deleteAll(mapChiTiet.values());
//
//		return bienBienLayMau;
//	}
//
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public boolean updateStatus(StatusReq stReq) throws Exception {
//		UserInfo userInfo = SecurityContextService.getUser();
//		if (userInfo == null)
//			throw new Exception("Bad request.");
//
//		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(stReq.getId());
//		if (!optional.isPresent()) {
//			throw new Exception("Không tìm thấy dữ liệu.");
//		}
//
//		BienBanLayMau bb = optional.get();
//		String trangThai = bb.getTrangThai();
//		if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//			bb.setNguoiGuiDuyetId(userInfo.getId());
//			bb.setNgayGuiDuyet(LocalDate.now());
//		} else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//			bb.setNguoiPduyetId(userInfo.getId());
//			bb.setNgayPduyet(LocalDate.now());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//			bb.setNguoiPduyetId(userInfo.getId());
//			bb.setNgayPduyet(LocalDate.now());
//		} else {
//			throw new Exception("Bad request.");
//		}
//
//		return true;
//	}
//
//	@Override
//	public BienBanLayMau detail(Long id) throws Exception {
//		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu.");
//
//		BienBanLayMau item = optional.get();
//		item.setChiTiets(bienBanLayMauCtRepository.findByBbLayMauIdIn(Collections.singleton(item.getId())));
//
//		return optional.get();
//	}
//
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public boolean delete(Long id) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu.");
//
//		BienBanLayMau bb = optional.get();
//
//		if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(bb.getTrangThai())) {
//			throw new Exception("Không thể xóa đề xuất điều chỉnh đã đã duyệt");
//		}
//		bienBanLayMauCtRepository.deleteByBbLayMauIdIn(Collections.singleton(bb.getId()));
//		bienBanLayMauRepository.deleteById(id);
//		return true;
//	}
//
//
////	private BienBanLayMauRes toResponse(BienBanLayMau item) throws Exception {
////		if (item == null)
////			return null;
////
////		BienBanLayMauRes res = new BienBanLayMauRes();
////		BeanUtils.copyProperties(item, res);
////
////		res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
////		res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
////		QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
////		res.setMaDvi(donvi.getMaDvi());
////		res.setTenDvi(donvi.getTenDvi());
////		res.setMaQhns(donvi.getMaQhns());
////		Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
////		if (!CollectionUtils.isEmpty(maVatTus)) {
////			Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
////			if (CollectionUtils.isEmpty(vatTus))
////				throw new Exception("Không tìm thấy vật tư");
////			vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
////					.ifPresent(v -> res.setTenVatTu(v.getTen()));
////			vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
////					.ifPresent(v -> res.setTenVatTuCha(v.getTen()));
////		}
////
////
////		if (item.getQdgnvnxId() != null) {
////			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
////			if (!qdNhap.isPresent()) {
////				throw new Exception("Không tìm thấy quyết định nhập");
////			}
////			res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
////		}
////
////		if (item.getHopDongId() != null) {
////			Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findById(item.getHopDongId());
////			if (!qOpHdong.isPresent())
////				throw new Exception("Hợp đồng không tồn tại");
////
////			res.setSoHopDong(qOpHdong.get().getSoHd());
////		}
////
////		if (item.getBbNhapDayKhoId() != null) {
////			Optional<QlBienBanNhapDayKhoLt> bbNhapDayKho = qlBienBanNhapDayKhoLtRepository.findById(item.getBbNhapDayKhoId());
////			if (!bbNhapDayKho.isPresent())
////				throw new Exception("Biên bản nhập đầy kho không tồn tại");
////
////			res.setSoBbNhapDayKho(bbNhapDayKho.get().getSoBienBan());
////			if (StringUtils.hasText(bbNhapDayKho.get().getMaNganLo())) {
////				KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(bbNhapDayKho.get().getMaNganLo());
////				this.thongTinNganLo(res, nganLo);
////			}
////		}
////
////		if (item.getBbGuiHangId() != null) {
////			Optional<NhBienBanGuiHang> bbGuiHang = nhBienBanGuiHangRepository.findById(item.getBbGuiHangId());
////			if (!bbGuiHang.isPresent())
////				throw new Exception("Biên bản gửi hàng không tồn tại");
////
////			res.setSoBbGuiHang(bbGuiHang.get().getSoBienBan());
////		}
////
////		List<BienBanLayMauCtRes> chiTiets = item.getChiTiets().stream().map(BienBanLayMauCtRes::new).collect(Collectors.toList());
////		res.setChiTiets(chiTiets);
////		return res;
////	}
//
//	private void thongTinNganLo(BienBanLayMauRes item, KtNganLo nganLo) {
//		if (nganLo != null) {
//			item.setTenNganLo(nganLo.getTenNganlo());
//			KtNganKho nganKho = nganLo.getParent();
//			if (nganKho == null)
//				return;
//
//			item.setTenNganKho(nganKho.getTenNgankho());
//			item.setMaNganKho(nganKho.getMaNgankho());
//			KtNhaKho nhaKho = nganKho.getParent();
//			if (nhaKho == null)
//				return;
//
//			item.setTenNhaKho(nhaKho.getTenNhakho());
//			item.setMaNhaKho(nhaKho.getMaNhakho());
//			KtDiemKho diemKho = nhaKho.getParent();
//			if (diemKho == null)
//				return;
//
//			item.setTenDiemKho(diemKho.getTenDiemkho());
//			item.setMaDiemKho(diemKho.getMaDiemkho());
//		}
//	}
//
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public boolean deleteMultiple(DeleteReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (CollectionUtils.isEmpty(req.getIds()))
//			return false;
//
//
//		bienBanLayMauCtRepository.deleteByBbLayMauIdIn(req.getIds());
//		bienBanLayMauRepository.deleteByIdIn(req.getIds());
//		return true;
//	}
//
//	@Override
//	public boolean exportToExcel(BienBanLayMauSearchReq objReq, HttpServletResponse response) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//		List<BienBanLayMauRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//		if (CollectionUtils.isEmpty(list))
//			return true;
//
//		String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_LAY_MAU, SO_HOP_DONG,
//				DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//		String filename = "Danh_sach_bien_ban_lay_mau.xlsx";
//
//		List<Object[]> dataList = new ArrayList<Object[]>();
//		Object[] objs = null;
//
//		try {
//			for (int i = 0; i < list.size(); i++) {
//				BienBanLayMauRes item = list.get(i);
//				objs = new Object[rowsName.length];
//				objs[0] = i;
//				objs[1] = item.getSoBienBan();
//				objs[2] = item.getSoQuyetDinhNhap();
//				objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayLayMau());
//				objs[4] = item.getSoHopDong();
//				objs[5] = item.getTenDiemKho();
//				objs[6] = item.getTenNhaKho();
//				objs[7] = item.getTenNganKho();
//				objs[8] = item.getTenNganLo();
//				objs[9] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//				dataList.add(objs);
//			}
//
//			ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_LAY_MAU, filename, rowsName, dataList, response);
//			ex.export();
//		} catch (Exception e) {
//			log.error("Error export", e);
//			return false;
//		}
//
//		return true;
//	}
//
//	private void validateSoBb(BienBanLayMau update, BienBanLayMauReq req) throws Exception {
//		String soBB = req.getSoBienBan();
//		if (!StringUtils.hasText(soBB))
//			return;
//		if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(soBB))) {
//			Optional<BienBanLayMau> optional = bienBanLayMauRepository.findFirstBySoBienBan(soBB);
//			Long updateId = Optional.ofNullable(update).map(BienBanLayMau::getId).orElse(null);
//			if (optional.isPresent() && !optional.get().getId().equals(updateId))
//				throw new Exception("Số biên bản " + soBB + " đã tồn tại");
//		}
//	}
//
//	@Override
//	public Integer getSo() throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		Integer so = bienBanLayMauRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//		so = Optional.ofNullable(so).orElse(0);
//		so = so + 1;
//		return so;
//	}
//
//	@Override
//	public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//		BienBanLayMauSearchReq countReq = new BienBanLayMauSearchReq();
//		countReq.setMaDvis(maDvis);
//		BaseNhapHangCount count = new BaseNhapHangCount();
//
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_THOC);
//		count.setThoc(bienBanLayMauRepository.countBienBan(countReq));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_GAO);
//		count.setGao(bienBanLayMauRepository.countBienBan(countReq));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_MUOI);
//		count.setMuoi(bienBanLayMauRepository.countBienBan(countReq));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_VATTU);
//		count.setVatTu(bienBanLayMauRepository.countBienBan(countReq));
//		return count;
//	}
}
