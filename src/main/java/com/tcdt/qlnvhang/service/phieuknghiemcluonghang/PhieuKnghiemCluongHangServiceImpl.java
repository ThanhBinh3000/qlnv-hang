package com.tcdt.qlnvhang.service.phieuknghiemcluonghang;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.phieuknghiemcluonghang.PhieuKnghiemCluongHangRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.KquaKnghiemRes;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.PhieuKnghiemCluongHangRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@Service
public class PhieuKnghiemCluongHangServiceImpl implements PhieuKnghiemCluongHangService {
	@Autowired
	private PhieuKnghiemCluongHangRepository phieuKnghiemCluongHangRepository;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private KquaKnghiemService kquaKnghiemService;

	@Autowired
	private KtNganLoRepository ktNganLoRepository;

	@Autowired
	private KtDiemKhoRepository ktDiemKhoRepository;

	@Autowired
	private KtNhaKhoRepository ktNhaKhoRepository;

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_INDEX = 0;

	@Override
	public Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),  req.getPaggingReq().getLimit());
		List<PhieuKnghiemCluongHang> list = phieuKnghiemCluongHangRepository.search(req, pageable);
		return new PageImpl<>(this.toResponseList(list), pageable, phieuKnghiemCluongHangRepository.countCtkhn(req));
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
		phieuKnclh.setMaDonVi(userInfo.getDvql());
		phieuKnclh.setCapDonVi(userInfo.getCapDvi());
		phieuKnclh.setTrangThai(TrangThaiEnum.DU_THAO.getMa());
		phieuKnghiemCluongHangRepository.save(phieuKnclh);
		kquaKnghiemService.update(phieuKnclh.getId(), req.getKquaKnghiem());

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

		phieuKnghiemCluongHangRepository.save(phieuKnclh);
		kquaKnghiemService.update(phieuKnclh.getId(), req.getKquaKnghiem());

		return this.detail(phieuKnclh);
	}

	@Override
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(stReq.getId());
		if (!optional.isPresent()) {
			throw new Exception("Không tìm thấy dữ liệu.");
		}

		PhieuKnghiemCluongHang phieu = optional.get();
		String trangThai = phieu.getTrangThai();
		if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO.getMa().equals(trangThai))
				return false;

			phieu.setTrangThai(TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa());
			phieu.setNguoiGuiDuyetId(userInfo.getId());
			phieu.setNgayGuiDuyet(LocalDate.now());
		} else if (TrangThaiEnum.LANH_DAO_DUYET.getMa().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa().equals(trangThai))
				return false;
			phieu.setTrangThai(TrangThaiEnum.LANH_DAO_DUYET.getMa());
			phieu.setNguoiPduyetId(userInfo.getId());
			phieu.setNgayPduyet(LocalDate.now());
		} else if (TrangThaiEnum.BAN_HANH.getMa().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.LANH_DAO_DUYET.getMa().equals(trangThai))
				return false;

			phieu.setTrangThai(TrangThaiEnum.BAN_HANH.getMa());
			phieu.setNguoiPduyetId(userInfo.getId());
			phieu.setNgayPduyet(LocalDate.now());
		} else if (TrangThaiEnum.TU_CHOI.getMa().equals(stReq.getTrangThai())) {
			if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa().equals(trangThai))
				return false;

			phieu.setTrangThai(TrangThaiEnum.TU_CHOI.getMa());
			phieu.setNguoiPduyetId(userInfo.getId());
			phieu.setNgayPduyet(LocalDate.now());
			phieu.setLdoTchoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}

		phieuKnghiemCluongHangRepository.save(phieu);
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

		res.setTenTrangThai(TrangThaiEnum.getTen(res.getTrangThai()));
		res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(res.getTrangThai()));

		Page<KquaKnghiemRes> list = kquaKnghiemService.list(phieuKnclh.getId(), PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE));
		res.setTongSoKquaKnghiem(list.getTotalElements());
		res.setKquaKnghiem(list.getContent());
		res.setSluongKquaKnghiem((long) list.getNumberOfElements());

		List<KtNganLo> byMaNganloIn = StringUtils.hasText(phieuKnclh.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(phieuKnclh.getMaNganLo())) : new ArrayList<>();
		List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(phieuKnclh.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(phieuKnclh.getMaDiemKho())) : new ArrayList<>();
		List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(phieuKnclh.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(phieuKnclh.getMaNhaKho())) : new ArrayList<>();

		byMaNganloIn.stream().filter(l -> l.getMaNganlo().equals(phieuKnclh.getMaNganLo())).findFirst().ifPresent(l -> {
			res.setTenNganLo(l.getTenNganlo());
			res.setNganLoId(l.getId());
		});

		byMaDiemkhoIn.stream().filter(l -> l.getMaDiemkho().equals(phieuKnclh.getMaDiemKho())).findFirst().ifPresent(l -> {
			res.setTenDiemKho(l.getTenDiemkho());
			res.setDiemKhoId(l.getId());
		});

		byMaNhakhoIn.stream().filter(l -> l.getMaNhakho().equals(phieuKnclh.getMaNhaKho())).findFirst().ifPresent(l -> {
			res.setTenNhaKho(l.getTenNhakho());
			res.setNhaKhoId(l.getId());
		});
		return res;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<PhieuKnghiemCluongHang> optional = phieuKnghiemCluongHangRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		PhieuKnghiemCluongHang phieu = optional.get();

		if (TrangThaiEnum.BAN_HANH.getMa().equals(phieu.getTrangThai())) {
			throw new Exception("Không thể xóa đề xuất điều chỉnh đã ban hành");
		}
		kquaKnghiemService.deleteByPhieuKnghiemId(phieu.getId());
		phieuKnghiemCluongHangRepository.delete(phieu);
		return true;
	}

	private List<PhieuKnghiemCluongHangRes> toResponseList(Collection<PhieuKnghiemCluongHang> data) {
		Set<String> maHhoaSet = new HashSet<>();
		Set<String> maNganLos =  new HashSet<>();
		Set<String> maDiemKhos = new HashSet<>();
		Set<String> maNhaKhos = new HashSet<>();
		for (PhieuKnghiemCluongHang phieuKnclh : data) {
			if (!StringUtils.isEmpty(phieuKnclh.getMaHhoa()))
				maHhoaSet.add(phieuKnclh.getMaHhoa());

			if (StringUtils.hasText(phieuKnclh.getMaNganLo()))
				maNganLos.add(phieuKnclh.getMaNganLo());

			if (StringUtils.hasText(phieuKnclh.getMaDiemKho()))
				maDiemKhos.add(phieuKnclh.getMaDiemKho());

			if (StringUtils.hasText(phieuKnclh.getMaNhaKho()))
				maNhaKhos.add(phieuKnclh.getMaNhaKho());
		}

		List<KtNganLo> byMaNganloIn = !CollectionUtils.isEmpty(maNganLos) ? ktNganLoRepository.findByMaNganloIn(maNganLos) : new ArrayList<>();
		List<KtDiemKho> byMaDiemkhoIn = !CollectionUtils.isEmpty(maDiemKhos) ? ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhos) : new ArrayList<>();
		List<KtNhaKho> byMaNhakhoIn = !CollectionUtils.isEmpty(maNhaKhos) ?  ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhos) : new ArrayList<>();
		Set<QlnvDmVattu> vattuSet = qlnvDmVattuRepository.findByMaIn(maHhoaSet);

		List<PhieuKnghiemCluongHangRes> resList = new ArrayList<>();
		for (PhieuKnghiemCluongHang phieuKnclh : data) {
			QlnvDmVattu vattu = vattuSet.stream().filter(vt -> vt.getMa().equals(phieuKnclh.getMaHhoa())).findFirst().orElse(null);
			PhieuKnghiemCluongHangRes res = this.toResponse(phieuKnclh, vattu,byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn);
			if (res != null)
				resList.add(res);
		}

		return resList;
	}

	private PhieuKnghiemCluongHangRes toResponse(PhieuKnghiemCluongHang phieu,
												 QlnvDmVattu vattu,
												 Collection<KtNganLo> nganLos,
												 Collection<KtDiemKho> diemKhos,
												 Collection<KtNhaKho> nhaKhos) {
		if (phieu == null)
			return null;

		PhieuKnghiemCluongHangRes res = new PhieuKnghiemCluongHangRes();
		BeanUtils.copyProperties(phieu, res);
		res.setTenTrangThai(TrangThaiEnum.getTen(res.getTrangThai()));
		res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(res.getTrangThai()));
		if (vattu != null)
			res.setTenHhoa(vattu.getTen());

		nganLos.stream().filter(l -> l.getMaNganlo().equals(phieu.getMaNganLo())).findFirst().ifPresent(l -> {
			res.setTenNganLo(l.getTenNganlo());
			res.setNganLoId(l.getId());
		});

		diemKhos.stream().filter(l -> l.getMaDiemkho().equals(phieu.getMaDiemKho())).findFirst().ifPresent(l -> {
			res.setTenDiemKho(l.getTenDiemkho());
			res.setDiemKhoId(l.getId());
		});

		nhaKhos.stream().filter(l -> l.getMaNhakho().equals(phieu.getMaNhaKho())).findFirst().ifPresent(l -> {
			res.setTenNhaKho(l.getTenNhakho());
			res.setNhaKhoId(l.getId());
		});
		return res;
	}

	private void updateEntity(PhieuKnghiemCluongHang phieuKnclh, PhieuKnghiemCluongHangReq req) {
		BeanUtils.copyProperties(req, phieuKnclh, "id");
	}
}
