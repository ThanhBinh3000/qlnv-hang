package com.tcdt.qlnvhang.service.impl;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatPreview;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.HhQdGiaoNvuNhapxuatDtlLoaiNx;
import com.tcdt.qlnvhang.enums.HhQdGiaoNvuNhapxuatHdrLoaiQd;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.repository.HhDviThuchienQdinhRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoRepository;
import com.tcdt.qlnvhang.repository.phieuknghiemcluonghang.PhieuKnghiemCluongHangRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNxDdiemRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatDtlRepository;
import com.tcdt.qlnvhang.repository.vattu.bangke.NhBangKeVtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan.NhBbGiaoNhanVtRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiRepository;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatDtlReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemtracl.NhPhieuKtChatLuongService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapxuat;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.secification.HhQdGiaoNvuNhapxuatSpecification;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;

@Service
public class HhQdGiaoNvuNhapxuatServiceImpl extends BaseServiceImpl implements HhQdGiaoNvuNhapxuatService {
	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private NhBbNhapDayKhoRepository nhBbNhapDayKhoRepository;

	@Autowired
	private NhBbNhapDayKhoCtRepository nhBbNhapDayKhoCtRepository;

	@Autowired
	private HhQdGiaoNvuNhapxuatDtlRepository dtlRepository;

	@Autowired
	private HhQdGiaoNvuNxDdiemRepository ddiemNhapRepository;

	@Autowired
	private HhDviThuchienQdinhRepository hhDviThuchienQdinhRepository;

	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private NhPhieuKtChatLuongService nhPhieuKtChatLuongService;

	@Autowired
	private NhPhieuNhapKhoService nhPhieuNhapKhoService;

	@Autowired
	private NhBangKeCanHangService nhBangKeCanHangService;

	@Autowired
	private HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;
	
	@Autowired
	private BienBanLayMauRepository bienBanLayMauRepository;

	@Autowired
	private NhBienBanChuanBiKhoRepository nhBienBanChuanBiKhoRepository;

	@Autowired
	private PhieuKnghiemCluongHangRepository phieuKnghiemCluongHangRepository;

	@Autowired
	private NhPhieuNhapKhoTamGuiRepository nhPhieuNhapKhoTamGuiRepository;

	@Autowired
	private NhBienBanGuiHangRepository nhBienBanGuiHangRepository;

	@Autowired
	private NhBangKeVtRepository nhBangKeVtRepository;

	@Autowired
	private NhBbGiaoNhanVtRepository nhBbGiaoNhanVtRepository;

	@Autowired
	private NhHoSoKyThuatRepository nhHoSoKyThuatRepository;
	@Autowired
	private FileDinhKemService fileDinhKemService;
	@Autowired
	private UserInfoRepository userInfoRepository;
	@Override
	@Transactional
	public NhQdGiaoNvuNhapxuatHdr create(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		Optional<NhQdGiaoNvuNhapxuatHdr> byIdHdAndMaDviAndNamNhap = hhQdGiaoNvuNhapxuatRepository.findByIdHdAndMaDviAndNamNhap(objReq.getIdHd(), userInfo.getDvql(), objReq.getNamNhap());
		if(byIdHdAndMaDviAndNamNhap.isPresent()){
			throw new Exception("Đơn vị đã tạo hợp đồng, vui lòng tạo hợp đồng");
		}

		this.validateSoQd(null, objReq);

		NhQdGiaoNvuNhapxuatHdr dataMap = new NhQdGiaoNvuNhapxuatHdr();
		BeanUtils.copyProperties(objReq, dataMap, "id");

		dataMap.setNguoiTao(userInfo.getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		dataMap.setMaDvi(userInfo.getDvql());
		dataMap.setCapDvi(userInfo.getCapDvi());

		NhQdGiaoNvuNhapxuatHdr created = hhQdGiaoNvuNhapxuatRepository.save(dataMap);
		if (!DataUtils.isNullOrEmpty(objReq.getFileCanCu())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileCanCu(), created.getId(), NhQdGiaoNvuNhapxuatHdr.TABLE_NAME + "_CAN_CU");
		}
		if (!DataUtils.isNullObject(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), NhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
		}

		if(dataMap.getLoaiVthh().startsWith("02")){
//			List<HhHopDongDdiemNhapKho> allByIdHdongHdr = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(dataMap.getId());
//			List<HhHopDongDdiemNhapKho> collect = allByIdHdongHdr.stream().filter(item -> item.getMaDvi().equalsIgnoreCase(userInfo.getDvql())).collect(Collectors.toList());
//			collect.forEach(item -> {
//				item.setTrangThai(Contains.DADUTHAO_QD);
//			});
//			hhHopDongDdiemNhapKhoRepository.saveAll(collect);
			this.saveDetail(dataMap,objReq,false);
		}else{
			hhHopDongRepository.updateHopDong(dataMap.getIdHd(),Contains.DADUTHAO_QD);
			this.saveDetailLt(dataMap,objReq,false);
		}
		return dataMap;
	}

	@Override
	@Transactional
	public NhQdGiaoNvuNhapxuatHdr update(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<NhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		this.validateSoQd(qOptional.get(), objReq);

		NhQdGiaoNvuNhapxuatHdr dataDB = qOptional.get();
		BeanUtils.copyProperties(objReq, dataDB,"id");

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(userInfo.getUsername());
		dataDB.setId(dataDB.getId());

		NhQdGiaoNvuNhapxuatHdr created = hhQdGiaoNvuNhapxuatRepository.save(dataDB);
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(NhQdGiaoNvuNhapxuatHdr.TABLE_NAME + "_CAN_CU"));
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(NhQdGiaoNvuNhapxuatHdr.TABLE_NAME));
		if (!DataUtils.isNullOrEmpty(objReq.getFileCanCu())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileCanCu(), created.getId(), NhQdGiaoNvuNhapxuatHdr.TABLE_NAME + "_CAN_CU");
		}
		if (!DataUtils.isNullObject(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), NhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
		}
		if (created.getLoaiVthh().startsWith("02")) {
			this.saveDetail(dataDB,objReq,true);
		} else {
			this.saveDetailLt(dataDB,objReq,true);
		}
		return dataDB;
	}

	private void validateSoQd(NhQdGiaoNvuNhapxuatHdr update, HhQdGiaoNvuNhapxuatHdrReq req) throws Exception {
		String soQd = req.getSoQd();
		if(!StringUtils.isEmpty(soQd)){
			if(update == null){
				Optional<NhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
				if(optional.isPresent()){
					throw new Exception("Số quyết định " + soQd + " đã tồn tại");
				}
			}else{
				if(!req.getSoQd().equals(update.getSoQd())){
					Optional<NhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
					if(optional.isPresent()){
						throw new Exception("Số quyết định " + soQd + " đã tồn tại");
					}
				}
			}
		}

		if (!StringUtils.hasText(soQd))
			return;
		if (update == null || (StringUtils.hasText(update.getSoQd()) && !update.getSoQd().equalsIgnoreCase(soQd))) {
			Optional<NhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
			Long updateId = Optional.ofNullable(update).map(NhQdGiaoNvuNhapxuatHdr::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số quyết định " + soQd + " đã tồn tại");
		}
	}

	@Transactional()
	void saveDetail(NhQdGiaoNvuNhapxuatHdr main, HhQdGiaoNvuNhapxuatHdrReq objReq , boolean isUpdate){
		if(isUpdate){
			dtlRepository.deleteAllByIdHdr(main.getId());
		}
		List<NhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetailList(), NhQdGiaoNvuNhapxuatDtl.class);
		for (NhQdGiaoNvuNhapxuatDtl dtl : dtls) {
			ddiemNhapRepository.deleteAllByIdCt(dtl.getId());
			dtl.setId(null);
			dtl.setIdHdr(main.getId());
			dtl.setTrangThai(TrangThaiAllEnum.CHUA_CAP_NHAT.getId());
			dtlRepository.save(dtl);
			if(!ObjectUtils.isEmpty(dtl.getChildren())){
				List<NhQdGiaoNvuNxDdiem> ddNhap = ObjectMapperUtils.mapAll(dtl.getChildren(), NhQdGiaoNvuNxDdiem.class);
				ddNhap.forEach( item -> {
					item.setIdCt(dtl.getId());
				});
				ddiemNhapRepository.saveAll(ddNhap);
			}
		}
	}

	@Transactional()
	void saveDetailLt(NhQdGiaoNvuNhapxuatHdr main, HhQdGiaoNvuNhapxuatHdrReq objReq , boolean isUpdate){
		if(isUpdate){
			dtlRepository.deleteAllByIdHdr(main.getId());
		}
		List<NhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetailList(), NhQdGiaoNvuNhapxuatDtl.class);
		for (NhQdGiaoNvuNhapxuatDtl dtl : dtls) {
			ddiemNhapRepository.deleteAllByIdCt(dtl.getId());
			dtl.setId(null);
			dtl.setIdHdr(main.getId());
			dtl.setTrangThai(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
			dtlRepository.save(dtl);
			if(!ObjectUtils.isEmpty(dtl.getChildren())){
				List<NhQdGiaoNvuNxDdiem> ddNhap = ObjectMapperUtils.mapAll(dtl.getChildren(), NhQdGiaoNvuNxDdiem.class);
				ddNhap.forEach( item -> {
					item.setIdCt(dtl.getId());
				});
				ddiemNhapRepository.saveAll(ddNhap);
			}
		}
	}

	@Override
	public NhQdGiaoNvuNhapxuatHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<NhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		Map<String, String> tenCloaiVthh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

		NhQdGiaoNvuNhapxuatHdr data = qOptional.get();
		data.setTenLoaiQd(HhQdGiaoNvuNhapxuatHdrLoaiQd.getTenById(data.getLoaiQd()));
		data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
		data.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(data.getTrangThai()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		data.setTenLoaiVthh(tenCloaiVthh.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(tenCloaiVthh.get(data.getCloaiVthh()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Collections.singletonList(NhQdGiaoNvuNhapxuatHdr.TABLE_NAME));
		data.setFileDinhKems(fileDinhKem);
		List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singletonList(NhQdGiaoNvuNhapxuatHdr.TABLE_NAME + "_CAN_CU"));
		data.setFileCanCu(canCu);


		List<NhQdGiaoNvuNhapxuatDtl> nhQdGiaoNvuNhapxuatDtl = dtlRepository.findAllByIdHdr(data.getId());
		for (NhQdGiaoNvuNhapxuatDtl dtl: nhQdGiaoNvuNhapxuatDtl){
			dtl.setTenLoaiVthh(tenCloaiVthh.get(dtl.getLoaiVthh()));
			dtl.setTenCloaiVthh(tenCloaiVthh.get(dtl.getCloaiVthh()));
			dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
			dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));

			List<NhQdGiaoNvuNxDdiem> allByIdCt = ddiemNhapRepository.findAllByIdCt(dtl.getId());
			allByIdCt.forEach(item->{
				item.setTenCuc(mapDmucDvi.get(item.getMaCuc()));
				item.setTenChiCuc(mapDmucDvi.get(item.getMaChiCuc()));
				item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
				item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
				item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
				item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
				this.setDataPhieu(null,item, null);
			});
			dtl.setChildren(allByIdCt);
		}
		data.setDtlList(nhQdGiaoNvuNhapxuatDtl);
		Optional<HhHopDongHdr> hhHopDongHdr = hhHopDongRepository.findById(data.getIdHd());
		if(hhHopDongHdr.isPresent()){
			data.setHopDong(hhHopDongHdr.get());
		}

		return data;
	}

	@Override
	public Page<NhQdGiaoNvuNhapxuatHdr> colection(HhQdNhapxuatSearchReq objReq, HttpServletRequest req)
			throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

		Page<NhQdGiaoNvuNhapxuatHdr> dataPage = hhQdGiaoNvuNhapxuatRepository
				.findAll(HhQdGiaoNvuNhapxuatSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapTenDvi();
		for (NhQdGiaoNvuNhapxuatHdr hdr : dataPage.getContent()) {
			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
			hdr.setTenLoaiQd(HhQdGiaoNvuNhapxuatHdrLoaiQd.getTenById(hdr.getLoaiQd()));
		}
		return dataPage;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		Optional<NhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		NhQdGiaoNvuNhapxuatHdr item = optional.get();

		// Is vật tư
//		if (item.getLoaiVthh().startsWith("02")) {
		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
			case Contains.CHODUYET_TP + Contains.DUTHAO:
			case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
			case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
			case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
				optional.get().setNguoiGuiDuyet(getUser().getFullName());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
			case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
				optional.get().setNguoiPduyet(getUser().getFullName());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.BAN_HANH + Contains.CHODUYET_LDC:
				optional.get().setNguoiPduyet(getUser().getFullName());
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
//			if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
//				List<HhHopDongDdiemNhapKho> allByIdHdongHdr = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(optional.get().getIdHd());
//				List<HhHopDongDdiemNhapKho> collect = allByIdHdongHdr.stream().filter( x -> x.getMaDvi().equalsIgnoreCase(userInfo.getDvql())).collect(Collectors.toList());
//				collect.forEach(x -> {
//					x.setTrangThai(Contains.DABANHANH_QD);
//				});
//				hhHopDongDdiemNhapKhoRepository.saveAll(collect);
//			}
//		} else {
//			String status = stReq.getTrangThai() + optional.get().getTrangThai();
//			switch (status) {
//				case Contains.BAN_HANH + Contains.DUTHAO:
//					optional.get().setNguoiPduyet(getUser().getUsername());
//					optional.get().setNgayPduyet(getDateTimeNow());
//					break;
//				default:
//					throw new Exception("Phê duyệt không thành công");
//			}

//		}
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
			hhHopDongRepository.updateHopDong(optional.get().getIdHd(),Contains.DABANHANH_QD);
		}
		optional.get().setTrangThai(stReq.getTrangThai());
		hhQdGiaoNvuNhapxuatRepository.save(item);
		return true;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<NhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

		List<NhQdGiaoNvuNhapxuatDtl> listDtl  = dtlRepository.findAllByIdHdr(idSearchReq.getId());
		if(!CollectionUtils.isEmpty(listDtl)){
			dtlRepository.deleteAll(listDtl);
		}
		hhHopDongRepository.updateHopDong(optional.get().getIdHd(),NhapXuatHangTrangThaiEnum.DAKY.getId());
		hhQdGiaoNvuNhapxuatRepository.delete(optional.get());

	}

	@Transactional
	@Override
	public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");
		if (idSearchReq.getIdList() != null && idSearchReq.getIdList().size() > 0){
			List<NhQdGiaoNvuNhapxuatHdr> listData = hhQdGiaoNvuNhapxuatRepository.findByIdIn(idSearchReq.getIdList());
			for (NhQdGiaoNvuNhapxuatHdr qd: listData) {
				if (!qd.getTrangThai().equals(Contains.DUTHAO)
						&& !qd.getTrangThai().equals(Contains.TU_CHOI)) {
					throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");
				}
				List<NhQdGiaoNvuNhapxuatDtl> listDtl  = dtlRepository.findAllByIdHdr(qd.getId());
				if(!CollectionUtils.isEmpty(listDtl)){
					dtlRepository.deleteAll(listDtl);
				}
				hhHopDongRepository.updateHopDong(qd.getIdHd(),NhapXuatHangTrangThaiEnum.DAKY.getId());
				hhQdGiaoNvuNhapxuatRepository.delete(qd);
			}
		} else {
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		}
	}

	@Override
	public NhQdGiaoNvuNhapxuatHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HhDviThuchienQdinh dviThQdDetail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDviThuchienQdinh> qOptional = hhDviThuchienQdinhRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		HhDviThuchienQdinh data = qOptional.get();
		Map<String, String> mapDmucDvi = getMapTenDvi();
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		return data;
	}

	@Override
	public List<Map<String, String>> listLoaiNx() {
		List<Map<String, String>> response = new ArrayList<>();
		for (HhQdGiaoNvuNhapxuatDtlLoaiNx loaiNx : HhQdGiaoNvuNhapxuatDtlLoaiNx.values()) {
			Map<String, String> map = new HashMap<>();
			map.put("ma", loaiNx.getId());
			map.put("ten", loaiNx.getTen());
			response.add(map);
		}
		return response;
	}

	@Override
	public void exportDsQdGNvNx(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		searchReq.setMaDvi(userInfo.getDvql());
		Page<NhQdGiaoNvuNhapxuatHdr> page = this.searchPage(searchReq);
		List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();

		String title = "Danh sách quyết định giao nhiệm vụ nhập hàng";
		String[] rowsName = new String[]{"STT","Năm Nhập", "Số quyết định", "Ngày quyết định", "Số hợp đồng", "Loại hàng hóa", "Chủng loại hàng hóa",
				"Thời gian nhập kho muộn nhất", "Trích Yếu Quyết Định", "Trạng thái QĐ", "Trạng thái NH"};
		String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_nhap_hang.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getNamNhap();
			objs[2] = qd.getSoQd();
			objs[3] = convertDate(qd.getNgayQdinh());
			objs[4] = qd.getSoHd();
			objs[5] = qd.getTenLoaiVthh();
			objs[6] = qd.getTenCloaiVthh();
			objs[7] = convertDate(qd.getTgianNkho());
			objs[8] = qd.getTrichYeu();
			objs[9] = NhapXuatHangTrangThaiEnum.getTenById(qd.getTrangThai());
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public void exportBbNtBq(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		searchReq.setMaDvi(userInfo.getDvql());
		Page<NhQdGiaoNvuNhapxuatHdr> page = this.searchPage(searchReq);
		List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();

		String title = "Danh sách quyết định giao nhiệm vụ nhập xuất";
		String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Điểm kho", "Lô kho",
				"Số BB NT kê lót, BQLĐ", "Ngày lập biên bản", "Ngày kết thúc NT kê lót, BQLĐ", "Tổng kinh phí thực tế (đ)", "Tổng kinh phí TC PD (đ)", "Trạng thái"};
		String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_nhap_xuat.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getSoQd();
			objs[2] = convertDateToString(qd.getNgayQdinh());
			objs[3] = qd.getNamNhap();
			objs[4] = qd.getTenLoaiVthh();
			objs[5] = qd.getTrichYeu();
			objs[6] = NhapXuatHangTrangThaiEnum.getTenById(qd.getTrangThai());
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<NhQdGiaoNvuNhapxuatHdr> timKiem(HhQdNhapxuatSearchReq req) throws Exception {
//		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
//		UserInfo userInfo = UserUtils.getUserInfo();
//		String dvql = userInfo.getDvql();
//		req.setMaDvi(dvql);
//		String capDvi = userInfo.getCapDvi();
//		if (!Contains.CAP_CHI_CUC.equalsIgnoreCase(userInfo.getCapDvi()) && !Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
//			return new PageImpl<>(new ArrayList<>(), pageable, 0);
//		}
//
//		List<HhQdGiaoNvuNhapxuatHdr> qds = new ArrayList<>();
//		List<Object[]> data = hhQdGiaoNvuNhapxuatRepository.search(req, capDvi);
//		Set<Long> qdIds = data.stream().map(o -> (Long) o[0]).collect(Collectors.toSet());
//		Map<Long, List<HhQdGiaoNvuNhapxuatDtl1>> dtl1Map = hhQdGiaoNvuNhapxuatDtl1Repository.findByParentIdIn(qdIds)
//				.stream().collect(Collectors.groupingBy(HhQdGiaoNvuNhapxuatDtl1::getParentId));
//		List<HhQdGiaoNvuNhapxuatDtl1> dtl1s = hhQdGiaoNvuNhapxuatDtl1Repository.findByParentIdIn(qdIds);
//
//		Map<String, String> mapDmucHh = getListDanhMucHangHoa();
//
//		for (Object[] o : data) {
//			HhQdGiaoNvuNhapxuatHdr qd = new HhQdGiaoNvuNhapxuatHdr();
//			Long qdId = (Long) o[0];
//			String soQdNhap = o[1] != null ? (String) o[1] : null;
//			Date ngayQdinh = o[2] != null ? (Date) o[2] : null;
//			Integer namNhap = o[3] != null ? (Integer) o[3] : null;
//			String trichYeu = o[4] != null ? (String) o[4] : null;
//			String trangThai = o[5] != null ? (String) o[5] : null;
//			qd.setId(qdId);
//			qd.setSoQd(soQdNhap);
//			qd.setNgayQdinh(ngayQdinh);
//			qd.setNamNhap(namNhap);
//			qd.setTrichYeu(trichYeu);
//			qd.setTrangThai(trangThai);
//			qd.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(trangThai));
//			qd.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(trangThai));
//			if (!CollectionUtils.isEmpty(dtl1Map.get(qd.getId()))) {
//				qd.setChildren1(dtl1Map.get(qd.getId()));
//			}
//
//			for (HhQdGiaoNvuNhapxuatDtl1 dtl : dtl1s) {
//				Optional<HhHopDongHdr> hd = hhHopDongRepository.findById(dtl.getHopDong().getId());
//				qd.setTenVthh(mapDmucHh.get(hd.get().getLoaiVthh()));
//				qd.setTenCloaiVthh(mapDmucHh.get(hd.get().getCloaiVthh()));
//			}
//			qds.add(qd);
//		}
//		return new PageImpl<>(qds, pageable, hhQdGiaoNvuNhapxuatRepository.count(req, capDvi));
		return null;
	}

	@Override
	public BaseNhapHangCount count() throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		String dvql = userInfo.getDvql();
//		BaseNhapHangCount count = new BaseNhapHangCount();
//
//		if (Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
//			count.setTatCa(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, null));
//			count.setThoc(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_THOC));
//			count.setGao(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_GAO));
//			count.setVatTu(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_VATTU));
//			count.setMuoi(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_MUOI));
//		} else if (Contains.CAP_CHI_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
//			count.setTatCa(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, null));
//			count.setThoc(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_THOC));
//			count.setGao(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_GAO));
//			count.setVatTu(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_VATTU));
//			count.setMuoi(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_MUOI));
//		}

//		return count;
		return null;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deleteMultiple(DeleteReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (CollectionUtils.isEmpty(req.getIds()))
			return false;

		hhQdGiaoNvuNhapxuatRepository.deleteByIdIn(req.getIds());
		return true;
	}

	@Override
	public Page<NhQdGiaoNvuNhapxuatHdr> searchPage(HhQdNhapxuatSearchReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
				req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<NhQdGiaoNvuNhapxuatHdr> data = null;
		if (userInfo.getCapDvi().equalsIgnoreCase(Contains.CAP_CHI_CUC)) {
			data = hhQdGiaoNvuNhapxuatRepository.selectPageChiCuc(
					req.getNamNhap(),
					req.getSoQd(),
					req.getLoaiVthh(),
					req.getTrichYeu(),
					Contains.convertDateToString(req.getTuNgayLP()),
					Contains.convertDateToString(req.getDenNgayLP()),
					Contains.convertDateToString(req.getTuNgayKT()),
					Contains.convertDateToString(req.getDenNgayKT()),
					userInfo.getDvql(),
					req.getSoBbNtBq(),
					req.getTrangThai(),
					Contains.convertDateToString(req.getTuNgayGD()),
					Contains.convertDateToString(req.getDenNgayGD()),
					req.getKqDanhGia(),
					pageable);
		} else {
			// Cục or Tổng cục
			data = hhQdGiaoNvuNhapxuatRepository.selectPageCuc(
					req.getNamNhap(),
					req.getSoQd(),
					req.getLoaiVthh(),
					req.getCloaiVthh(),
					req.getTrichYeu(),
					convertFullDateToString(req.getTuNgayQd()),
					convertFullDateToString(req.getDenNgayQd()),
					userInfo.getDvql(),
					req.getTrangThai(),
					pageable);
		}
		Map<String, String> mapDmucHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		data.getContent().forEach(f -> {
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setTenLoaiVthh(mapDmucHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(mapDmucHh.get(f.getCloaiVthh()));

			List<NhQdGiaoNvuNhapxuatDtl> nhQdGiaoNvuNhapxuatDtl = dtlRepository.findAllByIdHdr(f.getId());
			for (NhQdGiaoNvuNhapxuatDtl dtl: nhQdGiaoNvuNhapxuatDtl){

				// Set biên bản nhập đầy kho;
				List<NhBbNhapDayKho> bbNhapDayKho = nhBbNhapDayKhoRepository.findByIdQdGiaoNvNhAndMaDvi(f.getId(),dtl.getMaDvi());
				bbNhapDayKho.forEach( item -> {
					item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
					item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
					item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
					item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
					item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
					item.setChiTiets(nhBbNhapDayKhoCtRepository.findAllByIdBbNhapDayKho(item.getId()));
				});
				dtl.setListBienBanNhapDayKho(bbNhapDayKho);

				// Set biên bản nghiệm thu bảo quản
				List<HhBbNghiemthuKlstHdr> bbNghiemThuBq = hhBbNghiemthuKlstRepository.findByIdQdGiaoNvNhAndMaDviStartsWith(f.getId(), dtl.getMaDvi());
				bbNghiemThuBq.forEach( item ->  {
					item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
				});
				dtl.setListBienBanNghiemThuBq(bbNghiemThuBq);

				// Set biên bản lấy mẫu/ bàn giao mẫu
//				req.setIdQdGiaoNvNh(f.getId());
//				req.setMaDviDtl(dtl.getMaDvi());
//				List<BienBanLayMau> bbLayMau = bienBanLayMauRepository.timKiemList(req);
//				bbLayMau.forEach( item -> {
//					item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//					item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
//					item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
//					item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
//					item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
//					NhBbNhapDayKho nhBbNhapDayKhoStream = bbNhapDayKho.stream().filter(x -> Objects.equals(x.getId(), item.getIdBbNhapDayKho())).findAny().orElse(null);
//					item.setBbNhapDayKho(nhBbNhapDayKhoStream);
//				});
//				dtl.setListBienBanLayMau(bbLayMau);

				// Set biên bản chuẩn bị kho
//				if(req.getBienBan().contains("bienBanChuanBiKho")){
					List<NhBienBanChuanBiKho> bbChuanBiKho = nhBienBanChuanBiKhoRepository.findByIdQdGiaoNvNhAndMaDvi(f.getId(), dtl.getMaDvi());
					bbChuanBiKho.forEach( item -> {
						item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
					});
					dtl.setListBienBanChuanBiKho(bbChuanBiKho);
//				}

				dtl.setTenLoaiVthh(mapDmucHh.get(dtl.getLoaiVthh()));
				dtl.setTenCloaiVthh(mapDmucHh.get(dtl.getCloaiVthh()));
				dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
				dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));

				List<NhQdGiaoNvuNxDdiem> allByIdCt = ddiemNhapRepository.findAllByIdCt(dtl.getId());
				allByIdCt.forEach(item->{
					item.setTenCuc(mapDmucDvi.get(item.getMaCuc()));
					item.setTenChiCuc(mapDmucDvi.get(item.getMaChiCuc()));
					item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
					item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
					item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
					item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
					this.setDataPhieu(null,item, req);
				});
				dtl.setChildren(allByIdCt);
			}

			// Set phiếu kiểm nghiệm chất lượng
			List<PhieuKnghiemCluongHang> phieuKnghiemCl = phieuKnghiemCluongHangRepository.findBySoQdGiaoNvNhAndMaDvi(f.getSoQd(), f.getMaDvi());
			phieuKnghiemCl.forEach( item -> {
				item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
				item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
				item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
				item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
				item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
			});

			// Set biên bản giao nhân
			List<NhBbGiaoNhanVt> bbGiaoNhanVt = nhBbGiaoNhanVtRepository.findByIdQdGiaoNvNhAndMaDvi(f.getId(),f.getMaDvi());
			bbGiaoNhanVt.forEach( item -> {
				Optional<NhBbNhapDayKho> bbNhapDayKho = nhBbNhapDayKhoRepository.findById(Long.parseLong(item.getSoBbNhapDayKho().split("/")[0]));
				if(bbNhapDayKho.isPresent()){
					NhBbNhapDayKho nhBbNhapDayKho = bbNhapDayKho.get();
					nhBbNhapDayKho.setChiTiets(nhBbNhapDayKhoCtRepository.findAllByIdBbNhapDayKho(nhBbNhapDayKho.getId()));
					item.setBbNhapDayKho(nhBbNhapDayKho);
				}
			});
			f.setListBienBanGiaoNhan(bbGiaoNhanVt);
			f.setListPhieuKiemNghiemCl(phieuKnghiemCl);
			f.setDtlList(nhQdGiaoNvuNhapxuatDtl);
		});
		return data;
	}

	@Override
	public List<NhQdGiaoNvuNhapxuatHdr> layTatCaQdGiaoNvNh(HhQdNhapxuatSearchReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		List<NhQdGiaoNvuNhapxuatHdr> data = null;
		if (userInfo.getCapDvi().equalsIgnoreCase(Contains.CAP_CHI_CUC)) {

		} else {
			data = hhQdGiaoNvuNhapxuatRepository.findAllByLoaiVthhStartsWithAndTrangThaiAndMaDviStartsWith(req.getLoaiVthh(), req.getTrangThai(), userInfo.getDvql());
		}
		if (data != null) {
			Map<String, String> mapDmucHh = getListDanhMucHangHoa();
			for (NhQdGiaoNvuNhapxuatHdr qdGiaoNvuNhapxuatHdr : data) {
				qdGiaoNvuNhapxuatHdr.setTenLoaiVthh(mapDmucHh.get(qdGiaoNvuNhapxuatHdr.getLoaiVthh()));
				qdGiaoNvuNhapxuatHdr.setTenCloaiVthh(mapDmucHh.get(qdGiaoNvuNhapxuatHdr.getCloaiVthh()));
				Optional<HhHopDongHdr> hhHopDongHdr = hhHopDongRepository.findById(qdGiaoNvuNhapxuatHdr.getIdHd());
				hhHopDongHdr.ifPresent(qdGiaoNvuNhapxuatHdr::setHopDong);
				List<BienBanLayMau> bienBanLayMauList = bienBanLayMauRepository.findByIdQdGiaoNvNh(qdGiaoNvuNhapxuatHdr.getId());
				if (!bienBanLayMauList.isEmpty()) {
					Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
					for (BienBanLayMau bienBanLayMau : bienBanLayMauList) {
						bienBanLayMau.setTenDvi(mapDmucDvi.get(bienBanLayMau.getMaDvi()));
						bienBanLayMau.setTenDiemKho(mapDmucDvi.get(bienBanLayMau.getMaDiemKho()));
						bienBanLayMau.setTenNhaKho(mapDmucDvi.get(bienBanLayMau.getMaNhaKho()));
						bienBanLayMau.setTenNganLoKho(bienBanLayMau.getMaLoKho() != null ? mapDmucDvi.get(bienBanLayMau.getMaLoKho()) + " - " + mapDmucDvi.get(bienBanLayMau.getMaNganKho()): mapDmucDvi.get(bienBanLayMau.getMaNganKho()));
					}
				}
				qdGiaoNvuNhapxuatHdr.setListBienBanLayMau(bienBanLayMauList);
			}
		}
		return data;
	}


	void setDataPhieu(NhQdGiaoNvuNhapxuatDtl dtl , NhQdGiaoNvuNxDdiem ddNhap, HhQdNhapxuatSearchReq req){
		if(dtl != null){
//			dtl.setListPhieuKtraCl(nhPhieuKtChatLuongService.findAllByIdQdGiaoNvNh(dtl.getIdHdr()));
//			dtl.setListPhieuNhapKho(nhPhieuNhapKhoService.findAllByIdQdGiaoNvNh(dtl.getIdHdr()));
//			dtl.setListBangKeCanHang(nhBangKeCanHangService.findAllByIdQdGiaoNvNh(dtl.getIdHdr()));
		}else{
			if (req != null) {
				req.setIdDdiemGiaoNvNh(ddNhap.getId());
				// Lay Bb Lay mau
				req.setNgayLayMauTuStr(convertFullDateToString(req.getNgayLayMauTu()));
				req.setNgayLayMauDenStr(convertFullDateToString(req.getNgayLayMauDen()));
				ddNhap.setBienBanLayMau(bienBanLayMauRepository.timKiemByDiaDiemNh(req));
				// Lay Bb Chuan bi kho
				req.setTuNgayTaoStr(convertFullDateToString(req.getTuNgayTao()));
				req.setDenNgayTaoStr(convertFullDateToString(req.getDenNgayTao()));
				ddNhap.setBienBanChuanBiKho(nhBienBanChuanBiKhoRepository.timKiemByDiaDiemNh(req));
			} else {
				ddNhap.setBienBanLayMau(bienBanLayMauRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
				ddNhap.setBienBanChuanBiKho(nhBienBanChuanBiKhoRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
			}
			ddNhap.setListPhieuKtraCl(nhPhieuKtChatLuongService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setListPhieuNhapKho(nhPhieuNhapKhoService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setListBangKeCanHang(nhBangKeCanHangService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setBienBanNhapDayKho(nhBbNhapDayKhoRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setPhieuNhapKhoTamGui(nhPhieuNhapKhoTamGuiRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setBienBanGuiHang(nhBienBanGuiHangRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setListPhieuNhapKho(nhPhieuNhapKhoService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setListBangKeVt(nhBangKeVtRepository.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			List<HhBbNghiemthuKlstHdr> hhBbNghiemthuKlstHdrList = hhBbNghiemthuKlstRepository.findByIdDdiemGiaoNvNh(ddNhap.getId());
			hhBbNghiemthuKlstHdrList.forEach(item -> {
				item.setTenThuKho(ObjectUtils.isEmpty(item.getIdThuKho()) ? null : userInfoRepository.findById(item.getIdThuKho()).get().getFullName());
			});
			ddNhap.setListBbNtbqld(hhBbNghiemthuKlstHdrList);
			if(!ObjectUtils.isEmpty(ddNhap.getBienBanLayMau())){
				ddNhap.setHoSoKyThuat(nhHoSoKyThuatRepository.findBySoBbLayMau(ddNhap.getBienBanLayMau().getSoBienBan()));
			}
		}
	}

	@Override
	@Transactional
	public void updateDdiemNhap(HhQdGiaoNvuNhapxuatHdrReq req) throws Exception {
		List<HhQdGiaoNvuNhapxuatDtlReq> detailList = req.getDetailList();
		for (HhQdGiaoNvuNhapxuatDtlReq dtl : detailList) {
			List<NhQdGiaoNvuNxDdiem> allByIdCt = ddiemNhapRepository.findAllByIdCt(dtl.getId());
			if(!allByIdCt.isEmpty()){
				ddiemNhapRepository.deleteAll(allByIdCt);
			}
			List<NhQdGiaoNvuNxDdiem> dtls = ObjectMapperUtils.mapAll(dtl.getChildren(), NhQdGiaoNvuNxDdiem.class);
			dtls.forEach( item -> {
				item.setIdCt(dtl.getId());
			});
			dtlRepository.updateTrangThai(dtl.getId(),dtl.getTrangThai());
			ddiemNhapRepository.saveAll(dtls);
		}
	}

	@Override
	public ReportTemplateResponse preview(HhQdGiaoNvuNhapxuatHdrReq hhQdGiaoNvuNhapxuatHdrReq) throws Exception {
		NhQdGiaoNvuNhapxuatHdr qdGiaoNvuNhapxuatHdr = detail(hhQdGiaoNvuNhapxuatHdrReq.getId().toString());
		if (!hhQdGiaoNvuNhapxuatHdrReq.getLoaiVthh().startsWith("02")) {
			qdGiaoNvuNhapxuatHdr.setDonViTinh("tấn");
		}
		ReportTemplate model = findByTenFile(hhQdGiaoNvuNhapxuatHdrReq.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		NhQdGiaoNvuNhapxuatPreview object = new NhQdGiaoNvuNhapxuatPreview();
		object.setHdr(qdGiaoNvuNhapxuatHdr);
		object.setThoiHanNk(convertDate(qdGiaoNvuNhapxuatHdr.getTgianNkho()));
		String[] parts = Objects.requireNonNull(convertDate(qdGiaoNvuNhapxuatHdr.getNgayPduyet())).split("/");
		object.setNgayKy(parts[0]);
		object.setThangKy(parts[1]);
		object.setNamKy(parts[2]);
		return docxToPdfConverter.convertDocxToPdf(inputStream, object);
	}
}