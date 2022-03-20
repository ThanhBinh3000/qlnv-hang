package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.entities.kehoachluachonnhathau.GoiThau;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khlcnt.GoiThauRepository;
import com.tcdt.qlnvhang.request.object.khlcnt.GoiThauReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.khlcnt.DiaDiemNhapRes;
import com.tcdt.qlnvhang.response.khlcnt.GoiThauRes;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoiThauServiceImpl implements GoiThauService {
	@Autowired
	private GoiThauRepository goiThauRepository;

	@Autowired
	private DiaDiemNhapService diaDiemNhapService;

	@Autowired
	private QlnvDmVattuRepository qlnvDmVattuRepository;

	@Override
	public ListResponse<GoiThauRes> list(Long khLcntId, Pageable pageable) {
		List<GoiThau> list = goiThauRepository.findByKhLcntId(khLcntId, pageable);
		ListResponse<GoiThauRes> response = new ListResponse<>();
		response.setList(this.toResponseList(list));
		response.setTotal(goiThauRepository.countByKhLcntId(khLcntId));
		return response;
	}

	@Override
	public GoiThauRes getDetail(Long id, Pageable pageable) throws Exception {
		Optional<GoiThau> optionalGoiThau = goiThauRepository.findById(id);
		if (!optionalGoiThau.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		GoiThau goiThau = optionalGoiThau.get();
		QlnvDmVattu vattu = qlnvDmVattuRepository.findById(goiThau.getVatTuId()).orElse(null);
		GoiThauRes res = this.toResponse(goiThau, vattu);

		ListResponse<DiaDiemNhapRes> diaDiemNhapResList = diaDiemNhapService.list(goiThau.getId(), pageable);

		res.setDiaDiemNhap(diaDiemNhapResList.getList());
		res.setTongSoDiaDiemNhap(diaDiemNhapResList.getTotal());
		res.setSoLuongDiaDiemNhap(diaDiemNhapResList.getCount());
		return res;
	}

	@Override
	public GoiThauRes getDetail(Long id) throws Exception {
		return this.getDetail(id, PageRequest.of(0, Integer.MAX_VALUE));
	}

	@Override
	public boolean deleteByKhLcntId(Long khLcntId) {
		List<GoiThau> goiThauList = goiThauRepository.findByKhLcntId(khLcntId);
		diaDiemNhapService.deleteByGoiThauIds(goiThauList.stream().map(GoiThau::getId).collect(Collectors.toSet()));
		goiThauRepository.deleteAll(goiThauList);
		return true;
	}

	@Override
	public List<GoiThauRes> update(Long khLcntId, List<GoiThauReq> reqList) {
		List<GoiThau> goiThauList = goiThauRepository.findByKhLcntId(khLcntId);
		Set<GoiThau> updatedGoiThauSet = new HashSet<>();
		for (GoiThauReq req : reqList) {
			GoiThau goiThau = goiThauList.stream().filter(g -> g.getId().equals(req.getId())).findFirst().orElse(null);
			if (goiThau == null)
				goiThau = new GoiThau();

			goiThau.setTenGoiThau(req.getTenGoiThau());
			goiThau.setVatTuId(req.getVatTuId());
			goiThau.setTenVatTu(req.getTenVatTu());
			goiThau.setMaVatTu(req.getMaVatTu());
			goiThau.setDonGia(req.getDonGia());
			goiThau.setSoLuong(req.getSoLuong());
			goiThau.setStt(req.getStt());
			diaDiemNhapService.update(goiThau.getId(), req.getDiaDiemNhap());
			updatedGoiThauSet.add(goiThau);
		}
		goiThauRepository.saveAll(updatedGoiThauSet);
		goiThauList.removeAll(updatedGoiThauSet);
		goiThauRepository.deleteAll(goiThauList);
		return this.toResponseList(updatedGoiThauSet.stream().sorted(Comparator.comparing(GoiThau::getStt)).collect(Collectors.toList()));
	}

	private List<GoiThauRes> toResponseList(List<GoiThau> list) {
		Set<String> maVatTuSet = new HashSet<>();
		for (GoiThau goiThau : list) {
			if (goiThau == null)
				continue;

			if (!StringUtils.isEmpty(goiThau.getMaVatTu()))
				maVatTuSet.add(goiThau.getMaVatTu());
		}
		Set<QlnvDmVattu> vattuList = qlnvDmVattuRepository.findByMaIn(maVatTuSet);
		List<GoiThauRes> goiThauResList = new ArrayList<>();
		for (GoiThau goiThau : list) {
			if (goiThau == null)
				continue;
			QlnvDmVattu vattu = vattuList.stream().filter(v -> v.getMa().equals(goiThau.getMaVatTu())).findFirst().orElse(null);
			GoiThauRes res = this.toResponse(goiThau, vattu);

			if (res != null)
				goiThauResList.add(res);
		}

		return goiThauResList;
	}

	private GoiThauRes toResponse(GoiThau goiThau, QlnvDmVattu vattu) {
		if (goiThau == null)
			return null;

		GoiThauRes res = new GoiThauRes();
		res.setId(goiThau.getId());
		res.setTenGoiThau(goiThau.getTenGoiThau());
		res.setSoLuong(goiThau.getSoLuong());
		res.setDonGia(goiThau.getDonGia());
		res.setStt(goiThau.getStt());
		if (vattu != null) {
			res.setTenVatTu(vattu.getTen());
			res.setMaVatTu(vattu.getMa());
			res.setVatTuId(vattu.getId());
		}

		return res;
	}
}
