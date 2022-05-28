package com.tcdt.qlnvhang.service.kehoachluachonnhathau;

import com.tcdt.qlnvhang.entities.kehoachluachonnhathau.KhLuaChonNhaThau;
import com.tcdt.qlnvhang.enums.HhBbNghiemthuKlstStatusEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khlcnt.KhLuaChonNhaThauRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.kehoachluachonnhathau.GoiThauRes;
import com.tcdt.qlnvhang.response.kehoachluachonnhathau.KhLuaChonNhaThauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.donvi.QlnvDmDonViService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.Contains;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class KhLuaChonNhaThauServiceImpl implements KhLuaChonNhaThauService{
	@Autowired
	private KhLuaChonNhaThauRepository khLuaChonNhaThauRepository;

	@Autowired
	private GoiThauService goiThauService;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Autowired
	private QlnvDmDonViService qlnvDmDonViService;

	private final int DEFAULT_PAGE_SIZE = 10;

	@Override
	public KhLuaChonNhaThauRes create(KhLuaChonNhaThauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		KhLuaChonNhaThau khLuaChonNhaThau = new KhLuaChonNhaThau();
		this.updateEntity(req, khLuaChonNhaThau);
		khLuaChonNhaThau.setNgayTao(LocalDate.now());
		khLuaChonNhaThau.setNguoiTaoId(userInfo.getId());
		khLuaChonNhaThau.setTrangThai(Contains.TAO_MOI);
		khLuaChonNhaThau.setMaDonVi(userInfo.getDvql());
		khLuaChonNhaThau.setCapDonVi(qlnvDmDonViService.getCapDviByMa(userInfo.getDvql()));
		khLuaChonNhaThauRepository.save(khLuaChonNhaThau);
		goiThauService.update(khLuaChonNhaThau.getId(), req.getGoiThau());

		return this.getDetail(khLuaChonNhaThau.getId(), PageRequest.of(0, DEFAULT_PAGE_SIZE));
	}

	@Override
	public KhLuaChonNhaThauRes update(KhLuaChonNhaThauReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		KhLuaChonNhaThau khLuaChonNhaThau = khLuaChonNhaThauRepository.findById(req.getId()).orElse(null);
		if (khLuaChonNhaThau == null)
			throw new Exception("Không tìm thấy dữ liệu.");

		this.updateEntity(req, khLuaChonNhaThau);
		goiThauService.update(khLuaChonNhaThau.getId(), req.getGoiThau());
		return this.getDetail(khLuaChonNhaThau.getId(), PageRequest.of(0, DEFAULT_PAGE_SIZE));
	}

	private void updateEntity(KhLuaChonNhaThauReq req, KhLuaChonNhaThau khLuaChonNhaThau) {
		if (req == null)
			return;

		khLuaChonNhaThau.setSoQdinh(req.getSoQdinh());
		khLuaChonNhaThau.setNamKhoach(req.getNamKhoach());
		khLuaChonNhaThau.setMaVatTu(req.getMaVatTu());
		khLuaChonNhaThau.setTenVatTu(req.getTenVatTu());
		khLuaChonNhaThau.setTenChuDtu(req.getTenChuDtu());
		khLuaChonNhaThau.setTenDuAn(req.getTenDuAn());
		khLuaChonNhaThau.setDonViTien(req.getDonViTien());
		khLuaChonNhaThau.setDienGiai(req.getDienGiai());
		khLuaChonNhaThau.setHthucLcnt(req.getHthucLcnt());
		khLuaChonNhaThau.setNguonVon(req.getNguonVon());
		khLuaChonNhaThau.setQchuanKthuat(req.getQchuanKthuat());
		khLuaChonNhaThau.setTgianThienDuAn(req.getTgianThienDuAn());
		khLuaChonNhaThau.setTongMucDtu(req.getTongMucDtu());
	}

	@Override
	public KhLuaChonNhaThauRes getDetail(Long id) throws Exception {
		return this.getDetail(id, PageRequest.of(0, Integer.MAX_VALUE));
	}

	@Override
	public KhLuaChonNhaThauRes getDetail(Long id, Pageable pageable) throws Exception {
		KhLuaChonNhaThau khLuaChonNhaThau = khLuaChonNhaThauRepository.findById(id).orElse(null);
		if (khLuaChonNhaThau == null)
			throw new Exception("Không tìm thấy dữ liệu.");
		QlnvDmVattu vattu = qlnvDmVattuRepository.findByMa(khLuaChonNhaThau.getMaVatTu());
		KhLuaChonNhaThauRes res = this.toResponse(khLuaChonNhaThau, vattu);
		if (res == null)
			return null;

		ListResponse<GoiThauRes> goiThauResList = goiThauService.list(khLuaChonNhaThau.getId(), pageable);
		res.setGoiThau(goiThauResList.getList());
		res.setSoGoiThau(goiThauResList.getCount());
		res.setTongSoGoiThau(goiThauResList.getTotal());

		return res;
	}

	@Override
	public ListResponse<KhLuaChonNhaThauRes> search(KhLuaChonNhaThauSearchReq req) {
		Page<KhLuaChonNhaThau> list = khLuaChonNhaThauRepository.search(req);
		ListResponse<KhLuaChonNhaThauRes> response = new ListResponse<>();
		response.setList(this.toResponseList(list.getContent()));
		response.setTotal(list.getTotalElements());
		return response;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		KhLuaChonNhaThau khLuaChonNhaThau = khLuaChonNhaThauRepository.findById(id).orElse(null);
		if (khLuaChonNhaThau == null)
			throw new Exception("Không tìm thấy dữ liệu.");

		goiThauService.deleteByKhLcntId(khLuaChonNhaThau.getId());
		khLuaChonNhaThauRepository.delete(khLuaChonNhaThau);
		return true;
	}

	@Override
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		KhLuaChonNhaThau khLuaChonNhaThau = khLuaChonNhaThauRepository.findById(stReq.getId()).orElse(null);
		if (khLuaChonNhaThau == null)
			throw new Exception("Không tìm thấy dữ liệu.");

		String trangThai = khLuaChonNhaThau.getTrangThai();
		if (HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO.getId().equals(trangThai))
				return false;

			khLuaChonNhaThau.setTrangThai(HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId());
			khLuaChonNhaThau.setNguoiGuiDuyetId(userInfo.getId());
			khLuaChonNhaThau.setNgayGuiDuyet(LocalDate.now());
		} else if (HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;
			khLuaChonNhaThau.setTrangThai(HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId());
			khLuaChonNhaThau.setNguoiPduyetId(userInfo.getId());
			khLuaChonNhaThau.setNgayPduyet(LocalDate.now());
		} else if (HhBbNghiemthuKlstStatusEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.LANH_DAO_DUYET.getId().equals(trangThai))
				return false;

			khLuaChonNhaThau.setTrangThai(HhBbNghiemthuKlstStatusEnum.BAN_HANH.getId());
			khLuaChonNhaThau.setNguoiPduyetId(userInfo.getId());
			khLuaChonNhaThau.setNgayPduyet(LocalDate.now());
		} else if (HhBbNghiemthuKlstStatusEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
			if (!HhBbNghiemthuKlstStatusEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
				return false;

			khLuaChonNhaThau.setTrangThai(HhBbNghiemthuKlstStatusEnum.TU_CHOI.getId());
			khLuaChonNhaThau.setNguoiPduyetId(userInfo.getId());
			khLuaChonNhaThau.setNgayPduyet(LocalDate.now());
			khLuaChonNhaThau.setLdoTchoi(stReq.getLyDo());
		}  else {
			throw new Exception("Bad request.");
		}
		khLuaChonNhaThauRepository.save(khLuaChonNhaThau);

		return true;
	}

	private List<KhLuaChonNhaThauRes> toResponseList(List<KhLuaChonNhaThau> list) {
		Set<String> maVatTuSet = new HashSet<>();
		for (KhLuaChonNhaThau khLuaChonNhaThau : list) {
			if (!StringUtils.isEmpty(khLuaChonNhaThau.getMaVatTu()))
				maVatTuSet.add(khLuaChonNhaThau.getMaVatTu());
		}

		Set<QlnvDmVattu> vattuList = qlnvDmVattuRepository.findByMaIn(maVatTuSet);

		List<KhLuaChonNhaThauRes> resList = new ArrayList<>();
		for (KhLuaChonNhaThau khLuaChonNhaThau : list) {
			KhLuaChonNhaThauRes res = this.toResponse(khLuaChonNhaThau, vattuList.stream().filter(v -> v.getMa().equals(khLuaChonNhaThau.getMaVatTu())).findFirst().orElse(null));
			if (res != null)
				resList.add(res);
		}

		return resList;
	}

	private KhLuaChonNhaThauRes toResponse(KhLuaChonNhaThau khLuaChonNhaThau, QlnvDmVattu vatTu) {
		if (khLuaChonNhaThau == null)
			return null;

		KhLuaChonNhaThauRes res = new KhLuaChonNhaThauRes();
		res.setId(khLuaChonNhaThau.getId());
		res.setNamKhoach(khLuaChonNhaThau.getNamKhoach());
		res.setDienGiai(khLuaChonNhaThau.getDienGiai());
		res.setNguonVon(khLuaChonNhaThau.getNguonVon());
		res.setQchuanKthuat(khLuaChonNhaThau.getQchuanKthuat());
		res.setHthucLcnt(khLuaChonNhaThau.getHthucLcnt());
		res.setTenDuAn(khLuaChonNhaThau.getTenDuAn());
		res.setTongMucDtu(khLuaChonNhaThau.getTongMucDtu());
		res.setDonViTien(khLuaChonNhaThau.getDonViTien());
		res.setSoQdinh(khLuaChonNhaThau.getSoQdinh());
		res.setTenChuDtu(khLuaChonNhaThau.getTenChuDtu());
		if (vatTu != null) {
			res.setMaVatTu(vatTu.getMa());
			res.setTenVatTu(vatTu.getTen());
			res.setVatTuId(vatTu.getId());
		}
		return res;
	}
}
