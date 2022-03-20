package com.tcdt.qlnvhang.service.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.ThongTinDauThauVT;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.dauthauvattu.ThongTinDauThauVTRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.dauthauvattu.ThongTinDauThauVTReq;
import com.tcdt.qlnvhang.request.search.ThongTinDauThauVTSearchReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.dauthauvattu.DTVatTuGoiThauVTRes;
import com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ThongTinDauThauVTServiceImpl implements ThongTinDauThauVTService {
	@Autowired
	private ThongTinDauThauVTRepository thongTinDauThauVTRepository;

	@Autowired
	private DTVatTuGoiThauVTService dtVatTuGoiThauVTService;

	@Override
	public ThongTinDauThauRes create(ThongTinDauThauVTReq req) {
		ThongTinDauThauVT thongTinDauThauVT = new ThongTinDauThauVT();
		updateEntity(req, thongTinDauThauVT);

		thongTinDauThauVTRepository.save(thongTinDauThauVT);
		dtVatTuGoiThauVTService.update(req.getGoiThau(), thongTinDauThauVT.getId());

		return null;
	}

	@Override
	public ThongTinDauThauRes update(ThongTinDauThauVTReq req) throws Exception {
		Optional<ThongTinDauThauVT> thongTinDauThauVTOptional = thongTinDauThauVTRepository.findById(req.getId());
		if (!thongTinDauThauVTOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		ThongTinDauThauVT thongTinDauThauVT = thongTinDauThauVTOptional.get();

		updateEntity(req, thongTinDauThauVT);

		dtVatTuGoiThauVTService.update(req.getGoiThau(), thongTinDauThauVT.getId());

		return this.detail(thongTinDauThauVT, null, null);
	}

	private void updateEntity(ThongTinDauThauVTReq req, ThongTinDauThauVT thongTinDauThauVT) {
		thongTinDauThauVT.setSoDxuat(req.getSoDxuat());
		thongTinDauThauVT.setBenMoiThau(req.getBenMoiThau());
		thongTinDauThauVT.setHthucDuThau(req.getHthucDuThau());
		thongTinDauThauVT.setNgayDxuat(req.getNgayDxuat());
		thongTinDauThauVT.setNgayPduyetKhlcnt(req.getNgayPduyetKhlcnt());
		thongTinDauThauVT.setQdKhlcntId(req.getQdKhlcntId());
		thongTinDauThauVT.setSoQdinhPduyetKhlcnt(req.getSoQdinhPduyetKhlcnt());
		thongTinDauThauVT.setTenChuDtu(req.getTenChuDtu());
		thongTinDauThauVT.setTenDuAn(req.getTenDuAn());
		thongTinDauThauVT.setTgianDongThau(req.getTgianDongThau());
		thongTinDauThauVT.setTgianHlucEhsmt(req.getTgianHlucEhsmt());
		thongTinDauThauVT.setTongMucDtu(req.getTongMucDtu());
	}

	@Override
	public ThongTinDauThauRes detail(Long id, Integer pageIndex, Integer pageSize) throws Exception {
		Optional<ThongTinDauThauVT> thongTinDauThauVTOptional = thongTinDauThauVTRepository.findById(id);
		if (!thongTinDauThauVTOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		return this.detail(thongTinDauThauVTOptional.get(), pageIndex, pageSize);
	}

	private ThongTinDauThauRes detail(ThongTinDauThauVT thongTinDauThauVT, Integer pageIndex, Integer pageSize) {
		if (thongTinDauThauVT == null)
			return null;
		final int DEFAULT_PAGE_INDEX = 0;
		final int DEFAULT_PAGE_SIZE = 10;

		ThongTinDauThauRes res = this.toResponse(thongTinDauThauVT);

		if (pageIndex == null)
			pageIndex = DEFAULT_PAGE_INDEX;
		if (pageSize == null)
			pageSize = DEFAULT_PAGE_SIZE;

		ListResponse<DTVatTuGoiThauVTRes> goiThau = dtVatTuGoiThauVTService.list(thongTinDauThauVT.getId(), PageRequest.of(pageIndex, pageSize));
		res.setGoiThau(goiThau.getList());
		res.setSoGoiThau(goiThau.getCount());
		res.setTongGoiThau(goiThau.getTotal());

		return res;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		Optional<ThongTinDauThauVT> thongTinDauThauVTOptional = thongTinDauThauVTRepository.findById(id);
		if (!thongTinDauThauVTOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		dtVatTuGoiThauVTService.deleteByTtdtVtId(id);
		return true;
	}

	@Override
	public boolean updateStatus(StatusReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		ThongTinDauThauVT thongTinDauThauVT = thongTinDauThauVTRepository.findById(req.getId()).orElse(null);
		if (thongTinDauThauVT == null)
			throw new Exception("Không tìm thấy dữ liệu.");

		if (TrangThaiEnum.MOI_TAO.getMa().equals(thongTinDauThauVT.getTrangThai()) && TrangThaiEnum.CHO_DUYET.getMa().equals(req.getTrangThai())) {
			thongTinDauThauVT.setTrangThai(TrangThaiEnum.CHO_DUYET.getMa());
			thongTinDauThauVT.setNgayGuiDuyet(LocalDate.now());
			thongTinDauThauVT.setNguoiPduyetId(userInfo.getId());
		} else if (TrangThaiEnum.CHO_DUYET.getMa().equals(thongTinDauThauVT.getTrangThai()) && TrangThaiEnum.DA_DUYET.getMa().equals(req.getTrangThai())) {
			thongTinDauThauVT.setNgayPduyet(LocalDate.now());
			thongTinDauThauVT.setNguoiPduyetId(userInfo.getId());
			thongTinDauThauVT.setTrangThai(TrangThaiEnum.DA_DUYET.getMa());
		} else if (TrangThaiEnum.CHO_DUYET.getMa().equals(thongTinDauThauVT.getTrangThai()) && TrangThaiEnum.TU_CHOI.getMa().equals(req.getTrangThai())) {
			thongTinDauThauVT.setNgayPduyet(LocalDate.now());
			thongTinDauThauVT.setNguoiPduyetId(userInfo.getId());
			thongTinDauThauVT.setTrangThai(TrangThaiEnum.TU_CHOI.getMa());
			thongTinDauThauVT.setLdoTchoi(req.getLyDo());
		} else {
			return false;
		}

		return true;
	}

	@Override
	public ListResponse<ThongTinDauThauRes> search(ThongTinDauThauVTSearchReq req) {
		return null;
	}

	private List<ThongTinDauThauRes> toResponseList(List<ThongTinDauThauVT> list) {
		List<ThongTinDauThauRes> resList = new ArrayList<>();
		for (ThongTinDauThauVT thongTinDauThauVT : list) {
			if (thongTinDauThauVT == null)
				continue;
			resList.add(this.toResponse(thongTinDauThauVT));
		}

		return resList;
	}

	private ThongTinDauThauRes toResponse(ThongTinDauThauVT thongTinDauThauVT) {
		ThongTinDauThauRes res = new ThongTinDauThauRes();
		res.setId(thongTinDauThauVT.getId());
		res.setSoDxuat(thongTinDauThauVT.getSoDxuat());
		res.setBenMoiThau(thongTinDauThauVT.getBenMoiThau());
		res.setHthucDuThau(thongTinDauThauVT.getHthucDuThau());
		res.setNgayDxuat(thongTinDauThauVT.getNgayDxuat());
		res.setNgayPduyetKhlcnt(thongTinDauThauVT.getNgayPduyetKhlcnt());
		res.setQdKhlcntId(thongTinDauThauVT.getQdKhlcntId());
		res.setSoQdinhPduyetKhlcnt(thongTinDauThauVT.getSoQdinhPduyetKhlcnt());
		res.setTenChuDtu(thongTinDauThauVT.getTenChuDtu());
		res.setTenDuAn(thongTinDauThauVT.getTenDuAn());
		res.setTgianDongThau(thongTinDauThauVT.getTgianDongThau());
		res.setTgianHlucEhsmt(thongTinDauThauVT.getTgianHlucEhsmt());
		res.setTongMucDtu(thongTinDauThauVT.getTongMucDtu());

		return res;
	}
}
