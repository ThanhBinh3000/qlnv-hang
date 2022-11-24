package com.tcdt.qlnvhang.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.HhQdGiaoNvuNhapxuatDtlLoaiNx;
import com.tcdt.qlnvhang.enums.HhQdGiaoNvuNhapxuatHdrLoaiQd;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.repository.HhDviThuchienQdinhRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoRepository;
import com.tcdt.qlnvhang.repository.phieuknghiemcluonghang.PhieuKnghiemCluongHangRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNxDdiemRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatDtlRepository;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatDtlReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemtracl.NhPhieuKtChatLuongService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoService;
import com.tcdt.qlnvhang.table.*;
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
	private PhieuKnghiemCluongHangRepository phieuKnghiemCluongHangRepository;

	@Override
	@Transactional
	public HhQdGiaoNvuNhapxuatHdr create(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		this.validateSoQd(null, objReq);

		HhQdGiaoNvuNhapxuatHdr dataMap = new HhQdGiaoNvuNhapxuatHdr();
		BeanUtils.copyProperties(objReq, dataMap, "id");

		dataMap.setNguoiTao(userInfo.getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		dataMap.setMaDvi(userInfo.getDvql());
		dataMap.setCapDvi(userInfo.getCapDvi());

		// File dinh kem cua goi thau
		List<FileDKemJoinQdNhapxuat> dtls2 = new ArrayList<FileDKemJoinQdNhapxuat>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdNhapxuat.class);
			dtls2.forEach(f -> {
				f.setDataType(HhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataMap.setChildren2(dtls2);

		hhQdGiaoNvuNhapxuatRepository.save(dataMap);

		if(dataMap.getLoaiVthh().startsWith("02")){
//			List<HhHopDongDdiemNhapKho> allByIdHdongHdr = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(dataMap.getId());
//			List<HhHopDongDdiemNhapKho> collect = allByIdHdongHdr.stream().filter(item -> item.getMaDvi().equalsIgnoreCase(userInfo.getDvql())).collect(Collectors.toList());
//			collect.forEach(item -> {
//				item.setTrangThai(Contains.DADUTHAO_QD);
//			});
//			hhHopDongDdiemNhapKhoRepository.saveAll(collect);
		}else{
			hhHopDongRepository.updateHopDong(dataMap.getIdHd(),Contains.DADUTHAO_QD);
		}


		this.saveDetail(dataMap,objReq,false);

		return dataMap;
	}

	@Override
	@Transactional
	public HhQdGiaoNvuNhapxuatHdr update(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		this.validateSoQd(qOptional.get(), objReq);

		HhQdGiaoNvuNhapxuatHdr dataDB = qOptional.get();
		BeanUtils.copyProperties(objReq, dataDB,"id");

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(userInfo.getUsername());
		dataDB.setId(dataDB.getId());

		// File dinh kem cua goi thau
		List<FileDKemJoinQdNhapxuat> dtls2 = new ArrayList<FileDKemJoinQdNhapxuat>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdNhapxuat.class);
			dtls2.forEach(f -> {
				f.setDataType(HhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataDB.setChildren2(dtls2);

		hhQdGiaoNvuNhapxuatRepository.save(dataDB);

		this.saveDetail(dataDB,objReq,true);

		return dataDB;
	}

	private void validateSoQd(HhQdGiaoNvuNhapxuatHdr update, HhQdGiaoNvuNhapxuatHdrReq req) throws Exception {
		String soQd = req.getSoQd();
		if(!StringUtils.isEmpty(soQd)){
			if(update == null){
				Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
				if(optional.isPresent()){
					throw new Exception("Số quyết định " + soQd + " đã tồn tại");
				}
			}else{
				if(!req.getSoQd().equals(update.getSoQd())){
					Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
					if(optional.isPresent()){
						throw new Exception("Số quyết định " + soQd + " đã tồn tại");
					}
				}
			}
		}

		if (!StringUtils.hasText(soQd))
			return;
		if (update == null || (StringUtils.hasText(update.getSoQd()) && !update.getSoQd().equalsIgnoreCase(soQd))) {
			Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
			Long updateId = Optional.ofNullable(update).map(HhQdGiaoNvuNhapxuatHdr::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số quyết định " + soQd + " đã tồn tại");
		}
	}

	@Transactional()
	void saveDetail(HhQdGiaoNvuNhapxuatHdr main,HhQdGiaoNvuNhapxuatHdrReq objReq ,boolean isUpdate){
		if(isUpdate){
			dtlRepository.deleteAllByIdHdr(main.getId());
		}
		List<HhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetailList(), HhQdGiaoNvuNhapxuatDtl.class);
		for (HhQdGiaoNvuNhapxuatDtl dtl : dtls) {
			ddiemNhapRepository.deleteAllByIdCt(dtl.getId());
			dtl.setId(null);
			dtl.setIdHdr(main.getId());
			dtl.setTrangThai(TrangThaiAllEnum.CHUA_CAP_NHAT.getId());
			dtlRepository.save(dtl);
			if(!ObjectUtils.isEmpty(dtl.getChildren())){
				List<HhQdGiaoNvuNxDdiem> ddNhap = ObjectMapperUtils.mapAll(dtl.getChildren(), HhQdGiaoNvuNxDdiem.class);
				ddNhap.forEach( item -> {
					item.setIdCt(dtl.getId());
				});
				ddiemNhapRepository.saveAll(ddNhap);
			}
		}
	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		Map<String, String> tenCloaiVthh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

		HhQdGiaoNvuNhapxuatHdr data = qOptional.get();
		data.setTenLoaiQd(HhQdGiaoNvuNhapxuatHdrLoaiQd.getTenById(data.getLoaiQd()));
		data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
		data.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(data.getTrangThai()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		data.setTenLoaiVthh(tenCloaiVthh.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(tenCloaiVthh.get(data.getCloaiVthh()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));


		List<HhQdGiaoNvuNhapxuatDtl> hhQdGiaoNvuNhapxuatDtl= dtlRepository.findAllByIdHdr(data.getId());
		for (HhQdGiaoNvuNhapxuatDtl dtl:hhQdGiaoNvuNhapxuatDtl){
			dtl.setTenLoaiVthh(tenCloaiVthh.get(dtl.getLoaiVthh()));
			dtl.setTenCloaiVthh(tenCloaiVthh.get(dtl.getCloaiVthh()));
			dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
			dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));

			List<HhQdGiaoNvuNxDdiem> allByIdCt = ddiemNhapRepository.findAllByIdCt(dtl.getId());
			allByIdCt.forEach(item->{
				item.setTenCuc(mapDmucDvi.get(item.getMaCuc()));
				item.setTenChiCuc(mapDmucDvi.get(item.getMaChiCuc()));
				item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
				item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
				item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
				item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
				this.setDataPhieu(null,item);
			});
			dtl.setChildren(allByIdCt);
		}
		data.setDtlList(hhQdGiaoNvuNhapxuatDtl);
		data.setHopDong(hhHopDongRepository.findById(data.getIdHd()).get());

		return data;
	}

	@Override
	public Page<HhQdGiaoNvuNhapxuatHdr> colection(HhQdNhapxuatSearchReq objReq, HttpServletRequest req)
			throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

		Page<HhQdGiaoNvuNhapxuatHdr> dataPage = hhQdGiaoNvuNhapxuatRepository
				.findAll(HhQdGiaoNvuNhapxuatSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapTenDvi();
		for (HhQdGiaoNvuNhapxuatHdr hdr : dataPage.getContent()) {
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

		Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		HhQdGiaoNvuNhapxuatHdr item = optional.get();

		// Is vật tư
//		if (item.getLoaiVthh().startsWith("02")) {
		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
			case Contains.CHODUYET_TP + Contains.DUTHAO:
			case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
			case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
				optional.get().setNguoiGuiDuyet(getUser().getUsername());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
				optional.get().setNguoiGuiDuyet(getUser().getUsername());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
			case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.BAN_HANH + Contains.CHODUYET_LDC:
				optional.get().setNguoiPduyet(getUser().getUsername());
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

		Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

		List<HhQdGiaoNvuNhapxuatDtl> listDtl  = dtlRepository.findAllByIdHdr(idSearchReq.getId());
		if(!CollectionUtils.isEmpty(listDtl)){
			dtlRepository.deleteAll(listDtl);
		}
		hhHopDongRepository.updateHopDong(optional.get().getIdHd(),NhapXuatHangTrangThaiEnum.DAKY.getId());
		hhQdGiaoNvuNhapxuatRepository.delete(optional.get());

	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
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
		Page<HhQdGiaoNvuNhapxuatHdr> page = this.searchPage(searchReq);
		List<HhQdGiaoNvuNhapxuatHdr> data = page.getContent();

		String title = "Danh sách quyết định giao nhiệm vụ nhập xuất";
		String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày quyết định", "Năm Nhập", "Loại hàng hóa", "Chủng loại hàng hóa",
				"Trích Yếu Quyết Định", "Trạng thái"};
		String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_nhap_xuat.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdGiaoNvuNhapxuatHdr qd = data.get(i);
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
	public Page<HhQdGiaoNvuNhapxuatHdr> timKiem(HhQdNhapxuatSearchReq req) throws Exception {
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
	public Page<HhQdGiaoNvuNhapxuatHdr> searchPage(HhQdNhapxuatSearchReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
				req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<HhQdGiaoNvuNhapxuatHdr> data = null;
		if (userInfo.getCapDvi().equalsIgnoreCase(Contains.CAP_CHI_CUC)) {
			data = hhQdGiaoNvuNhapxuatRepository.selectPageChiCuc(
					req.getNamNhap(),
					req.getSoQd(),
					req.getLoaiVthh(),
					req.getTrichYeu(),
					Contains.convertDateToString(req.getTuNgayQd()),
					Contains.convertDateToString(req.getDenNgayQd()),
					userInfo.getDvql(),
					pageable);
		} else {
			// Cục or Tổng cục
			data = hhQdGiaoNvuNhapxuatRepository.selectPageCuc(
					req.getNamNhap(),
					req.getSoQd(),
					req.getLoaiVthh(),
					req.getTrichYeu(),
					Contains.convertDateToString(req.getTuNgayQd()),
					Contains.convertDateToString(req.getDenNgayQd()),
					req.getMaDvi(),
					pageable);
		}
		Map<String, String> mapDmucHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String, String> tenCloaiVthh = getListDanhMucHangHoa();
		data.getContent().forEach(f -> {
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setTenLoaiVthh(mapDmucHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(mapDmucHh.get(f.getCloaiVthh()));

			List<HhQdGiaoNvuNhapxuatDtl> hhQdGiaoNvuNhapxuatDtl= dtlRepository.findAllByIdHdr(f.getId());
			for (HhQdGiaoNvuNhapxuatDtl dtl:hhQdGiaoNvuNhapxuatDtl){

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
				List<HhBbNghiemthuKlstHdr> bbNghiemThuBq = hhBbNghiemthuKlstRepository.findByIdQdGiaoNvNhAndMaDvi(f.getId(), dtl.getMaDvi());
				bbNghiemThuBq.forEach( item ->  {
					item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
				});
				dtl.setListBienBanNghiemThuBq(bbNghiemThuBq);

				// Set biên bản lấy mẫu/ bàn giao mẫu
				List<BienBanLayMau> bbLayMau = bienBanLayMauRepository.findByIdQdGiaoNvNhAndMaDvi(f.getId(), dtl.getMaDvi());
				bbLayMau.forEach( item -> {
					item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
					item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
					item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
					item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
					item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
					NhBbNhapDayKho nhBbNhapDayKhoStream = bbNhapDayKho.stream().filter(x -> Objects.equals(x.getId(), item.getIdBbNhapDayKho())).findAny().orElse(null);
					item.setNgayNhapDayKho(nhBbNhapDayKhoStream.getNgayKetThucNhap());
				});
				dtl.setListBienBanLayMau(bbLayMau);

				dtl.setTenLoaiVthh(tenCloaiVthh.get(dtl.getLoaiVthh()));
				dtl.setTenCloaiVthh(tenCloaiVthh.get(dtl.getCloaiVthh()));
				dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
				dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));

				List<HhQdGiaoNvuNxDdiem> allByIdCt = ddiemNhapRepository.findAllByIdCt(dtl.getId());
				allByIdCt.forEach(item->{
					item.setTenCuc(mapDmucDvi.get(item.getMaCuc()));
					item.setTenChiCuc(mapDmucDvi.get(item.getMaChiCuc()));
					item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
					item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
					item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
					item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
					this.setDataPhieu(null,item);
				});
				dtl.setChildren(allByIdCt);
//				this.setDataPhieu(dtl,null);
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
			f.setListPhieuKiemNghiemCl(phieuKnghiemCl);
			f.setDtlList(hhQdGiaoNvuNhapxuatDtl);
		});
		return data;
	}


	void setDataPhieu(HhQdGiaoNvuNhapxuatDtl dtl , HhQdGiaoNvuNxDdiem ddNhap){
		if(dtl != null){
//			dtl.setListPhieuKtraCl(nhPhieuKtChatLuongService.findAllByIdQdGiaoNvNh(dtl.getIdHdr()));
//			dtl.setListPhieuNhapKho(nhPhieuNhapKhoService.findAllByIdQdGiaoNvNh(dtl.getIdHdr()));
//			dtl.setListBangKeCanHang(nhBangKeCanHangService.findAllByIdQdGiaoNvNh(dtl.getIdHdr()));
		}else{
			ddNhap.setListPhieuKtraCl(nhPhieuKtChatLuongService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setListPhieuNhapKho(nhPhieuNhapKhoService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setListBangKeCanHang(nhBangKeCanHangService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setBienBanNhapDayKho(nhBbNhapDayKhoRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
			ddNhap.setBienBanLayMau(bienBanLayMauRepository.findByIdDdiemGiaoNvNh(ddNhap.getId()));
		}
	}

	@Override
	@Transactional
	public void updateDdiemNhap(HhQdGiaoNvuNhapxuatHdrReq req) throws Exception {
		List<HhQdGiaoNvuNhapxuatDtlReq> detailList = req.getDetailList();
		for (HhQdGiaoNvuNhapxuatDtlReq dtl : detailList) {
			List<HhQdGiaoNvuNxDdiem> allByIdCt = ddiemNhapRepository.findAllByIdCt(dtl.getId());
			if(!allByIdCt.isEmpty()){
				ddiemNhapRepository.deleteAll(allByIdCt);
			}
			List<HhQdGiaoNvuNxDdiem> dtls = ObjectMapperUtils.mapAll(dtl.getChildren(), HhQdGiaoNvuNxDdiem.class);
			dtls.forEach( item -> {
				item.setIdCt(dtl.getId());
			});
			dtlRepository.updateTrangThai(dtl.getId(),dtl.getTrangThai());
			ddiemNhapRepository.saveAll(dtls);
		}
	}
}