package com.tcdt.qlnvhang.service.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.enums.HhBbNghiemthuKlstStatusEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bbanbangiaomau.BienBanBanGiaoMauRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanBanGiaoMauReq;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.donvi.QlnvDmDonViService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.UserUtils;
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
import java.util.*;

@Service
public class BienBanBanGiaoMauServiceImpl implements BienBanBanGiaoMauService {
	@Autowired
	private BienBanBanGiaoMauRepository bienBanBanGiaoMauRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;

	@Autowired
	private QlnvDmDonViService qlnvDmDonViService;

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_INDEX = 0;

	@Override
	public Page<BienBanBanGiaoMauRes> search(BienBanBanGiaoMauSearchReq req) {
		int pageIndex = req.getPaggingReq().getPage() == null ? DEFAULT_PAGE_INDEX : req.getPaggingReq().getPage();
		int pageSize = req.getPaggingReq().getLimit() == null ? DEFAULT_PAGE_SIZE : req.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		List<BienBanBanGiaoMau> data = bienBanBanGiaoMauRepository.search(req, pageable);
		return new PageImpl<>(this.toResponseList(data), pageable, bienBanBanGiaoMauRepository.countBienBan(req));
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public BienBanBanGiaoMauRes create(BienBanBanGiaoMauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		BienBanBanGiaoMau bienBienBanGiaoMau = new BienBanBanGiaoMau();
		BeanUtils.copyProperties(req, bienBienBanGiaoMau, "id");
		bienBienBanGiaoMau.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		bienBienBanGiaoMau.setNguoiTaoId(userInfo.getId());
		bienBienBanGiaoMau.setNgayTao(LocalDate.now());
		bienBienBanGiaoMau.setMaDonVi(userInfo.getDvql());
		bienBienBanGiaoMau.setCapDonVi(qlnvDmDonViService.getCapDviByMa(userInfo.getDvql()));
		bienBanBanGiaoMauRepository.save(bienBienBanGiaoMau);
		return this.toResponseList(Collections.singletonList(bienBienBanGiaoMau)).stream().findFirst().orElse(null);
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

		BienBanBanGiaoMau bienBienBanGiaoMau = optional.get();
		BeanUtils.copyProperties(req, bienBienBanGiaoMau, "id");
		bienBienBanGiaoMau.setNguoiSuaId(userInfo.getId());
		bienBienBanGiaoMau.setNgaySua(LocalDate.now());
		bienBanBanGiaoMauRepository.save(bienBienBanGiaoMau);

		return this.toResponseList(Collections.singletonList(bienBienBanGiaoMau)).stream().findFirst().orElse(null);
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
		if (HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO.getId().equals(trangThai))
				return false;

			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId());
			bb.setNguoiGuiDuyetId(userInfo.getId());
			bb.setNgayGuiDuyet(LocalDate.now());
		} else if (HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;
			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else if (HhBbNghiemthuKlstStatusEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId().equals(trangThai))
				return false;

			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.BAN_HANH.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
		} else if (HhBbNghiemthuKlstStatusEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;

			bb.setTrangThai(HhBbNghiemthuKlstStatusEnum.TU_CHOI.getId());
			bb.setNguoiPduyetId(userInfo.getId());
			bb.setNgayPduyet(LocalDate.now());
			bb.setLdoTchoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		bienBanBanGiaoMauRepository.save(bb);
		return false;
	}

	private void updateEntity(BienBanBanGiaoMau bb, BienBanBanGiaoMauReq req) {
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
		bb.setSluongLmau(req.getSluongLmau());
		bb.setDdiemKtra(req.getDdiemKtra());
		bb.setMaHhoa(req.getMaHhoa());
		bb.setMaKho(req.getMaKho());
		bb.setMaNgan(req.getMaNgan());
		bb.setMaLo(req.getMaLo());
		bb.setNgayLapBban(req.getNgayLapBban());
		bb.setKquaNiemPhongMau(req.getKquaNiemPhongMau());
	}

	@Override
	public BienBanBanGiaoMauRes detail(Long id) throws Exception {
		Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		return this.toResponseList(Collections.singletonList(optional.get())).stream().findFirst().orElse(null);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<BienBanBanGiaoMau> optional = bienBanBanGiaoMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		BienBanBanGiaoMau bb = optional.get();

		if (TrangThaiEnum.BAN_HANH.getId().equals(bb.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã ban hành");
		}
		bienBanBanGiaoMauRepository.deleteById(id);
		return true;
	}


	private List<BienBanBanGiaoMauRes> toResponseList(List<BienBanBanGiaoMau> list) {
		Set<String> maDviSet = new HashSet<>();
		Set<String> maHhoaSet = new HashSet<>();
		for (BienBanBanGiaoMau bb : list) {
			if (!StringUtils.isEmpty(bb.getMaDviNhan()))
				maDviSet.add(bb.getMaDviNhan());

			if (!StringUtils.isEmpty(bb.getMaDviCcap()))
				maDviSet.add(bb.getMaDviCcap());

			if (!StringUtils.isEmpty(bb.getMaHhoa()))
				maDviSet.add(bb.getMaHhoa());
		}

		List<QlnvDmDonvi> donviList = qlnvDmDonviRepository.findByMaDviIn(maDviSet);
		Set<QlnvDmVattu> vattuList = qlnvDmVattuRepository.findByMaIn(maHhoaSet);

		List<BienBanBanGiaoMauRes> resList = new ArrayList<>();
		for (BienBanBanGiaoMau bb : list) {
			QlnvDmDonvi dviNhan = donviList.stream().filter(dv -> dv.getMaDvi().equals(bb.getMaDviNhan())).findFirst().orElse(null);
			QlnvDmDonvi dviCcap = donviList.stream().filter(dv -> dv.getMaDvi().equals(bb.getMaDviCcap())).findFirst().orElse(null);
			QlnvDmVattu vattu = vattuList.stream().filter(vt -> vt.getMa().equals(bb.getMaHhoa())).findFirst().orElse(null);
			BienBanBanGiaoMauRes res = this.toResponse(bb, dviNhan, dviCcap, vattu);
			if (res != null)
				resList.add(res);
		}
		return resList;
	}

	private BienBanBanGiaoMauRes toResponse(BienBanBanGiaoMau bb, QlnvDmDonvi dviNhan, QlnvDmDonvi dviCcap, QlnvDmVattu vattu) {
		if (bb == null)
			return null;

		BienBanBanGiaoMauRes res = new BienBanBanGiaoMauRes();
		BeanUtils.copyProperties(bb, res);
		res.setTrangThai(bb.getTrangThai());
		if (dviNhan != null)
			res.setTenDviNhan(dviNhan.getTenDvi());

		if (dviCcap != null)
			res.setTenDviCcap(dviCcap.getTenDvi());

		if (vattu != null)
			res.setTenHhoa(vattu.getTen());

		res.setTenTrangThai(TrangThaiEnum.getTenById(bb.getTrangThai()));
		res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(bb.getTrangThai()));
		return res;
	}
}
