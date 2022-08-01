package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponseMapper;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRepository;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgService;
import com.tcdt.qlnvhang.table.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class BhTongHopDeXuatKhbdgServiceImpl extends BaseServiceImpl implements BhTongHopDeXuatKhbdgService {
	private final BhTongHopDeXuatKhbdgRepository deXuatKhbdgRepository;
	private final BhTongHopDeXuatKhbdgResponseMapper tongHopDeXuatKhbdgResponseMapper;
	private final BhTongHopDeXuatKhbdgRequestMapper tongHopDeXuatKhbdgRequestMapper;

	@Override
	public BhTongHopDeXuatKhbdgResponse create(BhTongHopDeXuatKhbdgRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		BhTongHopDeXuatKhbdg theEntity = tongHopDeXuatKhbdgRequestMapper.toEntity(req);
		theEntity.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		theEntity.setNgayTao(LocalDate.now());
		theEntity.setNguoiTaoId(userInfo.getId());
		theEntity.setMaDonVi(userInfo.getDvql());
		theEntity.setCapDonVi(userInfo.getCapDvi());
		theEntity = deXuatKhbdgRepository.save(theEntity);

		return tongHopDeXuatKhbdgResponseMapper.toDto(theEntity);
	}

	@Override
	public BhTongHopDeXuatKhbdgResponse update(BhTongHopDeXuatKhbdgRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<BhTongHopDeXuatKhbdg> optional = deXuatKhbdgRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhTongHopDeXuatKhbdg theEntity = optional.get();

		log.info("Update ke hoach ban dau gia");
		tongHopDeXuatKhbdgRequestMapper.partialUpdate(theEntity, req);

		theEntity.setNgaySua(LocalDate.now());
		theEntity.setNguoiSuaId(userInfo.getId());
		theEntity = deXuatKhbdgRepository.save(theEntity);

		return tongHopDeXuatKhbdgResponseMapper.toDto(theEntity);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		if (id == null) throw new Exception("Bad request.");
		this.deleteMultiple(Collections.singletonList(id));
		return true;
	}

	@Override
	public boolean deleteMultiple(List<Long> ids) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");

		log.info("Delete tổng hợp đề xuất kế hoạch bán đấu giá");
		deXuatKhbdgRepository.deleteAllByIdIn(ids);
		return true;
	}

	@Override
	public Page<BhTongHopDeXuatKhbdgSearchResponse> search(BhTongHopDeXuatKhbdgSearchRequest req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		return deXuatKhbdgRepository.search(req, req.getPageable());
	}
}
