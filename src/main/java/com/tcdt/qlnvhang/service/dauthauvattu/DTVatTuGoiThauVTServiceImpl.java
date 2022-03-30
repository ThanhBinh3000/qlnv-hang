package com.tcdt.qlnvhang.service.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.DTVatTuGoiThauVT;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.dauthauvattu.DTVatTuGoiThauVTRepository;
import com.tcdt.qlnvhang.request.object.dauthauvattu.DTVatTuGoiThauVTReq;
import com.tcdt.qlnvhang.response.dauthauvattu.DTGoiThauDiaDiemNhapVTRes;
import com.tcdt.qlnvhang.response.dauthauvattu.DTVatTuGoiThauVTRes;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DTVatTuGoiThauVTServiceImpl implements DTVatTuGoiThauVTService {
	@Autowired
	private DTVatTuGoiThauVTRepository dtVatTuGoiThauVTRepository;

	@Autowired
	private DTGoiThauDiaDiemNhapVTService dtGoiThauDiaDiemNhapVTService;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Override
	public Page<DTVatTuGoiThauVTRes> list(Long ttdtVtId, Pageable pageable) {
		Page<DTVatTuGoiThauVT> list = dtVatTuGoiThauVTRepository.findByTtdtVtId(ttdtVtId, pageable);
		return new PageImpl<>(this.toResponseList(list.getContent()), pageable, list.getTotalElements());
	}

	@Override
	public List<DTVatTuGoiThauVTRes> update(List<DTVatTuGoiThauVTReq> reqList, Long ttdtVtId) {
		Set<Long> requestIds = reqList.stream().map(DTVatTuGoiThauVTReq::getId).collect(Collectors.toSet());
		List<DTVatTuGoiThauVT> list = dtVatTuGoiThauVTRepository.findByTtdtVtId(ttdtVtId);

		// Get all deleted GoiThau, then delete all DiaDiemNhap of them.
		Set<Long> deleteIds = list.stream().map(DTVatTuGoiThauVT::getId).filter(id -> !requestIds.contains(id)).collect(Collectors.toSet());
		dtGoiThauDiaDiemNhapVTService.deleteByGoiThauIds(deleteIds);

		List<DTVatTuGoiThauVT> saveList = new ArrayList<>();
		for (DTVatTuGoiThauVTReq req : reqList) {
			if (req == null)
				continue;

			DTVatTuGoiThauVT goiThauVT = new DTVatTuGoiThauVT();
			goiThauVT.setId(req.getId());
			goiThauVT.setTtdtVtId(req.getTtdtVtId());
			goiThauVT.setStt(req.getStt());
			goiThauVT.setTenGoiThau(req.getTenGoiThau());
			goiThauVT.setMaHhoa(req.getMaHhoa());
			goiThauVT.setDonGia(req.getDonGia());
			goiThauVT.setSoLuong(req.getSoLuong());
			goiThauVT.setHthucHdong(req.getHthucHdong());
			goiThauVT.setPthucLcnt(req.getPthucLcnt());
			goiThauVT.setTgianThienHdong(req.getTgianThienHdong());
			goiThauVT.setGhiChu(req.getGhiChu());

			dtGoiThauDiaDiemNhapVTService.update(req.getDiaDiemNhap(), goiThauVT.getId());
		}
		dtVatTuGoiThauVTRepository.saveAll(saveList);

		return this.toResponseList(saveList);
	}

	@Override
	public boolean deleteByTtdtVtId(Long ttdtVtId) {
		List<DTVatTuGoiThauVT> list = dtVatTuGoiThauVTRepository.findByTtdtVtId(ttdtVtId);
		Set<Long> ids = list.stream().map(DTVatTuGoiThauVT::getId).collect(Collectors.toSet());
		dtGoiThauDiaDiemNhapVTService.deleteByGoiThauIds(ids);
		dtVatTuGoiThauVTRepository.deleteAll(list);
		return true;
	}

	@Override
	public DTVatTuGoiThauVTRes detail(Long goiThauId, Integer pageSize, Integer pageIndex) throws Exception {
		Optional<DTVatTuGoiThauVT> goiThauVTOptional = dtVatTuGoiThauVTRepository.findById(goiThauId);
		if (!goiThauVTOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		DTVatTuGoiThauVT goiThauVT = goiThauVTOptional.get();
		QlnvDmVattu vattu = qlnvDmVattuRepository.findByMa(goiThauVT.getMaHhoa());
		DTVatTuGoiThauVTRes res = new DTVatTuGoiThauVTRes();
		res.setId(goiThauVT.getId());
		res.setTtdtVtId(goiThauVT.getTtdtVtId());
		res.setStt(goiThauVT.getStt());
		res.setTenGoiThau(goiThauVT.getTenGoiThau());
		res.setGhiChu(goiThauVT.getGhiChu());
		res.setTgianThienHdong(goiThauVT.getTgianThienHdong());
		res.setSoLuong(goiThauVT.getSoLuong());
		res.setDonGia(goiThauVT.getDonGia());
		res.setPthucLcnt(goiThauVT.getPthucLcnt());
		res.setHthucHdong(goiThauVT.getHthucHdong());
		if (vattu != null) {
			res.setMaHhoa(vattu.getMa());
			res.setTenHhoa(vattu.getTen());
		}


		final int DEFAULT_PAGE_SIZE = 10;
		final int DEFAULT_PAGE_INDEX = 0;

		if (pageSize == null) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		if (pageIndex == null)
			pageIndex = DEFAULT_PAGE_INDEX;

		Page<DTGoiThauDiaDiemNhapVTRes> diaDiemNhapVTResList = dtGoiThauDiaDiemNhapVTService.findByGoiThauId(goiThauVT.getId(), PageRequest.of(pageIndex, pageSize));
		res.setDiaDiemNhap(diaDiemNhapVTResList.getContent());
		res.setSoLuongDiaDiemNhap((long) diaDiemNhapVTResList.getNumberOfElements());
		res.setTongDiaDiemNhap(diaDiemNhapVTResList.getTotalElements());

		return res;
	}

	private List<DTVatTuGoiThauVTRes> toResponseList(List<DTVatTuGoiThauVT> list) {
		List<DTVatTuGoiThauVTRes> resList = new ArrayList<>();
		Set<String> maHhoaSet = new HashSet<>();

		for (DTVatTuGoiThauVT goiThauVT : list) {
			if (!StringUtils.isEmpty(goiThauVT.getMaHhoa()))
				maHhoaSet.add(goiThauVT.getMaHhoa());
		}

		Set<QlnvDmVattu> vattuList = qlnvDmVattuRepository.findByMaIn(maHhoaSet);
		for (DTVatTuGoiThauVT goiThauVT : list) {
			if (goiThauVT == null)
				continue;

			QlnvDmVattu vattu = vattuList.stream().filter(vt -> vt.getMa().equals(goiThauVT.getMaHhoa())).findFirst().orElse(null);
			resList.add(this.toResponse(goiThauVT, vattu));
		}

		return resList;
	}

	private DTVatTuGoiThauVTRes toResponse(DTVatTuGoiThauVT goiThauVT, QlnvDmVattu vattu) {
		DTVatTuGoiThauVTRes res = new DTVatTuGoiThauVTRes();
		res.setId(goiThauVT.getId());
		res.setTtdtVtId(goiThauVT.getTtdtVtId());
		res.setTenGoiThau(goiThauVT.getTenGoiThau());
		res.setHthucHdong(goiThauVT.getHthucHdong());
		res.setDonGia(goiThauVT.getDonGia());
		res.setPthucLcnt(goiThauVT.getPthucLcnt());
		res.setStt(goiThauVT.getStt());
		res.setSoLuong(goiThauVT.getSoLuong());
		res.setGhiChu(goiThauVT.getGhiChu());
		res.setTgianThienHdong(goiThauVT.getTgianThienHdong());
		if (vattu != null) {
			res.setMaHhoa(vattu.getMa());
			res.setTenHhoa(vattu.getTen());
		}

		return res;
	}
}
