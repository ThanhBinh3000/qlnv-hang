package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia_;
import com.tcdt.qlnvhang.entities.chitieukehoachnam.ChiTieuKeHoachNam;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia.BanDauGiaDiaDiemGiaoNhanRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia.BanDauGiaPhanLoTaiSanRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia.KeHoachBanDauGiaRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia.KeHoachBanDauGiaResponseMapper;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhanRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.chitieukehoachnam.ChiTieuKeHoachNamRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KehoachBanDauGiaRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.KeHoachBanDauGiaResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class KeHoachBanDauGiaServiceImpl extends BaseServiceImpl implements KeHoachBanDauGiaService {
	private final KeHoachBanDauGiaRepository keHoachBanDauGiaRepository;
	private final BanDauGiaDiaDiemGiaoNhanRepository diaDiemGiaoNhanRepository;
	private final BanDauGiaPhanLoTaiSanRepository phanLoTaiSanRepository;
	private final FileDinhKemService fileDinhKemService;

	private final KeHoachBanDauGiaRequestMapper kehoachRequestMapper;
	private final KeHoachBanDauGiaResponseMapper kehoachResponseMapper;
	private final BanDauGiaDiaDiemGiaoNhanRequestMapper diaDiemGiaoNhanRequestMapper;
	private final BanDauGiaPhanLoTaiSanRequestMapper phanLoTaiSanRequestMapper;
	private final ChiTieuKeHoachNamRepository chiTieuKeHoachNamRepository;
	private final QlnvDmVattuRepository qlnvDmVattuRepository;
	private static final String SHEET_NAME = "Danh sách kế hoạch bán đấu giá";
	private static final String STT = "STT";
	private static final String SO_KE_HOACH = "Số kế hoạch";
	private static final String NGAY_LAP_KE_HOACH = "Ngày lập kế hoạch";
	private static final String NGAY_KY = "Ngày ký";
	private static final String TRICH_YEU = "Trích yếu";
	private static final String LOAI_HANG_HOA = "Loại hàng hóa";
	private static final String SO_QD_GIAO_CHI_TIEU = "Số quyết định giao chỉ tiêu";
	private static final String SO_QD_PHE_DUYET_KH_BAN_DAU_GIA = "Số QĐ phê duyệt KH bán đấu giá";
	private static final String NAM_KE_HOACH = "Năm kế hoạch";
	private static final String TRANG_THAI = "Trạng thái";


	@Override
	public KeHoachBanDauGiaResponse create(KehoachBanDauGiaRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		log.info("Save ke hoach ban dau gia");
		KeHoachBanDauGia keHoachDauGia = kehoachRequestMapper.toEntity(req);
		keHoachDauGia.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		keHoachDauGia.setNgayTao(LocalDate.now());
		keHoachDauGia.setNguoiTaoId(userInfo.getId());
		keHoachDauGia.setMaDv(userInfo.getDvql());
		keHoachDauGia.setCapDv(userInfo.getCapDvi());
		keHoachDauGia = keHoachBanDauGiaRepository.save(keHoachDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), KeHoachBanDauGia.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);

		log.info("Save dia diem giao nhan");
		KeHoachBanDauGia finalKeHoachDauGia = keHoachDauGia;
		List<BanDauGiaDiaDiemGiaoNhan> diaDiemGiaoNhanList = req.getDiaDiemGiaoNhanList().stream().map(it -> {
			BanDauGiaDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = diaDiemGiaoNhanRequestMapper.toEntity(it);
			dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return dgKhDiaDiemGiaoNhan;
		}).collect(Collectors.toList());
		diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
		keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);


		log.info("Save phan lo tai san");
		List<BanDauGiaPhanLoTaiSan> phanLoTaiSanList = req.getPhanLoTaiSanList().stream().map(it -> {
			BanDauGiaPhanLoTaiSan phanLoTaiSan = phanLoTaiSanRequestMapper.toEntity(it);
			phanLoTaiSan.setBhDgKehoachId(finalKeHoachDauGia.getId());
			return phanLoTaiSan;
		}).collect(Collectors.toList());

		phanLoTaiSanList = phanLoTaiSanRepository.saveAll(phanLoTaiSanList);
		keHoachDauGia.setPhanLoTaiSanList(phanLoTaiSanList);

		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public KeHoachBanDauGiaResponse update(KehoachBanDauGiaRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<KeHoachBanDauGia> optional = keHoachBanDauGiaRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		KeHoachBanDauGia keHoachDauGia = optional.get();

		log.info("Update ke hoach ban dau gia");
		kehoachRequestMapper.partialUpdate(keHoachDauGia, req);

		keHoachDauGia.setNgaySua(LocalDate.now());
		keHoachDauGia.setNguoiSuaId(userInfo.getId());
		keHoachDauGia = keHoachBanDauGiaRepository.save(keHoachDauGia);

		KeHoachBanDauGia finalKeHoachDauGia = keHoachDauGia;

		log.info("Update file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), keHoachDauGia.getId(), KeHoachBanDauGia.TABLE_NAME);
		keHoachDauGia.setFileDinhKems(fileDinhKems);


		log.info("Update dia diem giao nhan");
		if (!CollectionUtils.isEmpty(req.getDiaDiemGiaoNhanList())) {
			//Clean data
			List<BanDauGiaDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
			if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
				diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
			}
			//Save data
			diaDiemGiaoNhanList = req.getDiaDiemGiaoNhanList().stream().map(it -> {
				BanDauGiaDiaDiemGiaoNhan dgKhDiaDiemGiaoNhan = diaDiemGiaoNhanRequestMapper.toEntity(it);
				dgKhDiaDiemGiaoNhan.setBhDgKehoachId(finalKeHoachDauGia.getId());
				return dgKhDiaDiemGiaoNhan;
			}).collect(Collectors.toList());
			diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.saveAll(diaDiemGiaoNhanList);
			keHoachDauGia.setDiaDiemGiaoNhanList(diaDiemGiaoNhanList);
		}

		log.info("Update phan lo tai san");
		if (!CollectionUtils.isEmpty(req.getPhanLoTaiSanList())) {
			//Clean data
			List<BanDauGiaPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
			if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
				phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
			}

			List<BanDauGiaPhanLoTaiSan> phanLoTaiSanList = req.getPhanLoTaiSanList().stream().map(it -> {
				BanDauGiaPhanLoTaiSan phanLoTaiSan = phanLoTaiSanRequestMapper.toEntity(it);
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
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		if (id == null) throw new Exception("Bad request.");
		Optional<KeHoachBanDauGia> optional = keHoachBanDauGiaRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		KeHoachBanDauGia keHoachDauGia = optional.get();

		log.info("Delete file dinh kem");
		fileDinhKemService.delete(keHoachDauGia.getId(), Collections.singleton(KeHoachBanDauGia.TABLE_NAME));
		log.info("Delete địa điểm giao nhận");
		List<BanDauGiaDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
		if (!CollectionUtils.isEmpty(diaDiemGiaoNhanList)) {
			diaDiemGiaoNhanRepository.deleteAll(diaDiemGiaoNhanList);
		}

		log.info("Delete phan lo tai san");
		List<BanDauGiaPhanLoTaiSan> phanLoTaiSanListExisted = phanLoTaiSanRepository.findByBhDgKehoachId(keHoachDauGia.getId());
		if (!CollectionUtils.isEmpty(phanLoTaiSanListExisted)) {
			phanLoTaiSanRepository.deleteAll(phanLoTaiSanListExisted);
		}

		log.info("Delete ke hoach ban dau gia");
		keHoachBanDauGiaRepository.delete(keHoachDauGia);
		return true;
	}

	@Override
	public Page<KeHoachBanDauGiaResponse> search(KeHoachBanDauGiaSearchRequest req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Specification<KeHoachBanDauGia> specs = SpecUtils.build(KeHoachBanDauGia.class);
		specs.and(SpecUtils.ge(KeHoachBanDauGia_.ID, 0));
		if (Objects.nonNull(req.getId())) {
			specs.and(SpecUtils.equal(KeHoachBanDauGia_.ID, req.getId()));
		}

		if (Objects.nonNull(req.getNamKeHoach())) {
			specs.and(SpecUtils.equal(KeHoachBanDauGia_.NAM_KE_HOACH, req.getNamKeHoach()));
		}
		if (Objects.nonNull(req.getSoKeHoach())) {
			specs.and(SpecUtils.like(KeHoachBanDauGia_.SO_KE_HOACH, req.getSoKeHoach()));
		}
		if (Objects.nonNull(req.getTrichYeu())) {
			specs.and(SpecUtils.like(KeHoachBanDauGia_.TRICH_YEU, req.getTrichYeu()));
		}
		if (Objects.nonNull(req.getNgayKyTuNgay())) {
			specs.and(SpecUtils.greaterThanOrEqualTo(KeHoachBanDauGia_.NGAY_KY, req.getNgayKyTuNgay()));
		}
		if (Objects.nonNull(req.getNgayKyTuNgay())) {
			specs.and(SpecUtils.lessThanOrEqualTo(KeHoachBanDauGia_.NGAY_KY, req.getNgayKyDenNgay()));
		}
		if (!CollectionUtils.isEmpty(req.getCapDvis())) {
			specs.and(SpecUtils.in(KeHoachBanDauGia_.CAP_DV, req.getCapDvis()));
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			specs.and(SpecUtils.in(KeHoachBanDauGia_.MA_DV, req.getMaDvis()));
		}

		if (!CollectionUtils.isEmpty(req.getLoaiVatTuHangHoa())) {
			specs.and(SpecUtils.in(KeHoachBanDauGia_.LOAI_VAT_TU_HANG_HOA, req.getLoaiVatTuHangHoa()));
		}

		Page<KeHoachBanDauGia> page = keHoachBanDauGiaRepository.findAll(specs, req.getPageable());

		List<KeHoachBanDauGia> response = page.get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(response)) page.map(kehoachResponseMapper::toDto);

		List<Long> ids = response.stream().map(KeHoachBanDauGia::getId).collect(Collectors.toList());
		log.info("Địa điểm giao nhận");
		List<BanDauGiaDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachIdIn(ids);
		//Map: Key-value = BhDgKehoachId-list BhDgKhDiaDiemGiaoNhan
		Map<Long, List<BanDauGiaDiaDiemGiaoNhan>> diaDiemGiaoNhanMap = diaDiemGiaoNhanList.stream()
				.collect(Collectors.groupingBy(BanDauGiaDiaDiemGiaoNhan::getBhDgKehoachId));

		//Map: Key-value = BhDgKehoachId- list BhDgKhPhanLoTaiSan
		log.info("Phân lô tài sản");
		List<BanDauGiaPhanLoTaiSan> phanLoTaiSanList = phanLoTaiSanRepository.findByBhDgKehoachIdIn(ids);
		Map<Long, List<BanDauGiaPhanLoTaiSan>> phanLoTaiSanMap = phanLoTaiSanList.stream()
				.collect(Collectors.groupingBy(BanDauGiaPhanLoTaiSan::getBhDgKehoachId));
		log.info("Chỉ tiêu kế hoạch năm");
		List<Long> chiTieuKeHoachNamIds = response.stream().map(KeHoachBanDauGia::getQdGiaoChiTieuId).collect(Collectors.toList());
		List<ChiTieuKeHoachNam> chiTieuKeHoachNamList = chiTieuKeHoachNamRepository.findByIdIn(chiTieuKeHoachNamIds);
		Map<Long, ChiTieuKeHoachNam> chiTieuKeHoachNamMap = chiTieuKeHoachNamList.stream().collect(Collectors.toMap(ChiTieuKeHoachNam::getId, Function.identity(),
				(existing, replacement) -> existing));

		log.info("Vật tư hàng hóa");
		Set<String> maVatTuHangHoa = response.stream().map(KeHoachBanDauGia::getLoaiHangHoa).collect(Collectors.toSet());
		//Key-vaule = mã vật tư hàng hóa - vật tư hàng hóa
		Map<String, QlnvDmVattu> vatTuHangHoaMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(maVatTuHangHoa)) {
			Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTuHangHoa);
			vatTuHangHoaMap = vatTus.stream().collect(Collectors
					.toMap(QlnvDmVattu::getMa, Function.identity(), (existing, replacement) -> existing));
		}


		Map<String, QlnvDmVattu> finalVatTuHangHoaMap = vatTuHangHoaMap;
		List<KeHoachBanDauGiaResponse> responseDto = response.stream().map(it -> {
			if (Objects.nonNull(diaDiemGiaoNhanMap.get(it.getId()))) {
				it.setDiaDiemGiaoNhanList(diaDiemGiaoNhanMap.get(it.getId()));
			}

			if (Objects.nonNull(phanLoTaiSanMap.get(it.getId()))) {
				it.setPhanLoTaiSanList(phanLoTaiSanMap.get(it.getId()));
			}

			if (Objects.nonNull(chiTieuKeHoachNamMap.get(it.getQdGiaoChiTieuId()))) {
				it.setSoQuyetDinhGiaoChiTieu(chiTieuKeHoachNamMap.get(it.getQdGiaoChiTieuId()).getSoQuyetDinh());
			}
			if (Objects.nonNull(finalVatTuHangHoaMap.get(it.getLoaiHangHoa()))) {
				it.setTenHangHoa(finalVatTuHangHoaMap.get(it.getLoaiHangHoa()).getTen());
			}
			return kehoachResponseMapper.toDto(it);
		}).collect(Collectors.toList());

		return new PageImpl<>(responseDto);
	}

	@Override
	public KeHoachBanDauGiaResponse updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<KeHoachBanDauGia> optional = keHoachBanDauGiaRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		KeHoachBanDauGia keHoachDauGia = optional.get();
		//validate Trạng Thái
		String trangThai = TrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		keHoachDauGia.setTrangThai(trangThaiId);
		keHoachDauGia = keHoachBanDauGiaRepository.save(keHoachDauGia);
		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public boolean exportToExcel(KeHoachBanDauGiaSearchRequest req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<KeHoachBanDauGiaResponse> list = this.search(req).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		String[] rowsName = new String[]{STT, SO_KE_HOACH, NGAY_LAP_KE_HOACH, NGAY_KY,
				TRICH_YEU, LOAI_HANG_HOA, SO_QD_GIAO_CHI_TIEU, SO_QD_PHE_DUYET_KH_BAN_DAU_GIA, NAM_KE_HOACH, TRANG_THAI};
		String filename = "ke_hoach_ban_dau_gia_hang_hoa.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;

		try {
			for (int i = 0; i < list.size(); i++) {
				KeHoachBanDauGiaResponse item = list.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i;
				objs[1] = item.getSoKeHoach();
				objs[2] = LocalDateTimeUtils.localDateToString(item.getNgayLapKeHoach());
				objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayKy());
				objs[4] = item.getTrichYeu();
				objs[5] = Optional.ofNullable(Contains.mpLoaiVthh.get(item.getLoaiVatTuHangHoa())).orElse("");
				objs[6] = item.getSoQuyetDinhGiaoChiTieu();
				objs[7] = item.getSoQuyetDinhPheDuyet();
				objs[8] = item.getNamKeHoach();
				objs[9] = item.getTenTrangThai();
				dataList.add(objs);
			}

			ExportExcel ex = new ExportExcel(SHEET_NAME, filename, rowsName, dataList, response);
			ex.export();
		} catch (Exception e) {
			log.error("Error export", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteMultiple(List<Long> ids) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");
		log.info("Delete file dinh kem");
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(KeHoachBanDauGia.TABLE_NAME));
		log.info("Delete địa điểm giao nhận");
		diaDiemGiaoNhanRepository.deleteAllByBhDgKehoachIdIn(ids);

		log.info("Delete phan lo tai san");
		phanLoTaiSanRepository.deleteAllByBhDgKehoachIdIn(ids);


		log.info("Delete ke hoach ban dau gia");
		keHoachBanDauGiaRepository.deleteAllByIdIn(ids);
		return true;
	}

	@Override
	public KeHoachBanDauGiaResponse detail(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<KeHoachBanDauGia> optional = keHoachBanDauGiaRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		KeHoachBanDauGia keHoachDauGia = optional.get();
		return kehoachResponseMapper.toDto(keHoachDauGia);
	}
}
