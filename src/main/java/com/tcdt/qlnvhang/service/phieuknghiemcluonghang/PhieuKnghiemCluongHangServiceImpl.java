package com.tcdt.qlnvhang.service.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.phieuknghiemcluonghang.PhieuKnghiemCluongHangRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.KquaKnghiemRes;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.PhieuKnghiemCluongHangRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PhieuKnghiemCluongHangServiceImpl implements PhieuKnghiemCluongHangService {
	@Autowired
	private PhieuKnghiemCluongHangRepository phieuKnghiemCluongHangRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private KquaKnghiemService kquaKnghiemService;

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_INDEX = 0;

	@Override
	public Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) {
		return null;
	}

	@Override
	public PhieuKnghiemCluongHangRes create(PhieuKnghiemCluongHangReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		PhieuKnghiemCluongHang phieuKnclh = new PhieuKnghiemCluongHang();
		this.updateEntity(phieuKnclh, req);
		phieuKnclh.setNguoiTaoId(userInfo.getId());
		phieuKnclh.setNgayTao(LocalDate.now());

		return this.detail(phieuKnclh);
	}

	@Override
	public PhieuKnghiemCluongHangRes update(PhieuKnghiemCluongHangReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		PhieuKnghiemCluongHang phieuKnclh = optional.get();
		this.updateEntity(phieuKnclh, req);
		phieuKnclh.setNguoiSuaId(userInfo.getId());
		phieuKnclh.setNgaySua(LocalDate.now());

		return this.detail(phieuKnclh);
	}

	@Override
	public boolean updateStatus(StatusReq req) throws Exception {
		return false;
	}

	@Override
	public PhieuKnghiemCluongHangRes detail(Long id) throws Exception {
		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");
		return this.detail(optional.get());
	}

	private PhieuKnghiemCluongHangRes detail(PhieuKnghiemCluongHang phieuKnclh) {
		PhieuKnghiemCluongHangRes res = new PhieuKnghiemCluongHangRes();
		BeanUtils.copyProperties(phieuKnclh, res);
		QlnvDmVattu vattu = qlnvDmVattuRepository.findByMa(phieuKnclh.getMaHhoa());
		if (vattu != null)
			res.setTenHhoa(vattu.getTen());

		Page<KquaKnghiemRes> list = kquaKnghiemService.list(phieuKnclh.getId(), PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE));
		res.setTongSoKquaKnghiem(list.getTotalElements());
		res.setKquaKnghiem(list.getContent());
		res.setSluongKquaKnghiem((long) list.getNumberOfElements());
		return res;
	}

	@Override
	public boolean delete(Long id) {
		return false;
	}

	private List<PhieuKnghiemCluongHangRes> toResponseList(List<PhieuKnghiemCluongHang> list) {
		Set<String> maHhoaSet = new HashSet<>();
		for (PhieuKnghiemCluongHang phieuKnclh : list) {
			if (!StringUtils.isEmpty(phieuKnclh.getMaHhoa()))
				maHhoaSet.add(phieuKnclh.getMaHhoa());
		}

		Set<QlnvDmVattu> vattuSet = qlnvDmVattuRepository.findByMaIn(maHhoaSet);

		List<PhieuKnghiemCluongHangRes> resList = new ArrayList<>();
		for (PhieuKnghiemCluongHang phieuKnclh : list) {
			QlnvDmVattu vattu = vattuSet.stream().filter(vt -> vt.getMa().equals(phieuKnclh.getMaHhoa())).findFirst().orElse(null);
			PhieuKnghiemCluongHangRes res = this.toResponse(phieuKnclh, vattu);
			if (res != null)
				resList.add(res);
		}

		return resList;
	}

	private PhieuKnghiemCluongHangRes toResponse(PhieuKnghiemCluongHang phieuKnclh, QlnvDmVattu vattu) {
		if (phieuKnclh == null)
			return null;

		PhieuKnghiemCluongHangRes res = new PhieuKnghiemCluongHangRes();
		BeanUtils.copyProperties(phieuKnclh, res);
		res.setTrangThai(TrangThaiEnum.getTen(res.getTrangThai()));
		if (vattu != null)
			res.setTenHhoa(vattu.getTen());

		return res;
	}

	private void updateEntity(PhieuKnghiemCluongHang phieuKnclh, PhieuKnghiemCluongHangReq req) {
		phieuKnclh.setSoPhieu(req.getSoPhieu());
		phieuKnclh.setDdiemBquan(req.getDdiemBquan());
		phieuKnclh.setHthucBquan(req.getHthucBquan());
		phieuKnclh.setMaHhoa(req.getMaHhoa());
		phieuKnclh.setNgayKnghiem(req.getNgayKnghiem());
		phieuKnclh.setMaNgan(req.getMaNgan());
		phieuKnclh.setMaKho(req.getMaKho());
		phieuKnclh.setNgayLayMau(req.getNgayLayMau());
		phieuKnclh.setNgayNhapDay(req.getNgayNhapDay());
		phieuKnclh.setSoBbanKthucNhap(req.getSoBbanKthucNhap());
		phieuKnclh.setTenKho(req.getTenKho());
		phieuKnclh.setTenKho(req.getTenKho());
		phieuKnclh.setTenHhoa(req.getTenHhoa());
		phieuKnclh.setSluongBquan(req.getSluongBquan());
	}
}
