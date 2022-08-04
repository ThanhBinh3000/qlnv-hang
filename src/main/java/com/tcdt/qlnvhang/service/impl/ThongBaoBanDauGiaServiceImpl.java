package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponseMapper;
import com.tcdt.qlnvhang.repository.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRepository;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequest;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
public class ThongBaoBanDauGiaServiceImpl extends BaseServiceImpl implements ThongBaoBanDauGiaService {
	private final ThongBaoBanDauGiaRepository thongBaoBanDauGiaRepository;
	private final ThongBaoBanDauGiaRequestMapper thongBaoBanDauGiaRequestMapper;
	private final ThongBaoBanDauGiaResponseMapper thongBaoBanDauGiaResponseMapper;

	private final FileDinhKemService fileDinhKemService;

	private static final String SHEET_NAME = "Danh sách kế hoạch bán đấu giá";

	@Override
	public ThongBaoBanDauGiaResponse create(ThongBaoBanDauGiaRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		ThongBaoBanDauGia thongBaoBanDauGia = thongBaoBanDauGiaRequestMapper.toEntity(req);
		thongBaoBanDauGia.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		thongBaoBanDauGia.setNgayTao(LocalDate.now());
		thongBaoBanDauGia.setNguoiTaoId(userInfo.getId());
		thongBaoBanDauGia.setMaDonVi(userInfo.getDvql());
		thongBaoBanDauGia.setCapDonVi(userInfo.getCapDvi());
		thongBaoBanDauGia = thongBaoBanDauGiaRepository.save(thongBaoBanDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), thongBaoBanDauGia.getId(), ThongBaoBanDauGia.TABLE_NAME);
		thongBaoBanDauGia.setFileDinhKems(fileDinhKems);
		return thongBaoBanDauGiaResponseMapper.toDto(thongBaoBanDauGia);
	}

	@Override
	public ThongBaoBanDauGiaResponse update(ThongBaoBanDauGiaRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<ThongBaoBanDauGia> optional = thongBaoBanDauGiaRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		ThongBaoBanDauGia thongBaoBanDauGia = optional.get();

		log.info("Update ke hoach ban dau gia");
		thongBaoBanDauGiaRequestMapper.partialUpdate(thongBaoBanDauGia, req);

		thongBaoBanDauGia.setNgaySua(LocalDate.now());
		thongBaoBanDauGia.setNguoiSuaId(userInfo.getId());
		thongBaoBanDauGia = thongBaoBanDauGiaRepository.save(thongBaoBanDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), thongBaoBanDauGia.getId(), ThongBaoBanDauGia.TABLE_NAME);
		thongBaoBanDauGia.setFileDinhKems(fileDinhKems);
		return thongBaoBanDauGiaResponseMapper.toDto(thongBaoBanDauGia);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		return this.deleteMultiple(Collections.singletonList(id));
	}

	@Override
	public boolean deleteMultiple(List<Long> ids) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");
		log.info("Delete file dinh kem");
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(KeHoachBanDauGia.TABLE_NAME));

		log.info("Delete ke hoach ban dau gia");
		thongBaoBanDauGiaRepository.deleteAllByIdIn(ids);
		return true;
	}

	@Override
	public Page<ThongBaoBanDauGiaSearchResponse> search(ThongBaoBanDauGiaSearchRequest req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		return thongBaoBanDauGiaRepository.search(req, req.getPageable());
	}

	@Override
	public ThongBaoBanDauGiaResponse detail(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");


		Optional<ThongBaoBanDauGia> optional = thongBaoBanDauGiaRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Thông báo bán đấu giá không tồn tại");
		ThongBaoBanDauGia thongBaoBanDauGia = optional.get();
		return null;
	}
}
