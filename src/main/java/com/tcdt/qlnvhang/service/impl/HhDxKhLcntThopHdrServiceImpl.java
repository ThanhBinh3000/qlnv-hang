package com.tcdt.qlnvhang.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.repository.HhDxKhLcntThopHdrRepository;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhDxKhLcntThopHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntDsChuaThReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.secification.HhDxKhLcntThopSpecification;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.service.HhDxKhLcntThopHdrService;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntGaoDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhDxKhLcntThopHdrServiceImpl extends BaseServiceImpl implements HhDxKhLcntThopHdrService {
	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@Override
	public HhDxKhLcntThopHdr sumarryData(HhDxKhLcntTChiThopReq objReq) throws Exception {
		List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
				.findAll(HhDxuatKhLcntSpecification.buildTHopQuery(objReq));

		if (dxuatList.isEmpty())
			throw new Exception("Không tìm thấy dữ liệu để tổng hợp");

		// Set thong tin hdr tong hop
		HhDxKhLcntThopHdr thopHdr = new HhDxKhLcntThopHdr();

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		thopHdr.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());
		thopHdr.setLoaiVthh(Contains.getLoaiVthh(dxuatList.get(0).getLoaiVthh()));
		thopHdr.setHthucLcnt(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getHthucLcnt()));
		thopHdr.setPthucLcnt(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getPthucLcnt()));
		thopHdr.setLoaiHdong(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getLoaiHdong()));
		thopHdr.setNguonVon(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getNguonVon()));

		// Add thong tin list dtl
		List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
		for (HhDxuatKhLcntHdr dxuat : dxuatList) {
			HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
			// Set ngay min va ngay max o detail Gao
			List<HhDxuatKhLcntGaoDtl> dtlsGao = dxuat.getChildren1();
			if (dtlsGao.isEmpty())
				continue;

			for (HhDxuatKhLcntGaoDtl dxuatGao : dtlsGao) {
				if (StringUtils.isEmpty(thopHdr.getTuTgianTbao())
						|| thopHdr.getTuTgianTbao().compareTo(dxuatGao.getTgianTbao()) > 0)
					thopHdr.setTuTgianTbao(dxuatGao.getTgianTbao());
				if (StringUtils.isEmpty(thopHdr.getDenTgianTbao())
						|| thopHdr.getDenTgianTbao().compareTo(dxuatGao.getTgianTbao()) < 0)
					thopHdr.setDenTgianTbao(dxuatGao.getTgianTbao());

				if (StringUtils.isEmpty(thopHdr.getTuTgianMthau())
						|| thopHdr.getTuTgianMthau().compareTo(dxuatGao.getTgianMoThau()) > 0)
					thopHdr.setTuTgianMthau(dxuatGao.getTgianMoThau());
				if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
						|| thopHdr.getDenTgianMthau().compareTo(dxuatGao.getTgianMoThau()) < 0)
					thopHdr.setDenTgianMthau(dxuatGao.getTgianMoThau());

				if (StringUtils.isEmpty(thopHdr.getTuTgianDthau())
						|| thopHdr.getTuTgianDthau().compareTo(dxuatGao.getTgianDongThau()) > 0)
					thopHdr.setTuTgianDthau(dxuatGao.getTgianDongThau());
				if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
						|| thopHdr.getDenTgianDthau().compareTo(dxuatGao.getTgianDongThau()) < 0)
					thopHdr.setDenTgianDthau(dxuatGao.getTgianDongThau());

				if (StringUtils.isEmpty(thopHdr.getTuTgianNhang())
						|| thopHdr.getTuTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) > 0)
					thopHdr.setTuTgianNhang(dxuatGao.getTgianNhapHang());
				if (StringUtils.isEmpty(thopHdr.getDenTgianNhang())
						|| thopHdr.getDenTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) < 0)
					thopHdr.setDenTgianNhang(dxuatGao.getTgianNhapHang());
			}

			// Set thong tin chung lay tu de xuat
			thopDtl.setIdDxHdr(dxuat.getId());
			thopDtl.setMaDvi(dxuat.getMaDvi());
			thopDtl.setTenDvi(getDviByMa(dxuat.getMaDvi()).getTenDvi());
			thopDtl.setSoDxuat(dxuat.getSoDxuat());
			thopDtl.setNgayDxuat(dxuat.getNgayKy());
			thopDtl.setTenDuAn(dtlsGao.get(0).getTenDuAn());

			// Add danh sach goi thau
			List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
			BigDecimal soLuong = BigDecimal.ZERO;
			BigDecimal tongTien = BigDecimal.ZERO;
			int soGthau = dtlsGThau.size();
			for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
				soLuong = soLuong.add(gthauDtl.getSoLuong());
				tongTien = tongTien.add(gthauDtl.getThanhTien());
			}
			thopDtl.setSoLuong(soLuong);
			thopDtl.setTongTien(tongTien);
			thopDtl.setSoGthau(Long.valueOf(soGthau));
			thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

			thopDtls.add(thopDtl);
		}

		// Quy doi don vi do luong khoi luong
		UnitScaler.reverseFormatList(thopDtls, Contains.DVT_TAN);
		thopHdr.setChildren(thopDtls);
		return thopHdr;
	}

	@Override
	public HhDxKhLcntThopHdr create(HhDxKhLcntThopHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
				.findAll(HhDxuatKhLcntSpecification.buildTHopQuery(objReq));

		if (dxuatList.isEmpty())
			throw new Exception("Không tìm thấy dữ liệu để tổng hợp");

		// Set thong tin hdr tong hop
		HhDxKhLcntThopHdr thopHdr = new HhDxKhLcntThopHdr();

		thopHdr.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());
		thopHdr.setLoaiVthh(dxuatList.get(0).getLoaiVthh());
		thopHdr.setHthucLcnt(dxuatList.get(0).getChildren1().get(0).getHthucLcnt());
		thopHdr.setPthucLcnt(dxuatList.get(0).getChildren1().get(0).getPthucLcnt());
		thopHdr.setLoaiHdong(dxuatList.get(0).getChildren1().get(0).getLoaiHdong());
		thopHdr.setNguonVon(dxuatList.get(0).getChildren1().get(0).getNguonVon());

		thopHdr.setNgayTao(getDateTimeNow());
		thopHdr.setNguoiTao(getUser().getUsername());
		thopHdr.setVeViec(objReq.getVeViec());

		// Add thong tin list dtl
		List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
		for (HhDxuatKhLcntHdr dxuat : dxuatList) {
			HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
			// Set ngay min va ngay max o detail Gao
			List<HhDxuatKhLcntGaoDtl> dtlsGao = dxuat.getChildren1();
			if (dtlsGao.isEmpty())
				continue;

			for (HhDxuatKhLcntGaoDtl dxuatGao : dtlsGao) {
				if (StringUtils.isEmpty(thopHdr.getTuTgianTbao())
						|| thopHdr.getTuTgianTbao().compareTo(dxuatGao.getTgianTbao()) > 0)
					thopHdr.setTuTgianTbao(dxuatGao.getTgianTbao());
				if (StringUtils.isEmpty(thopHdr.getDenTgianTbao())
						|| thopHdr.getDenTgianTbao().compareTo(dxuatGao.getTgianTbao()) < 0)
					thopHdr.setDenTgianTbao(dxuatGao.getTgianTbao());

				if (StringUtils.isEmpty(thopHdr.getTuTgianMthau())
						|| thopHdr.getTuTgianMthau().compareTo(dxuatGao.getTgianMoThau()) > 0)
					thopHdr.setTuTgianMthau(dxuatGao.getTgianMoThau());
				if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
						|| thopHdr.getDenTgianMthau().compareTo(dxuatGao.getTgianMoThau()) < 0)
					thopHdr.setDenTgianMthau(dxuatGao.getTgianMoThau());

				if (StringUtils.isEmpty(thopHdr.getTuTgianDthau())
						|| thopHdr.getTuTgianDthau().compareTo(dxuatGao.getTgianDongThau()) > 0)
					thopHdr.setTuTgianDthau(dxuatGao.getTgianDongThau());
				if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
						|| thopHdr.getDenTgianDthau().compareTo(dxuatGao.getTgianDongThau()) < 0)
					thopHdr.setDenTgianDthau(dxuatGao.getTgianDongThau());

				if (StringUtils.isEmpty(thopHdr.getTuTgianNhang())
						|| thopHdr.getTuTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) > 0)
					thopHdr.setTuTgianNhang(dxuatGao.getTgianNhapHang());
				if (StringUtils.isEmpty(thopHdr.getDenTgianNhang())
						|| thopHdr.getDenTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) < 0)
					thopHdr.setDenTgianNhang(dxuatGao.getTgianNhapHang());
			}

			// Set thong tin chung lay tu de xuat
			thopDtl.setIdDxHdr(dxuat.getId());
			thopDtl.setMaDvi(dxuat.getMaDvi());
			thopDtl.setTenDvi(getDviByMa(dxuat.getMaDvi()).getTenDvi());
			thopDtl.setSoDxuat(dxuat.getSoDxuat());
			thopDtl.setNgayDxuat(dxuat.getNgayKy());
			thopDtl.setTenDuAn(dtlsGao.get(0).getTenDuAn());

			// Add danh sach goi thau
			List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
			BigDecimal soLuong = BigDecimal.ZERO;
			BigDecimal tongTien = BigDecimal.ZERO;
			int soGthau = dtlsGThau.size();
			for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
				soLuong = soLuong.add(gthauDtl.getSoLuong());
				tongTien = tongTien.add(gthauDtl.getThanhTien());
			}
			thopDtl.setSoLuong(soLuong);
			thopDtl.setTongTien(tongTien);
			thopDtl.setSoGthau(Long.valueOf(soGthau));
			thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

			thopDtls.add(thopDtl);
		}
		thopHdr.setChildren(thopDtls);

		HhDxKhLcntThopHdr createCheck = hhDxKhLcntThopHdrRepository.save(thopHdr);
		if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
			List<String> soDxuatList = createCheck.getChildren().stream().map(HhDxKhLcntThopDtl::getSoDxuat)
					.collect(Collectors.toList());
			hhDxuatKhLcntHdrRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
		}
		return createCheck;
	}

	@Override
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

		return hhDxKhLcntThopHdrRepository.save(dataDTB);
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
		Map<String, String> mapDmuc = getMapCategory();

		hdrThop.setTenLoaiVthh(Contains.getLoaiVthh(hdrThop.getLoaiVthh()));
		hdrThop.setTenHthucLcnt(mapDmuc.get(hdrThop.getHthucLcnt()));
		hdrThop.setTenPthucLcnt(mapDmuc.get(hdrThop.getPthucLcnt()));
		hdrThop.setTenLoaiHdong(mapDmuc.get(hdrThop.getLoaiHdong()));
		hdrThop.setTenNguonVon(mapDmuc.get(hdrThop.getNguonVon()));

		// Quy doi don vi kg = tan
		List<HhDxKhLcntThopDtl> dtls = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhDxKhLcntThopDtl.class);
		UnitScaler.formatList(dtls, Contains.DVT_TAN);
		qOptional.get().setChildren(dtls);
		return qOptional.get();
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

		if (optional.get().getPhuongAn().equals(Contains.ACTIVE))
			throw new Exception("Tổng hợp đã được lập phương án trình Tổng cục, không được phép xóa");

		hhDxKhLcntThopHdrRepository.delete(optional.get());
		List<String> soDxuatList = optional.get().getChildren().stream().map(HhDxKhLcntThopDtl::getSoDxuat)
				.collect(Collectors.toList());
		hhDxuatKhLcntHdrRepository.updateTongHop(soDxuatList, Contains.DUYET);

	}

	@Override
	public List<HhDxKhLcntThopDtl> findByStatus(HhDxKhLcntDsChuaThReq objReq) throws Exception {
		List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
				.findAll(HhDxuatKhLcntSpecification.buildDsChuaTh(objReq));

		if (dxuatList.isEmpty())
			throw new Exception("Không tìm thấy dữ liệu");

		// Add thong tin list dtl
		List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
		for (HhDxuatKhLcntHdr dxuat : dxuatList) {
			HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
			// Set ngay min va ngay max o detail Gao
			List<HhDxuatKhLcntGaoDtl> dtlsGao = dxuat.getChildren1();
			if (dtlsGao.isEmpty())
				continue;

			// Set thong tin chung lay tu de xuat
			thopDtl.setIdDxHdr(dxuat.getId());
			thopDtl.setMaDvi(dxuat.getMaDvi());
			thopDtl.setTenDvi(getDviByMa(dxuat.getMaDvi()).getTenDvi());
			thopDtl.setSoDxuat(dxuat.getSoDxuat());
			thopDtl.setNgayDxuat(dxuat.getNgayKy());
			thopDtl.setTenDuAn(dtlsGao.get(0).getTenDuAn());

			// Add danh sach goi thau
			List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
			BigDecimal soLuong = BigDecimal.ZERO;
			BigDecimal tongTien = BigDecimal.ZERO;
			int soGthau = dtlsGThau.size();
			for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
				soLuong = soLuong.add(gthauDtl.getSoLuong());
				tongTien = tongTien.add(gthauDtl.getThanhTien());
			}
			thopDtl.setSoLuong(soLuong);
			thopDtl.setTongTien(tongTien);
			thopDtl.setSoGthau(Long.valueOf(soGthau));
			thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

			thopDtls.add(thopDtl);
		}

		// Quy doi don vi do luong khoi luong
		UnitScaler.reverseFormatList(thopDtls, Contains.DVT_TAN);
		return thopDtls;
	}

}
