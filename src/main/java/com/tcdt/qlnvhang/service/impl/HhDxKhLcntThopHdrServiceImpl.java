package com.tcdt.qlnvhang.service.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhlcntThopHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntDsgtDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.DsChiCucPreview;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntThopPreview;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
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

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhDxKhlcntDsgthauCtietRepository hhDxKhlcntDsgthauCtietRepository;

	@Autowired
	private HhQdKhlcntDtlRepository hhQdKhlcntDtlRepository;

	@Autowired
	private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

	@Override
	public HhDxKhLcntThopHdr sumarryData(HhDxKhLcntTChiThopReq objReq) throws Exception {
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
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

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
//			// Set ngay min va ngay max o detail
//			// Set min max ngày bắt đầu tổ chức
//			if (StringUtils.isEmpty(thopHdr.getTgianBdauTchucTu())
//					|| thopHdr.getTgianBdauTchucTu().compareTo(dxuat.getTgianBdauTchuc()) > 0){
//				thopHdr.setTgianBdauTchucTu(dxuat.getTgianBdauTchuc());
//			}
//			if (StringUtils.isEmpty(thopHdr.getTgianBdauTchucDen())
//					|| thopHdr.getTgianBdauTchucDen().compareTo(dxuat.getTgianBdauTchuc()) < 0){
//				thopHdr.setTgianBdauTchucDen(dxuat.getTgianBdauTchuc());
//			}
//			// Set ngày min max ngày mở thầu
//			if (StringUtils.isEmpty(thopHdr.getTgianMthauTu())
//					|| thopHdr.getTgianMthauTu().compareTo(dxuat.getTgianMthau()) > 0){
//				thopHdr.setTgianMthauTu(dxuat.getTgianMthau());
//			}
//			if (StringUtils.isEmpty(thopHdr.getTgianMthauDen())
//					|| thopHdr.getTgianMthauDen().compareTo(dxuat.getTgianMthau()) < 0){
//				thopHdr.setTgianMthauDen(dxuat.getTgianMthau());
//			}
//			// Set ngày min max ngày đóng thầu
//			if (StringUtils.isEmpty(thopHdr.getTgianDthauTu())
//					|| thopHdr.getTgianDthauTu().compareTo(dxuat.getTgianDthau()) > 0){
//				thopHdr.setTgianDthauTu(dxuat.getTgianDthau());
//			}
//			if (StringUtils.isEmpty(thopHdr.getTgianDthauDen())
//					|| thopHdr.getTgianDthauDen().compareTo(dxuat.getTgianDthau()) < 0){
//				thopHdr.setTgianDthauDen(dxuat.getTgianDthau());
//			}
//			// Set ngày mim max nhập hàng
//			if (StringUtils.isEmpty(thopHdr.getTgianNhangTu())
//					|| thopHdr.getTgianNhangTu().compareTo(dxuat.getTgianNhang()) > 0){
//				thopHdr.setTgianNhangTu(dxuat.getTgianNhang());
//			}
//			if (StringUtils.isEmpty(thopHdr.getTgianNhangDen())
//					|| thopHdr.getTgianNhangDen().compareTo(dxuat.getTgianNhang()) < 0){
//				thopHdr.setTgianNhangDen(dxuat.getTgianNhang());
//			}

			// Set thong tin chung lay tu de xuat
			thopDtl.setIdDxHdr(dxuat.getId());
			thopDtl.setMaDvi(dxuat.getMaDvi());
			thopDtl.setTenDvi(mapDmucDvi.get(dxuat.getMaDvi()));
			thopDtl.setSoDxuat(dxuat.getSoDxuat());
			thopDtl.setNgayTao(dxuat.getNgayTao());
			thopDtl.setNgayPduyet(dxuat.getNgayPduyet());
			thopDtl.setDiaChiDvi(dxuat.getDiaChiDvi());
			thopDtl.setTenDuAn(dxuat.getTenDuAn());
			thopDtl.setTrichYeu(dxuat.getTrichYeu());
			thopDtl.setDonGiaVat(dxuat.getDonGiaVat());
			thopDtl.setTongTien(dxuat.getTongMucDtDx());
			// Add danh sach goi thau
			List<HhDxKhlcntDsgthau> dtlsGThau = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcntOrderByGoiThau(dxuat.getId());
			BigDecimal soLuong = BigDecimal.ZERO;
			BigDecimal tongTien = BigDecimal.ZERO;
			int soGthau = dtlsGThau.size();
			for (HhDxKhlcntDsgthau gthauDtl : dtlsGThau) {
				soLuong = soLuong.add(gthauDtl.getSoLuong());
				tongTien = tongTien.add(gthauDtl.getThanhTien() == null ? BigDecimal.ZERO : gthauDtl.getThanhTien());
			}
			thopDtl.setSoLuong(soLuong);
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
		HhDxKhLcntThopHdr thopHdr = sumarryData(objReq);
		thopHdr.setNgayTao(getDateTimeNow());
		thopHdr.setNguoiTao(getUser().getUsername());
		thopHdr.setNoiDung(objReq.getNoiDung());
		thopHdr.setTrangThai(Contains.CHUATAO_QD);
		thopHdr.setNgayThop(new Date());
		thopHdr.setGhiChu(objReq.getGhiChu());
		thopHdr.setMaTh(objReq.getMaTh());
		thopHdr.setSoQdCc(objReq.getSoQdCc());
		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhlcntThopHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhlcntThopHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinDxKhlcntThopHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxKhLcntThopHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
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
		if (objReq.getId() == null ){
			throw new Exception("Không tìm thấy bản ghi tổng hợp");
		}
		Optional<HhDxKhLcntThopHdr> thopHdr = hhDxKhLcntThopHdrRepository.findById(objReq.getId());
		if (!thopHdr.isPresent()){
			throw new Exception("Không tìm thấy bản ghi tổng hợp");
		}
		thopHdr.get().setNoiDung(objReq.getNoiDung());
		return hhDxKhLcntThopHdrRepository.save(thopHdr.get());
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
		if (StringUtils.isEmpty(idSearchReq.getId())) {
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		}
		Optional<HhDxKhLcntThopHdr> optional = hhDxKhLcntThopHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		}
		if (optional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
			throw new Exception("Xóa thất bại, quyết định đã được ban hành.");
		}
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
 		if (optional.get().getTrangThai().equals(Contains.DADUTHAO_QD)) {
			Optional<HhQdKhlcntHdr> qdKhlcntHdr = hhQdKhlcntHdrRepository.findByIdThHdrAndLastest(optional.get().getId(), false);
			if (!qdKhlcntHdr.isPresent() || qdKhlcntHdr.get().getTrangThai().equals(Contains.BAN_HANH)){
				throw new Exception("Không tìm thấy bản ghi quyết định hoặc quyết định đã được ban hành");
			}
			List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findAllByIdQdHdr(optional.get().getId());
			if(!CollectionUtils.isEmpty(hhQdKhlcntDtl)){
				for (HhQdKhlcntDtl dtl:hhQdKhlcntDtl) {
					List<HhQdKhlcntDsgthau> byIdQdDtl = hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId());
					for (HhQdKhlcntDsgthau gThau :byIdQdDtl) {
						hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gThau.getId());
					}
					hhQdKhlcntDsgthauRepository.deleteByIdQdDtl(dtl.getId());
				}
				hhQdKhlcntDtlRepository.deleteAll(hhQdKhlcntDtl);
			}
			hhQdKhlcntHdrRepository.delete(qdKhlcntHdr.get());
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
//			thopDtl.setNgayDxuat(dxuat.getNgayKy());
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
//			thopDtl.setTongTien(tongTien);
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
		"Năm kế hoạch", "Loại hàng hóa", "Chủng loại hàng hóa", "Hình thức LCNT","Phương thức LCNT","Loại hợp đồng","Nguồn vốn","Số QĐ PD KHLCNT","Trạng thái"};
		String filename = "Tong_hop_de_xuat_ke_hoach_lua_chon_nha_thau.xlsx";

		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhDxKhLcntThopHdr dx = data.get(i);
			Optional<HhQdKhlcntHdr> hhQdKhlcntHdr = hhQdKhlcntHdrRepository.findByIdThHdrAndLastest(dx.getId(), true);

			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dx.getId();
			objs[2] = dx.getNgayThop();
			objs[3] = dx.getNoiDung();
			objs[4] = dx.getNamKhoach();
			objs[5] = StringUtils.isEmpty(dx.getLoaiVthh()) ? null : hashMapDmHh.get(dx.getLoaiVthh());
			objs[6] = StringUtils.isEmpty(dx.getCloaiVthh()) ? null : hashMapDmHh.get(dx.getCloaiVthh());
			objs[7] = StringUtils.isEmpty(dx.getHthucLcnt()) ? null : hashMapHtLcnt.get(dx.getHthucLcnt());
			objs[8] = StringUtils.isEmpty(dx.getPthucLcnt()) ? null :hashMapPthucDthau.get(dx.getPthucLcnt());
			objs[9] = StringUtils.isEmpty(dx.getLoaiHdong()) ? null :hashMapLoaiHdong.get(dx.getLoaiHdong());
			objs[10] = StringUtils.isEmpty(dx.getNguonVon()) ? null :hashMapNguonVon.get(dx.getNguonVon());
			if(hhQdKhlcntHdr.isPresent()){
				dx.setSoQdCc(hhQdKhlcntHdr.get().getSoQd());
				objs[11] = dx.getSoQdCc();
			}else{
				objs[11] = null;
			}
			objs[12] = NhapXuatHangTrangThaiEnum.getTenById(dx.getTrangThai());
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<HhDxKhLcntThopHdr> timKiemPage(HttpServletRequest request,HhDxKhLcntThopSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<HhDxKhLcntThopHdr> page = hhDxKhLcntThopHdrRepository.select(req.getNamKhoach(),req.getLoaiVthh(),req.getCloaiVthh(),convertDateToString(req.getTuNgayThop()),convertDateToString(req.getDenNgayThop()),req.getNoiDung(),req.getTrangThai(), pageable);
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		page.getContent().forEach(f -> {
			Optional<HhQdKhlcntHdr> hhQdKhlcntHdr = hhQdKhlcntHdrRepository.findByIdThHdrAndLastest(f.getId(), true);
			if(hhQdKhlcntHdr.isPresent()){
				f.setSoQdCc(hhQdKhlcntHdr.get().getSoQd());
				f.setQdPdKhlcntId(hhQdKhlcntHdr.get().getId());
			}
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

	@Override
	public ReportTemplateResponse preview(HhDxKhLcntThopSearchReq objReq) throws Exception {
		Optional<HhDxKhLcntThopHdr> optional = hhDxKhLcntThopHdrRepository.findById(objReq.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu");
		}
		ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		HhDxuatKhLcntThopPreview object = new HhDxuatKhLcntThopPreview();
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		List<HhDxKhLcntThopDtl> listTh = hhDxKhLcntThopDtlRepository.findByIdThopHdr(optional.get().getId());
		Map<String, String> mapDmucDvi = getMapTenDvi();
		AtomicReference<BigDecimal> tongSl = new AtomicReference<>(BigDecimal.ZERO);
		AtomicReference<Long> tongSoGthau = new AtomicReference<>(0L);
		listTh.forEach(f -> {
			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(f.getIdDxHdr());
			if (qOptional.isPresent()) {
				try {
					f.setTgianDongThau(convertDate(qOptional.get().getTgianDthau()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcntOrderByGoiThau(f.getIdDxHdr());
			List<DsChiCucPreview> dsChiCuc = new ArrayList<>();
			for (HhDxKhlcntDsgthau dsG : dsGthauList) {
				List<HhDxKhlcntDsgthauCtiet> listDdNhap = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
				listDdNhap.forEach(ctiet -> {
					DsChiCucPreview chiCuc = new DsChiCucPreview();
					chiCuc.setDonGia(docxToPdfConverter.convertBigDecimalToStr(dsG.getDonGiaTamTinh()));
					chiCuc.setChiCuc(StringUtils.isEmpty(ctiet.getMaDvi()) ? null : mapDmucDvi.get(ctiet.getMaDvi()));
					dsChiCuc.add(chiCuc);
				});
			}
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
			f.setDsChiCucPreviews(dsChiCuc.stream().distinct().collect(Collectors.toList()));
			f.setSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(f.getSoLuong()));
			tongSl.updateAndGet(v -> v.add(f.getSoLuong()));
			tongSoGthau.updateAndGet(v -> v + f.getSoGthau());
		});
		object.setDetails(listTh);
		object.setNamKhoach(optional.get().getNamKhoach().toString());
		object.setTongSl(docxToPdfConverter.convertBigDecimalToStr(tongSl.get()));
		object.setTongSoGthau(tongSoGthau.toString());
		object.setTenLoaiVthh(hashMapDmHh.get(optional.get().getLoaiVthh()).toUpperCase());
		return docxToPdfConverter.convertDocxToPdf(inputStream, object);
	}
}
