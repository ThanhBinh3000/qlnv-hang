package com.tcdt.qlnvhang.service.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.KquaKnghiem;
import com.tcdt.qlnvhang.repository.phieuknghiemcluonghang.KquaKnghiemRepository;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.KquaKnghiemReq;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.KquaKnghiemRes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KquaKnghiemServiceImpl implements KquaKnghiemService {
	@Autowired
	private KquaKnghiemRepository kquaKnghiemRepository;

	@Override
	public Page<KquaKnghiemRes> list(Long phieuKnghiemId, Pageable pageable) {
		Page<KquaKnghiem> list = kquaKnghiemRepository.findByPhieuKnghiemIdOrderBySttAsc(phieuKnghiemId, pageable);
		return new PageImpl<>(this.toResponseList(list.getContent()), pageable, list.getTotalElements());
	}

	@Override
	public void update(Long phieuKnghiemId, List<KquaKnghiemReq> list) {
		Set<Long> rqIds = list.stream().map(KquaKnghiemReq::getId).collect(Collectors.toSet());
		List<KquaKnghiem> kquaKnghiemList = kquaKnghiemRepository.findByPhieuKnghiemId(phieuKnghiemId);
		List<KquaKnghiem> delete = kquaKnghiemList.stream().filter(k -> !rqIds.contains(k.getId())).collect(Collectors.toList());
		kquaKnghiemRepository.deleteAll(delete);
		kquaKnghiemList.removeAll(delete);
		List<KquaKnghiem> saveList = new ArrayList<>();
		for (KquaKnghiemReq kqReq : list) {
			KquaKnghiem kq = kquaKnghiemList.stream().filter(k -> k.getId().equals(kqReq.getId())).findFirst().orElse(null);
			if (kq == null) {
				kq = new KquaKnghiem();
			}
			this.updateEntity(kq, kqReq);
			kq.setPhieuKnghiemId(phieuKnghiemId);
			saveList.add(kq);
		}

		if (!CollectionUtils.isEmpty(saveList))
			kquaKnghiemRepository.saveAll(saveList);
	}

	private List<KquaKnghiemRes> toResponseList(List<KquaKnghiem> list) {
		List<KquaKnghiemRes> resList = new ArrayList<>();
		for (KquaKnghiem kq : list) {
			if (kq == null)
				continue;
			KquaKnghiemRes res = new KquaKnghiemRes();
			BeanUtils.copyProperties(kq, res);
			resList.add(res);
		}

		return resList;
	}

	private void updateEntity(KquaKnghiem kq, KquaKnghiemReq req) {
		BeanUtils.copyProperties(req, kq, "id");
	}

	@Override
	public void deleteByPhieuKnghiemId(Long phieuKnghiemId) {
		kquaKnghiemRepository.deleteByphieuKnghiemId(phieuKnghiemId);
	}
}
