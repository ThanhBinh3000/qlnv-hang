package com.tcdt.qlnvhang.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhDxKhLcntThopHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntDsChuaThReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.secification.HhDxKhLcntThopSpecification;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.service.HhDxKhLcntThopHdrService;

@Service
public class HhDxKhLcntThopHdrServiceImpl extends BaseServiceImpl implements HhDxKhLcntThopHdrService {
	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private HhDxKhLcntThopDtlRepository hhDxKhLcntThopDtlRepository;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@Autowired
	private HhDxuatKhLcntDsgtDtlRepository hhDxuatKhLcntDsgtDtlRepository;

	@Override
	public HhDxKhLcntThopHdr sumarryData(HhDxKhLcntTChiThopReq objReq, HttpServletRequest req) throws Exception {
		List<HhDxuatKhLcntHdr> dxuatList =
				hhDxuatKhLcntHdrRepository.listTongHop(objReq.getLoaiVthh(),objReq.getCloaiVthh(),objReq.getNamKhoach(),objReq.getHthucLcnt(),objReq.getPthucLcnt(),objReq.getLoaiHdong(),objReq.getNguonVon());


		if (dxuatList.isEmpty()){
			throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
		}

		// Set thong tin hdr tong hop
		HhDxKhLcntThopHdr thopHdr = new HhDxKhLcntThopHdr();

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();

		thopHdr.setNamKhoach(objReq.getNamKhoach());
		thopHdr.setLoaiVthh(objReq.getLoaiVthh());
		thopHdr.setTenLoaiVthh(listDanhMucHangHoa.get(objReq.getLoaiVthh()));
		thopHdr.setCloaiVthh(objReq.getCloaiVthh());
		thopHdr.setTenCloaiVthh(listDanhMucHangHoa.get(objReq.getCloaiVthh()));

		thopHdr.setHthucLcnt(objReq.getHthucLcnt());
		thopHdr.setPthucLcnt(objReq.getPthucLcnt());
		thopHdr.setLoaiHdong(objReq.getLoaiHdong());
		thopHdr.setNguonVon(objReq.getNguonVon());

		// Add thong tin list dtl
		List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
		String tChuanCluong = "";
		for (HhDxuatKhLcntHdr dxuat : dxuatList) {
			HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
			// Set ngay min va ngay max o detail
			// Set min max ngày bắt đầu tổ chức
			if (StringUtils.isEmpty(thopHdr.getTgianBdauTchucTu())
					|| thopHdr.getTgianBdauTchucTu().compareTo(dxuat.getTgianBdauTchuc()) > 0){
				thopHdr.setTgianBdauTchucTu(dxuat.getTgianBdauTchuc());
			}
			if (StringUtils.isEmpty(thopHdr.getTgianBdauTchucDen())
					|| thopHdr.getTgianBdauTchucDen().compareTo(dxuat.getTgianBdauTchuc()) < 0){
				thopHdr.setTgianBdauTchucDen(dxuat.getTgianBdauTchuc());
			}
			// Set ngày min max ngày mở thầu
			if (StringUtils.isEmpty(thopHdr.getTgianMthauTu())
					|| thopHdr.getTgianMthauTu().compareTo(dxuat.getTgianMthau()) > 0){
				thopHdr.setTgianMthauTu(dxuat.getTgianMthau());
			}
			if (StringUtils.isEmpty(thopHdr.getTgianMthauDen())
					|| thopHdr.getTgianMthauDen().compareTo(dxuat.getTgianMthau()) < 0){
				thopHdr.setTgianMthauDen(dxuat.getTgianMthau());
			}
			// Set ngày min max ngày đóng thầu
			if (StringUtils.isEmpty(thopHdr.getTgianDthauTu())
					|| thopHdr.getTgianDthauTu().compareTo(dxuat.getTgianDthau()) > 0){
				thopHdr.setTgianDthauTu(dxuat.getTgianDthau());
			}
			if (StringUtils.isEmpty(thopHdr.getTgianDthauDen())
					|| thopHdr.getTgianDthauDen().compareTo(dxuat.getTgianDthau()) < 0){
				thopHdr.setTgianDthauDen(dxuat.getTgianDthau());
			}
			// Set ngày mim max nhập hàng
			if (StringUtils.isEmpty(thopHdr.getTgianNhangTu())
					|| thopHdr.getTgianNhangTu().compareTo(dxuat.getTgianNhang()) > 0){
				thopHdr.setTgianNhangTu(dxuat.getTgianNhang());
			}
			if (StringUtils.isEmpty(thopHdr.getTgianNhangDen())
					|| thopHdr.getTgianNhangDen().compareTo(dxuat.getTgianNhang()) < 0){
				thopHdr.setTgianNhangDen(dxuat.getTgianNhang());
			}

			// Set thong tin chung lay tu de xuat
			thopDtl.setIdDxHdr(dxuat.getId());
			thopDtl.setMaDvi(dxuat.getMaDvi());
			thopDtl.setTenDvi(getDviByMa(dxuat.getMaDvi(), req).getTenDvi());
			thopDtl.setSoDxuat(dxuat.getSoDxuat());
			thopDtl.setNgayDxuat(dxuat.getNgayKy());
			thopDtl.setTenDuAn(dxuat.getTenDuAn());
			thopDtl.setTrichYeu(dxuat.getTrichYeu());
			thopDtl.setGtriDthau(dxuat.getGtriDthau());
			thopDtl.setGtriHdong(dxuat.getGtriHdong());
			// Add danh sach goi thau
			List<HhDxKhlcntDsgthau> dtlsGThau = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(dxuat.getId());
			BigDecimal soLuong = BigDecimal.ZERO;
			BigDecimal tongTien = BigDecimal.ZERO;
			int soGthau = dtlsGThau.size();
			for (HhDxKhlcntDsgthau gthauDtl : dtlsGThau) {
				soLuong = soLuong.add(gthauDtl.getSoLuong());
				tongTien = tongTien.add(gthauDtl.getThanhTien() == null ? BigDecimal.ZERO : gthauDtl.getThanhTien());
			}
			thopDtl.setSoLuong(soLuong);
			thopDtl.setTongTien(tongTien);
			thopDtl.setSoGthau(Long.valueOf(soGthau));
			tChuanCluong = tChuanCluong.concat(dxuat.getTchuanCluong()+",");
			thopDtls.add(thopDtl);
		}
		thopHdr.setTchuanCluong(tChuanCluong);
		// Quy doi don vi do luong khoi luong
//		UnitScaler.reverseFormatList(thopDtls, Contains.DVT_TAN);
		thopHdr.setHhDxKhLcntThopDtlList(thopDtls);
		return thopHdr;
	}

	@Override
	@Transactional()
	public HhDxKhLcntThopHdr create(HhDxKhLcntThopHdrReq objReq, HttpServletRequest req) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())){
			throw new Exception("Loại vật tư hàng hóa không phù hợp");
		}
		// Set thong tin hdr tong hop
		HhDxKhLcntThopHdr thopHdr = sumarryData(objReq,req);
		thopHdr.setNgayTao(getDateTimeNow());
		thopHdr.setNguoiTao(getUser().getUsername());
		thopHdr.setNoiDung(objReq.getNoiDung());
		thopHdr.setTrangThai(Contains.CHUATAO_QD);
		thopHdr.setNgayThop(new Date());
		thopHdr.setGhiChu(objReq.getGhiChu());
		hhDxKhLcntThopHdrRepository.save(thopHdr);
		for (HhDxKhLcntThopDtl dtl : thopHdr.getHhDxKhLcntThopDtlList()){
			dtl.setIdThopHdr(thopHdr.getId());
			hhDxKhLcntThopDtlRepository.save(dtl);
		}
		if (thopHdr.getId() > 0 && thopHdr.getHhDxKhLcntThopDtlList().size() > 0) {
			List<String> soDxuatList = thopHdr.getHhDxKhLcntThopDtlList().stream().map(HhDxKhLcntThopDtl::getSoDxuat)
					.collect(Collectors.toList());
			hhDxuatKhLcntHdrRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP);
		}
		return thopHdr;
	}

	@Override
	@Transactional()
	public HhDxKhLcntThopHdr update(HhDxKhLcntThopHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		HhDxKhLcntThopHdr dataDTB = qOptional.get();
		HhDxKhLcntThopHdr dataMap = ObjectMapperUtils.map(objReq, HhDxKhLcntThopHdr.class);

		updateObjectToObject(dataDTB, dataMap);
		hhDxKhLcntThopHdrRepository.save(dataDTB);
		this.setPhuongAnId(dataDTB);
		return dataDTB;
	}

	@Override
	public HhDxKhLcntThopHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		HhDxKhLcntThopHdr hdrThop = qOptional.get();

		// Lay danh muc dung chung
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

		hdrThop.setTenLoaiVthh(hashMapDmHh.get(hdrThop.getLoaiVthh()));
		hdrThop.setTenCloaiVthh(hashMapDmHh.get(hdrThop.getCloaiVthh()));

		List<HhDxKhLcntThopDtl> listTh = hhDxKhLcntThopDtlRepository.findByIdThopHdr(hdrThop.getId());
		Map<String, String> mapDmucDvi = getMapTenDvi();
		listTh.forEach(f -> {
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
		});
		hdrThop.setHhDxKhLcntThopDtlList(listTh);

		return hdrThop;
	}

	@Override
	public Page<HhDxKhLcntThopHdr> colection(HhDxKhLcntThopSearchReq objReq) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit);

		Page<HhDxKhLcntThopHdr> qhKho = hhDxKhLcntThopHdrRepository
				.findAll(HhDxKhLcntThopSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		for (HhDxKhLcntThopHdr hdr : qhKho.getContent()) {
			hdr.setTenHthucLcnt(mapDmuc.get(hdr.getHthucLcnt()));
			hdr.setTenPthucLcnt(mapDmuc.get(hdr.getPthucLcnt()));
			hdr.setTenLoaiHdong(mapDmuc.get(hdr.getLoaiHdong()));
			hdr.setTenNguonVon(mapDmuc.get(hdr.getNguonVon()));
			this.setPhuongAnId(hdr);
		}
		return qhKho;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhDxKhLcntThopHdr> optional = hhDxKhLcntThopHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		List<HhDxKhLcntThopDtl> listDls= hhDxKhLcntThopDtlRepository.findByIdThopHdr(optional.get().getId());
		if(!CollectionUtils.isEmpty(listDls)){
			List<Long> idDxList = listDls.stream().map(HhDxKhLcntThopDtl::getIdDxHdr).collect(Collectors.toList());
			List<HhDxuatKhLcntHdr> listDxHdr = hhDxuatKhLcntHdrRepository.findByIdIn(idDxList);
			if(!CollectionUtils.isEmpty(listDxHdr)){
				listDxHdr.stream().map(item ->{
					item.setTrangThaiTh(Contains.CHUATONGHOP);
					return item;
				}).collect(Collectors.toList());
			}
			hhDxuatKhLcntHdrRepository.saveAll(listDxHdr);
		}
		hhDxKhLcntThopDtlRepository.deleteAll(listDls);
		hhDxKhLcntThopHdrRepository.delete(optional.get());
	}

	@Override
	public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getIdList()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		List<HhDxKhLcntThopHdr> listThop= hhDxKhLcntThopHdrRepository.findAllByIdIn(idSearchReq.getIdList());
		for (HhDxKhLcntThopHdr thopHdr: listThop){
			List<HhDxKhLcntThopDtl> listDls= hhDxKhLcntThopDtlRepository.findByIdThopHdr(thopHdr.getId());
			if(!CollectionUtils.isEmpty(listDls)){
				List<Long> idDxList = listDls.stream().map(HhDxKhLcntThopDtl::getIdDxHdr).collect(Collectors.toList());
				List<HhDxuatKhLcntHdr> listDxHdr = hhDxuatKhLcntHdrRepository.findByIdIn(idDxList);
				if(!CollectionUtils.isEmpty(listDxHdr)){
					listDxHdr.stream().map(item ->{
						item.setTrangThaiTh(Contains.CHUATONGHOP);
						return item;
					}).collect(Collectors.toList());
				}
				hhDxuatKhLcntHdrRepository.saveAll(listDxHdr);
			}
			hhDxKhLcntThopDtlRepository.deleteAll(listDls);
		}
		hhDxKhLcntThopHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
	}

	@Override
	public List<HhDxKhLcntThopDtl> findByStatus(HhDxKhLcntDsChuaThReq objReq, HttpServletRequest req) throws Exception {
		List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
				.findAll(HhDxuatKhLcntSpecification.buildDsChuaTh(objReq));

		if (dxuatList.isEmpty())
			throw new Exception("Không tìm thấy dữ liệu");

		// Add thong tin list dtl
		List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
		for (HhDxuatKhLcntHdr dxuat : dxuatList) {
			HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
			// Set ngay min va ngay max o detail Gao
//			HhDxuatKhLcntDtl dtlsGao = dxuat.getChildren1();
//			if (dtlsGao.isEmpty())
//				continue;

			// Set thong tin chung lay tu de xuat
			thopDtl.setIdDxHdr(dxuat.getId());
			thopDtl.setMaDvi(dxuat.getMaDvi());
			thopDtl.setTenDvi(getDviByMa(dxuat.getMaDvi(), req).getTenDvi());
			thopDtl.setSoDxuat(dxuat.getSoDxuat());
			thopDtl.setNgayDxuat(dxuat.getNgayKy());
//			thopDtl.setTenDuAn(dtlsGao.getTenDuAn());

			// Add danh sach goi thau
//			List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
			BigDecimal soLuong = BigDecimal.ZERO;
			BigDecimal tongTien = BigDecimal.ZERO;
//			int soGthau = dtlsGThau.size();
//			for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
//				soLuong = soLuong.add(gthauDtl.getSoLuong());
//				tongTien = tongTien.add(gthauDtl.getThanhTien());
//			}
			thopDtl.setSoLuong(soLuong);
			thopDtl.setTongTien(tongTien);
//			thopDtl.setSoGthau(Long.valueOf(soGthau));
			thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

			thopDtls.add(thopDtl);
		}

		// Quy doi don vi do luong khoi luong
		UnitScaler.reverseFormatList(thopDtls, Contains.DVT_TAN);
		return thopDtls;
	}

	private void setPhuongAnId(HhDxKhLcntThopHdr dx) {
//		if (Contains.ACTIVE.equalsIgnoreCase(dx.getPhuongAn())) {
//			Optional<HhPaKhlcntHdr> optional = hhPaKhlcntHdrRepository.findByIdThHdr(dx.getId());
//			optional.ifPresent(hhPaKhlcntHdr -> dx.setPhuongAnId(hhPaKhlcntHdr.getId()));
//		}
	}

	@Override
	public void exportDsThDxKhLcnt(HhDxKhLcntThopSearchReq searchReq, HttpServletResponse response) throws Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		Page<HhDxKhLcntThopHdr> page = this.colection(searchReq);
		List<HhDxKhLcntThopHdr> data = page.getContent();

		String title = "Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu";
		String[] rowsName = new String[] { "STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
		"Năm kế hoạch", "Loại hàng hóa", "Chủng loại hàng hóa", "Hình thức LCNT","Phương thức LCNT","Loại hợp đồng","Nguồn vốn","Trạng thái"};
		String filename = "Tong_hop_de_xuat_ke_hoach_lua_chon_nha_thau.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhDxKhLcntThopHdr dx = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dx.getId();
			objs[2] = dx.getNgayThop();
			objs[3] = dx.getNoiDung();
			objs[4] = dx.getNamKhoach();
			objs[5] = dx.getTenLoaiVthh();
			objs[6] = dx.getTenCloaiVthh();
			objs[7] = dx.getTenHthucLcnt();
			objs[8] = dx.getTenPthucLcnt();
			objs[9] = dx.getTenLoaiHdong();
			objs[10] = dx.getTenTrangThai();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<HhDxKhLcntThopHdr> timKiemPage(HttpServletRequest request,HhDxKhLcntThopSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		Page<HhDxKhLcntThopHdr> page = hhDxKhLcntThopHdrRepository.select(req.getNamKhoach(),req.getLoaiVthh(),req.getCloaiVthh(),convertDateToString(req.getTuNgayThop()),convertDateToString(req.getDenNgayThop()),req.getNoiDung(),req.getTrangThai(), pageable);

		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		page.getContent().forEach(f -> {
			f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
			f.setTenHthucLcnt( StringUtils.isEmpty(f.getHthucLcnt()) ? null : hashMapHtLcnt.get(f.getHthucLcnt()));
			f.setTenPthucLcnt( StringUtils.isEmpty(f.getPthucLcnt()) ? null :hashMapPthucDthau.get(f.getPthucLcnt()));
			f.setTenLoaiHdong( StringUtils.isEmpty(f.getLoaiHdong()) ? null :hashMapLoaiHdong.get(f.getLoaiHdong()));
			f.setTenNguonVon( StringUtils.isEmpty(f.getNguonVon()) ? null :hashMapNguonVon.get(f.getNguonVon()));
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
		});
		return page;
	}

	@Override
	public List<HhDxKhLcntThopHdr> timKiemAll(HttpServletRequest request,HhDxKhLcntThopSearchReq req) throws Exception {
		return hhDxKhLcntThopHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(),req.getCloaiVthh(),convertDateToString(req.getTuNgayThop()),convertDateToString(req.getDenNgayThop()), req.getTrangThai());
	}
}
