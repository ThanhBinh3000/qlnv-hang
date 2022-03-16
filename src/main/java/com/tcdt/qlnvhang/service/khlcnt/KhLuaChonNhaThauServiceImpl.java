package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.entities.khlcnt.KhLuaChonNhaThau;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khlcnt.KhLuaChonNhaThauRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.khlcnt.GoiThauRes;
import com.tcdt.qlnvhang.response.khlcnt.KhLuaChonNhaThauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
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
	public boolean updateStatus(StatusReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		KhLuaChonNhaThau khLuaChonNhaThau = khLuaChonNhaThauRepository.findById(req.getId()).orElse(null);
		if (khLuaChonNhaThau == null)
			throw new Exception("Không tìm thấy dữ liệu.");

		if (Contains.TAO_MOI.equals(khLuaChonNhaThau.getTrangThai()) && Contains.CHO_DUYET.equals(req.getTrangThai())) {
			khLuaChonNhaThau.setTrangThai(Contains.CHO_DUYET);
			khLuaChonNhaThau.setNgayGuiDuyet(LocalDate.now());
			khLuaChonNhaThau.setNguoiPduyetId(userInfo.getId());
		} else if (Contains.CHO_DUYET.equals(khLuaChonNhaThau.getTrangThai()) && Contains.DUYET.equals(req.getTrangThai())) {
			khLuaChonNhaThau.setNgayPduyet(LocalDate.now());
			khLuaChonNhaThau.setNguoiPduyetId(userInfo.getId());
			khLuaChonNhaThau.setTrangThai(Contains.DUYET);
		} else if (Contains.CHO_DUYET.equals(khLuaChonNhaThau.getTrangThai()) && Contains.TU_CHOI.equals(req.getTrangThai())) {
			khLuaChonNhaThau.setNgayPduyet(LocalDate.now());
			khLuaChonNhaThau.setNguoiPduyetId(userInfo.getId());
			khLuaChonNhaThau.setTrangThai(Contains.TU_CHOI);
			khLuaChonNhaThau.setLdoTchoi(req.getLyDo());
		} else {
			return false;
		}

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
