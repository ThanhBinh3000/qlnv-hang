package com.tcdt.qlnvhang.response.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.*;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKehoachRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.kehoachbanhangdaugia.KeHoachBanDauGiaService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class KeHoachBanDauGiaServiceImpl implements KeHoachBanDauGiaService {
	private final BhDgKehoachRepository bhDgKehoachRepository;
	private final BhDgKhDiaDiemGiaoNhanRepository diaDiemGiaoNhanRepository;
	private final BhDgKhPhanLoTaiSanRepository phanLoTaiSanRepository;
	private final FileDinhKemService fileDinhKemService;

	private final BhDgKehoachRequestMapper kehoachRequestMapper;
	private final BhDgKehoachResponseMapper kehoachResponseMapper;
	private final BhDgKhDiaDiemGiaoNhanRequestMapper diaDiemGiaoNhanRequestMapper;
	private final BhDgKhDiaDiemGiaoNhanResponseMapper diaDiemGiaoNhanResponseMapper;
	private final BhDgKhPhanLoTaiSanResponseMapper phanLoTaiSanResponseMapper;
	private final BhDgKhPhanLoTaiSanRequestMapper phanLoTaiSanRequestMapper;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public BhDgKehoachRes create(BhDgKehoachReq req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		log.info("Save ke hoach ban dau gia");
		BhDgKehoach keHoachDauGia = kehoachRequestMapper.toEntity(req);
		keHoachDauGia.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		keHoachDauGia.setNgayTao(LocalDate.now());
		keHoachDauGia.setNguoiTaoId(userInfo.getId());
		keHoachDauGia.setMaDv(userInfo.getDvql());
		keHoachDauGia.setCapDv(userInfo.getCapDvi());
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), BhDgKehoach.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);

		log.info("Save dia diem giao nhan");
		BhDgKehoach finalKeHoachDauGia = keHoachDauGia;
		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = req.getDiaDiemGiaoNhanList().stream().map(it -> {
			BhDgKhDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = diaDiemGiaoNhanRequestMapper.toEntity(it);
			dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return dgKhDiaDiemGiaoNhan;
		}).collect(Collectors.toList());
		diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
		keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);


		log.info("Save phan lo tai san");
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = req.getPhanLoTaiSanList().stream().map(it -> {
			BhDgKhPhanLoTaiSan phanLoTaiSan = phanLoTaiSanRequestMapper.toEntity(it);
			phanLoTaiSan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return phanLoTaiSan;
		}).collect(Collectors.toList());

		phanLoTaiSanList = phanLoTaiSanRepository.saveAll(phanLoTaiSanList);
		keHoachDauGia.setPhanLoTaiSanList(phanLoTaiSanList);

		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public BhDgKehoachRes update(BhDgKehoachReq req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<BhDgKehoach> optional = bhDgKehoachRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhDgKehoach keHoachDauGia = optional.get();

		log.info("Update ke hoach ban dau gia");
		kehoachRequestMapper.partialUpdate(keHoachDauGia, req);

		keHoachDauGia.setNgaySua(LocalDate.now());
		keHoachDauGia.setNguoiSuaId(userInfo.getId());
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);

		BhDgKehoach finalKeHoachDauGia = keHoachDauGia;

		log.info("Update file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), BhDgKehoach.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);


		log.info("Update dia diem giao nhan");
		if (!CollectionUtils.isEmpty(req.getDiaDiemGiaoNhanList())) {
			//Clean data
			List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
			if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
				diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
			}
			//Save data
			diaDiemGiaoNhanList = req.getDiaDiemGiaoNhanList().stream().map(it -> {
				BhDgKhDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = diaDiemGiaoNhanRequestMapper.toEntity(it);
				dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
				return dgKhDiaDiemGiaoNhan;
			}).collect(Collectors.toList());
			diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
			keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);
		}

		log.info("Update phan lo tai san");
		if (!CollectionUtils.isEmpty(req.getPhanLoTaiSanList())) {
			//Clean data
			List<BhDgKhPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
			if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
				phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
			}

			List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = req.getPhanLoTaiSanList().stream().map(it -> {
				BhDgKhPhanLoTaiSan phanLoTaiSan = phanLoTaiSanRequestMapper.toEntity(it);
				phanLoTaiSan.setBhDgKehoachId(finalKeHoachDauGia.getId());
				return phanLoTaiSan;
			}).collect(Collectors.toList());

			phanLoTaiSanList = phanLoTaiSanRepository.saveAll(phanLoTaiSanList);
			keHoachDauGia.setPhanLoTaiSanList(phanLoTaiSanList);
		}

		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		if (id == null) throw new Exception("Bad request.");
		Optional<BhDgKehoach> optional = bhDgKehoachRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhDgKehoach keHoachDauGia = optional.get();

		log.info("Delete file dinh kem");
		fileDinhKemService.delete(keHoachDauGia.getId(), Collections.singleton(BhDgKehoach.TABLE_NAME));
		log.info("Delete địa điểm giao nhận");
		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
		if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
			diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
		}

		log.info("Delete phan lo tai san");
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
		if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
			phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
		}

		log.info("Delete ke hoach ban dau gia");
		bhDgKehoachRepository.delete(keHoachDauGia);
		return true;
	}
}
