package com.tcdt.qlnvhang.response.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.chitieukehoachnam.ChiTieuKeHoachNam;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKehoach_;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.chitieukehoachnam.ChiTieuKeHoachNamResponseMapper;
import com.tcdt.qlnvhang.mapper.kehoachbandaugia.BhDgKehoachRequestMapper;
import com.tcdt.qlnvhang.mapper.kehoachbandaugia.BhDgKehoachResponseMapper;
import com.tcdt.qlnvhang.mapper.kehoachbandaugia.BhDgKhDiaDiemGiaoNhanRequestMapper;
import com.tcdt.qlnvhang.mapper.kehoachbandaugia.BhDgKhPhanLoTaiSanRequestMapper;
import com.tcdt.qlnvhang.repository.chitieukehoachnam.ChiTieuKeHoachNamRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKehoachRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhanRepository;
import com.tcdt.qlnvhang.repository.kehoachbanhangdaugia.BhDgKhPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachSearchReq;
import com.tcdt.qlnvhang.response.chitieukehoachnam.ChiTieuKeHoachNamRes;
import com.tcdt.qlnvhang.response.vattu.bangke.NhBangKeVtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.kehoachbanhangdaugia.KeHoachBanDauGiaService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class KeHoachBanDauGiaServiceImpl extends BaseServiceImpl implements KeHoachBanDauGiaService {
	private final BhDgKehoachRepository bhDgKehoachRepository;
	private final BhDgKhDiaDiemGiaoNhanRepository diaDiemGiaoNhanRepository;
	private final BhDgKhPhanLoTaiSanRepository phanLoTaiSanRepository;
	private final FileDinhKemService fileDinhKemService;

	private final BhDgKehoachRequestMapper kehoachRequestMapper;
	private final BhDgKehoachResponseMapper kehoachResponseMapper;
	private final BhDgKhDiaDiemGiaoNhanRequestMapper diaDiemGiaoNhanRequestMapper;
	private final BhDgKhPhanLoTaiSanRequestMapper phanLoTaiSanRequestMapper;
	private final ChiTieuKeHoachNamResponseMapper chiTieuKeHoachNamResponseMapper;
	private final ChiTieuKeHoachNamRepository chiTieuKeHoachNamRepository;
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
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

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

	@Override
	public Page<BhDgKehoachRes> search(BhDgKehoachSearchReq req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Specification<BhDgKehoach> specs = SpecUtils.build(BhDgKehoach.class);
		specs.and(SpecUtils.ge(BhDgKehoach_.ID, 0));
		if (Objects.nonNull(req.getId())) {
			specs.and(SpecUtils.equal(BhDgKehoach_.ID, req.getId()));
		}

		if (Objects.nonNull(req.getNamKeHoach())) {
			specs.and(SpecUtils.equal(BhDgKehoach_.NAM_KE_HOACH, req.getNamKeHoach()));
		}
		if (Objects.nonNull(req.getSoKeHoach())) {
			specs.and(SpecUtils.like(BhDgKehoach_.SO_KE_HOACH, req.getSoKeHoach()));
		}
		if (Objects.nonNull(req.getTrichYeu())) {
			specs.and(SpecUtils.like(BhDgKehoach_.TRICH_YEU, req.getTrichYeu()));
		}
		if (Objects.nonNull(req.getNgayKyTuNgay())) {
			specs.and(SpecUtils.greaterThanOrEqualTo(BhDgKehoach_.NGAY_KY, req.getNgayKyTuNgay()));
		}
		if (Objects.nonNull(req.getNgayKyTuNgay())) {
			specs.and(SpecUtils.lessThanOrEqualTo(BhDgKehoach_.NGAY_KY, req.getNgayKyDenNgay()));
		}
		if (!CollectionUtils.isEmpty(req.getCapDvis())) {
			specs.and(SpecUtils.in(BhDgKehoach_.CAP_DV, req.getCapDvis()));
		}

		if (!CollectionUtils.isEmpty(req.getMaDvis())) {
			specs.and(SpecUtils.in(BhDgKehoach_.MA_DV, req.getMaDvis()));
		}

		if (!CollectionUtils.isEmpty(req.getLoaiVatTuHangHoa())) {
			specs.and(SpecUtils.in(BhDgKehoach_.LOAI_VAT_TU_HANG_HOA, req.getLoaiVatTuHangHoa()));
		}

		Page<BhDgKehoach> page = bhDgKehoachRepository.findAll(specs, req.getPageable());

		List<BhDgKehoach> response = page.get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(response)) page.map(kehoachResponseMapper::toDto);

		List<Long> ids = response.stream().map(BhDgKehoach::getId).collect(Collectors.toList());
		log.info("Địa điểm giao nhận");
		List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = diaDiemGiaoNhanRepository.findByBhDgKehoachIdIn(ids);
		//Map: Key-value = BhDgKehoachId-list BhDgKhDiaDiemGiaoNhan
		Map<Long, List<BhDgKhDiaDiemGiaoNhan>> diaDiemGiaoNhanMap = diaDiemGiaoNhanList.stream()
				.collect(Collectors.groupingBy(BhDgKhDiaDiemGiaoNhan::getBhDgKehoachId));

		//Map: Key-value = BhDgKehoachId- list BhDgKhPhanLoTaiSan
		log.info("Phân lô tài sản");
		List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = phanLoTaiSanRepository.findByBhDgKehoachIdIn(ids);
		Map<Long, List<BhDgKhPhanLoTaiSan>> phanLoTaiSanMap = phanLoTaiSanList.stream()
				.collect(Collectors.groupingBy(BhDgKhPhanLoTaiSan::getBhDgKehoachId));
		log.info("Chỉ tiêu kế hoạch năm");
		List<Long> chiTieuKeHoachNamIds = response.stream().map(BhDgKehoach::getQdGiaoChiTieuId).collect(Collectors.toList());
		List<ChiTieuKeHoachNam> chiTieuKeHoachNamList = chiTieuKeHoachNamRepository.findByIdIn(chiTieuKeHoachNamIds);
		Map<Long, ChiTieuKeHoachNam> chiTieuKeHoachNamMap = chiTieuKeHoachNamList.stream().collect(Collectors.toMap(ChiTieuKeHoachNam::getId, Function.identity(),
				(existing, replacement) -> existing));


		List<BhDgKehoachRes> responseDto = response.stream().map(it -> {
			if (Objects.nonNull(diaDiemGiaoNhanMap.get(it.getId()))) {
				it.setDiaDiemGiaoNhanList(diaDiemGiaoNhanMap.get(it.getId()));
			}

			if (Objects.nonNull(phanLoTaiSanMap.get(it.getId()))) {
				it.setPhanLoTaiSanList(phanLoTaiSanMap.get(it.getId()));
			}

			if (Objects.nonNull(chiTieuKeHoachNamMap.get(it.getQdGiaoChiTieuId()))) {
				it.setSoQuyetDinhGiaoChiTieu(chiTieuKeHoachNamMap.get(it.getQdGiaoChiTieuId()).getSoQuyetDinh());
			}
			return kehoachResponseMapper.toDto(it);
		}).collect(Collectors.toList());

		return new PageImpl<>(responseDto);
	}
	@Override
	public BhDgKehoachRes updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<BhDgKehoach> optional = bhDgKehoachRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhDgKehoach keHoachDauGia = optional.get();
		//validate Trạng Thái
		String trangThai = TrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		keHoachDauGia.setTrangThai(trangThaiId);
		keHoachDauGia = bhDgKehoachRepository.save(keHoachDauGia);
		return kehoachResponseMapper.toDto(keHoachDauGia);
	}

	@Override
	public boolean exportToExcel(BhDgKehoachSearchReq req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BhDgKehoachRes> list = this.search(req).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		String[] rowsName = new String[] { STT, SO_KE_HOACH, NGAY_LAP_KE_HOACH, NGAY_KY,
				TRICH_YEU, LOAI_HANG_HOA, SO_QD_GIAO_CHI_TIEU, SO_QD_PHE_DUYET_KH_BAN_DAU_GIA,NAM_KE_HOACH, TRANG_THAI};
		String filename = "ke_hoach_ban_dau_gia_hang_hoa.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;

		try {
			for (int i = 0; i < list.size(); i++) {
				BhDgKehoachRes item = list.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i;
				objs[1] = item.getSoKeHoach();
				objs[2] = LocalDateTimeUtils.localDateToString(item.getNgayLapKeHoach());
				objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayKy());
				objs[4] = item.getTrichYeu();
				objs[5] = Optional.ofNullable(Contains.mpLoaiVthh.get(item.getLoaiVatTuHangHoa())).orElse("");
				objs[6] = item.getSoQuyetDinhGiaoChiTieu();
				objs[7] = item.getSoQuyetDinhPheDuyet();
				objs[8] = item.getTenTrangThai();
				objs[9] = item.getTrichYeu();
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
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(BhDgKehoach.TABLE_NAME));
		log.info("Delete địa điểm giao nhận");
		diaDiemGiaoNhanRepository.deleteAllByBhDgKehoachIdIn(ids);

		log.info("Delete phan lo tai san");
		phanLoTaiSanRepository.deleteAllByBhDgKehoachIdIn(ids);


		log.info("Delete ke hoach ban dau gia");
		bhDgKehoachRepository.deleteAllByIdIn(ids);
		return true;
	}
}
