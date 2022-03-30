package com.tcdt.qlnvhang.service.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.DTGoiThauDiaDiemNhapVT;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dauthauvattu.DTGoiThauDiaDiemNhapVTRepository;
import com.tcdt.qlnvhang.request.object.dauthauvattu.DTGoiThauDiaDiemNhapVTReq;
import com.tcdt.qlnvhang.response.dauthauvattu.DTGoiThauDiaDiemNhapVTRes;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DTGoiThauDiaDiemNhapVTServiceImpl implements DTGoiThauDiaDiemNhapVTService {
	@Autowired
	private DTGoiThauDiaDiemNhapVTRepository dtGoiThauDiaDiemNhapVTRepository;

	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;

	@Override
	public Page<DTGoiThauDiaDiemNhapVTRes> findByGoiThauId(Long dtvtGoiThauId, Pageable pageable) {
		Page<DTGoiThauDiaDiemNhapVT> list = dtGoiThauDiaDiemNhapVTRepository.findByDtvtGoiThauId(dtvtGoiThauId, pageable);
		return new PageImpl<>(this.toResponseList(list.getContent()), pageable, list.getTotalElements());
	}

	@Override
	public List<DTGoiThauDiaDiemNhapVTRes> update(List<DTGoiThauDiaDiemNhapVTReq> list, Long dtvtGoiThauId) {
		Set<Long> requestIds = list.stream().map(DTGoiThauDiaDiemNhapVTReq::getId).collect(Collectors.toSet());
		dtGoiThauDiaDiemNhapVTRepository.deleteByDtvtGoiThauIdAndIdNotIn(dtvtGoiThauId, requestIds);
		List<DTGoiThauDiaDiemNhapVT> saveList = new ArrayList<>();
		for (DTGoiThauDiaDiemNhapVTReq req : list) {
			if (req == null)
				continue;

			DTGoiThauDiaDiemNhapVT diaDiemNhapVT = new DTGoiThauDiaDiemNhapVT();
			diaDiemNhapVT.setId(req.getId());
			diaDiemNhapVT.setDtvtGoiThauId(dtvtGoiThauId);
			diaDiemNhapVT.setStt(req.getStt());
			diaDiemNhapVT.setMaDonVi(req.getMaDonVi());
			diaDiemNhapVT.setDonViId(req.getDonViId());
			diaDiemNhapVT.setSoLuongNhap(req.getSoLuongNhap());
			saveList.add(diaDiemNhapVT);
		}
		dtGoiThauDiaDiemNhapVTRepository.saveAll(saveList);

		return this.toResponseList(saveList);
	}

	@Override
	public boolean deleteByGoiThauIds(Set<Long> dtvtGoiThauIds) {
		dtGoiThauDiaDiemNhapVTRepository.deleteByDtvtGoiThauIdIn(dtvtGoiThauIds);
		return true;
	}

	private List<DTGoiThauDiaDiemNhapVTRes> toResponseList(List<DTGoiThauDiaDiemNhapVT> list) {
		List<DTGoiThauDiaDiemNhapVTRes> resList = new ArrayList<>();
		if (CollectionUtils.isEmpty(list))
			return resList;

		Set<String> maDonViSet = new HashSet<>();
		for (DTGoiThauDiaDiemNhapVT diaDiemNhapVT : list) {
			if (!StringUtils.isEmpty(diaDiemNhapVT.getMaDonVi()))
				maDonViSet.add(diaDiemNhapVT.getMaDonVi());
		}

		List<QlnvDmDonvi> donviList = qlnvDmDonviRepository.findByMaDviIn(maDonViSet);

		for (DTGoiThauDiaDiemNhapVT diaDiemNhapVT : list) {
			if (diaDiemNhapVT == null)
				continue;

			QlnvDmDonvi donvi = donviList.stream().filter(dv -> dv.getMaDvi().equals(diaDiemNhapVT.getMaDonVi())).findFirst().orElse(null);
			DTGoiThauDiaDiemNhapVTRes res = this.toRespone(diaDiemNhapVT, donvi);
			if (res != null)
				resList.add(res);
		}

		return resList;

	}

	private DTGoiThauDiaDiemNhapVTRes toRespone(DTGoiThauDiaDiemNhapVT diaDiemNhapVT, QlnvDmDonvi donvi) {
		if (diaDiemNhapVT == null)
			return null;

		DTGoiThauDiaDiemNhapVTRes res = new DTGoiThauDiaDiemNhapVTRes();
		res.setId(diaDiemNhapVT.getId());
		res.setStt(diaDiemNhapVT.getStt());
		res.setDtvtGoiThauId(diaDiemNhapVT.getDtvtGoiThauId());
		if (donvi != null) {
			res.setTenDonVi(donvi.getTenDvi());
			res.setDonViId(diaDiemNhapVT.getDonViId());
			res.setMaDonVi(diaDiemNhapVT.getMaDonVi());
		}

		return res;
	}
}
