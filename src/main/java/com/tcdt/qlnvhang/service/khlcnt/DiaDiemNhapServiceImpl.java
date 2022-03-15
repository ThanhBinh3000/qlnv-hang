package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.entities.khlcnt.DiaDiemNhap;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.khlcnt.DiaDiemNhapRepository;
import com.tcdt.qlnvhang.request.object.khlcnt.DiaDiemNhapReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.khlcnt.DiaDiemNhapRes;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiaDiemNhapServiceImpl implements DiaDiemNhapService {
	@Autowired
	private DiaDiemNhapRepository diaDiemNhapRepository;

	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;

	@Override
	public ListResponse<DiaDiemNhapRes> list(Long goiThauId, Pageable pageable) {
		List<DiaDiemNhap> list = diaDiemNhapRepository.findByGoiThauId(goiThauId, pageable);
		ListResponse<DiaDiemNhapRes> res = new ListResponse<>();
		res.setList(this.toResponseList(list));
		res.setTotal(diaDiemNhapRepository.countByGoiThauId(goiThauId));
		return res;
	}

	@Override
	public List<DiaDiemNhapRes> update(Long goiThauId, List<DiaDiemNhapReq> reqList) {
		List<DiaDiemNhap> diaDiemNhapList = diaDiemNhapRepository.findByGoiThauId(goiThauId);
		Set<DiaDiemNhap> updatedDiaDiemNhapSet = new HashSet<>();
		for (DiaDiemNhapReq diaDiemNhapReq : reqList) {
			if (diaDiemNhapReq == null)
				continue;

			DiaDiemNhap diaDiemNhap = diaDiemNhapList.stream().filter(d -> d.getId().equals(diaDiemNhapReq.getId())).findFirst().orElse(null);
			if (diaDiemNhap == null)
				diaDiemNhap = new DiaDiemNhap();

			diaDiemNhap.setStt(diaDiemNhapReq.getStt());
			diaDiemNhap.setMaDonVi(diaDiemNhapReq.getMaDonVi());
			diaDiemNhap.setGoiThauId(diaDiemNhapReq.getGoiThauId());
			diaDiemNhap.setSoLuongNhap(diaDiemNhapReq.getSoLuongNhap());
			updatedDiaDiemNhapSet.add(diaDiemNhap);
		}
		diaDiemNhapRepository.saveAll(updatedDiaDiemNhapSet);
		diaDiemNhapList.removeAll(updatedDiaDiemNhapSet);
		diaDiemNhapRepository.deleteAll(diaDiemNhapList);

		return this.toResponseList(updatedDiaDiemNhapSet.stream().sorted(Comparator.comparing(DiaDiemNhap::getStt)).collect(Collectors.toList()));
	}

	@Override
	public boolean deleteByGoiThauIds(Set<Long> goiThauId) {
		List<DiaDiemNhap> diaDiemNhapList = diaDiemNhapRepository.findByGoiThauIdIn(goiThauId);
		diaDiemNhapRepository.deleteAll(diaDiemNhapList);
		return true;
	}

	public List<DiaDiemNhapRes> toResponseList(List<DiaDiemNhap> list) {
		Set<String> maDviSet = new HashSet<>();
		for (DiaDiemNhap diaDiemNhap : list) {
			if (!StringUtils.isEmpty(diaDiemNhap.getMaDonVi()))
				maDviSet.add(diaDiemNhap.getMaDonVi());
		}

		List<QlnvDmDonvi> donviList = qlnvDmDonviRepository.findByMaDviIn(maDviSet);

		List<DiaDiemNhapRes> diaDiemNhapResList = new ArrayList<>();
		for (DiaDiemNhap diaDiemNhap : list) {
			if (diaDiemNhap == null)
				continue;
			QlnvDmDonvi donVi = donviList.stream().filter(dv -> dv.getMaDvi().equals(diaDiemNhap.getMaDonVi())).findFirst().orElse(null);

			DiaDiemNhapRes res = this.toResponse(diaDiemNhap, donVi);
			if (res != null)
				diaDiemNhapResList.add(res);
		}

		return diaDiemNhapResList;
	}

	public DiaDiemNhapRes toResponse(DiaDiemNhap diaDiemNhap, QlnvDmDonvi donVi) {
		if (diaDiemNhap == null)
			return null;

		DiaDiemNhapRes res = new DiaDiemNhapRes();
		res.setId(diaDiemNhap.getId());
		res.setGoiThauId(diaDiemNhap.getGoiThauId());
		res.setSoLuongNhap(diaDiemNhap.getSoLuongNhap());
		res.setStt(diaDiemNhap.getStt());
		if (donVi != null) {
			res.setMaDonVi(diaDiemNhap.getMaDonVi());
			res.setTenDonVi(donVi.getTenDvi());
		}

		return res;
	}
}
