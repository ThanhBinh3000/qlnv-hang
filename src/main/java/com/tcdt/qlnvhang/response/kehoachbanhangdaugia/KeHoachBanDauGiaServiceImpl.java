package com.tcdt.qlnvhang.response.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKehoachRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class KeHoachBanDauGiaServiceImpl implements KeHoachBanDauGiaService {
	private final BhDgKehoachRepository bhDgKehoachRepository;
	private final BhDgKhDiaDiemGiaoNhanRepository diaDiemGiaoNhanRepository;
	private final BhDgKhPhanLoTaiSanRepository phanLoTaiSanRepository;
	private final FileDinhKemService fileDinhKemService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public BhDgKehoachRes create(BhDgKehoachReq req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		log.info("Save ke hoach ban dau gia");
		BhDgKehoach keHoachDauGia = ObjectMapperUtils.map(req, BhDgKehoach.class);
		keHoachDauGia.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		keHoachDauGia.setNgayTao(LocalDate.now());
		keHoachDauGia.setNguoiTao(userInfo.getId());
		keHoachDauGia.setMaDv(userInfo.getDvql());
		keHoachDauGia.setCapDv(userInfo.getCapDvi());
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), BhDgKehoach.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);

		log.info("Save dia diem giao nhan");
		BhDgKehoach finalKeHoachDauGia = keHoachDauGia;
		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList =  req.getDiaDiemGiaoNhanList().stream().map(it -> {
			BhDgKhDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = ObjectMapperUtils.map(it, BhDgKhDiaDiemGiaoNhan.class);
			dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return dgKhDiaDiemGiaoNhan;
		}).collect(Collectors.toList());
		diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
		keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);


		log.info("Save phan lo tai san");
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanList =  req.getPhanLoTaiSanList().stream().map(it -> {
			BhDgKhPhanLoTaiSan phanLoTaiSan = ObjectMapperUtils.map(it, BhDgKhPhanLoTaiSan.class);
			phanLoTaiSan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return phanLoTaiSan;
		}).collect(Collectors.toList());

		phanLoTaiSanList = phanLoTaiSanRepository.saveAll(phanLoTaiSanList);
		keHoachDauGia.setPhanLoTaiSanList(phanLoTaiSanList);

		return ObjectMapperUtils.map(keHoachDauGia, BhDgKehoachRes.class);
	}

	@Override
	public BhDgKehoachRes update(BhDgKehoachReq req) throws Exception {
		return null;
	}
}
