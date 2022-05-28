package com.tcdt.qlnvhang.service.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.donvi.QlnvDmDonViService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BienBanLayMauServiceImpl implements BienBanLayMauService{
	@Autowired
	private BienBanLayMauRepository bienBanLayMauRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;

	@Autowired
	private QlnvDmDonViService qlnvDmDonViService;

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_INDEX = 0;

	@Override
	public Page<BienBanLayMauRes> search(BienBanLayMauSearchReq req) {
		int pageIndex = req.getPaggingReq().getPage() == null ? DEFAULT_PAGE_INDEX : req.getPaggingReq().getPage();
		int pageSize = req.getPaggingReq().getLimit() == null ? DEFAULT_PAGE_SIZE : req.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<BienBanLayMau> page = bienBanLayMauRepository.search(req, pageable);
		return new PageImpl<>(this.toResponseList(page.getContent()), pageable, page.getTotalElements());
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BienBanLayMauRes create(BienBanLayMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		BienBanLayMau bienBienLayMau = new BienBanLayMau();
		this.updateEntity(bienBienLayMau, req);
		bienBienLayMau.setTrangThai(TrangThaiEnum.MOI_TAO.getMa());
		bienBienLayMau.setNguoiTaoId(userInfo.getId());
		bienBienLayMau.setNgayTao(LocalDate.now());
		bienBienLayMau.setMaDonVi(userInfo.getDvql());
		bienBienLayMau.setCapDonVi(qlnvDmDonViService.getCapDviByMa(userInfo.getDvql()));
		bienBanLayMauRepository.save(bienBienLayMau);
		return this.toResponseList(Collections.singletonList(bienBienLayMau)).stream().findFirst().orElse(null);
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

		BienBanLayMau bienBienLayMau = new BienBanLayMau();

		bienBienLayMau.setNguoiSuaId(userInfo.getId());
		bienBienLayMau.setNgaySua(LocalDate.now());
		bienBanLayMauRepository.save(bienBienLayMau);

		return this.toResponseList(Collections.singletonList(bienBienLayMau)).stream().findFirst().orElse(null);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean updateStatus(StatusReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(req.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		BienBanLayMau bb = optional.get();

		if (TrangThaiEnum.MOI_TAO.getMa().equals(bb.getTrangThai()) && TrangThaiEnum.CHO_DUYET.getMa().equals(req.getTrangThai())) {
			bb.setTrangThai(TrangThaiEnum.CHO_DUYET.getMa());
			bb.setNgayGuiDuyet(LocalDate.now());
			bb.setNguoiPduyetId(userInfo.getId());
		} else if (TrangThaiEnum.CHO_DUYET.getMa().equals(bb.getTrangThai()) && TrangThaiEnum.DA_DUYET.getMa().equals(req.getTrangThai())) {
			bb.setNgayPduyet(LocalDate.now());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setTrangThai(TrangThaiEnum.DA_DUYET.getMa());
		} else if (TrangThaiEnum.CHO_DUYET.getMa().equals(bb.getTrangThai()) && TrangThaiEnum.TU_CHOI.getMa().equals(req.getTrangThai())) {
			bb.setNgayPduyet(LocalDate.now());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setTrangThai(TrangThaiEnum.TU_CHOI.getMa());
			bb.setLdoTchoi(req.getLyDo());
		} else {
			return false;
		}
		return false;
	}

	private void updateEntity(BienBanLayMau bb, BienBanLayMauReq req) {
		bb.setSoBban(req.getSoBban());
		bb.setCanCu(req.getCanCu());
		bb.setCtieuKtra(req.getCtieuKtra());
		bb.setCcuQdinhGiaoNvuNhap(req.getCcuQdinhGiaoNvuNhap());
		bb.setMaDviNhan(req.getMaDviNhan());
		bb.setTenDdienNhan(req.getTenDdienNhan());
		bb.setCvuDdienNhan(req.getCvuDdienNhan());
		bb.setMaDviCcap(req.getMaDviCcap());
		bb.setTenDdienCcap(req.getTenDdienCcap());
		bb.setCvuDdienCcap(req.getCvuDdienCcap());
		bb.setPphapLayMau(req.getPphapLayMau());
		bb.setSluongLMau(req.getSluongLMau());
		bb.setDdiemKtra(req.getDdiemKtra());
		bb.setMaHhoa(req.getMaHhoa());
		bb.setMaKho(req.getMaKho());
		bb.setMaNgan(req.getMaNgan());
		bb.setMaLo(req.getMaLo());
		bb.setNgayLapBban(req.getNgayLapBban());
		bb.setKquaNiemPhongMau(req.getKquaNiemPhongMau());
	}

	@Override
	public BienBanLayMauRes detail(Long id) throws Exception {
		Optional<BienBanLayMau> optional = bienBanLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		return this.toResponseList(Collections.singletonList(optional.get())).stream().findFirst().orElse(null);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean delete(Long id) {
		bienBanLayMauRepository.deleteById(id);
		return true;
	}


	private List<BienBanLayMauRes> toResponseList(List<BienBanLayMau> list) {
		Set<String> maDviSet = new HashSet<>();
		Set<String> maHhoaSet = new HashSet<>();
		for (BienBanLayMau bb : list) {
			if (!StringUtils.isEmpty(bb.getMaDviNhan()))
				maDviSet.add(bb.getMaDviNhan());

			if (!StringUtils.isEmpty(bb.getMaDviCcap()))
				maDviSet.add(bb.getMaDviCcap());

			if (!StringUtils.isEmpty(bb.getMaHhoa()))
				maDviSet.add(bb.getMaHhoa());
		}

		List<QlnvDmDonvi> donviList = qlnvDmDonviRepository.findByMaDviIn(maDviSet);
		Set<QlnvDmVattu> vattuList = qlnvDmVattuRepository.findByMaIn(maHhoaSet);

		List<BienBanLayMauRes> resList = new ArrayList<>();
		for (BienBanLayMau bb : list) {
			QlnvDmDonvi dviNhan = donviList.stream().filter(dv -> dv.getMaDvi().equals(bb.getMaDviNhan())).findFirst().orElse(null);
			QlnvDmDonvi dviCcap = donviList.stream().filter(dv -> dv.getMaDvi().equals(bb.getMaDviCcap())).findFirst().orElse(null);
			QlnvDmVattu vattu = vattuList.stream().filter(vt -> vt.getMa().equals(bb.getMaHhoa())).findFirst().orElse(null);
			BienBanLayMauRes res = this.toResponse(bb, dviNhan, dviCcap, vattu);
			if (res != null)
				resList.add(res);
		}
		return resList;
	}

	private BienBanLayMauRes toResponse(BienBanLayMau bb, QlnvDmDonvi dviNhan, QlnvDmDonvi dviCcap, QlnvDmVattu vattu) {
		if (bb == null)
			return null;

		BienBanLayMauRes res = new BienBanLayMauRes();
		BeanUtils.copyProperties(bb, res);
		res.setTrangThai(TrangThaiEnum.getTen(res.getTrangThai()));
		if (dviNhan != null)
			res.setTenDviNhan(dviNhan.getTenDvi());

		if (dviCcap != null)
			res.setTenDviCcap(dviCcap.getTenDvi());

		if (vattu != null)
			res.setTenHhoa(vattu.getTen());

		return res;
	}
}
