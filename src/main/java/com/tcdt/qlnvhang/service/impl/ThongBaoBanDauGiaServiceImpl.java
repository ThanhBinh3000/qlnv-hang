package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.kehoachbandaugia.BanDauGiaPhanLoTaiSanResponseMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSanResponseMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponseMapper;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia.BhBbBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgRepository;
import com.tcdt.qlnvhang.repository.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequest;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgService;
import com.tcdt.qlnvhang.service.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.ExcelHeaderConst;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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
public class ThongBaoBanDauGiaServiceImpl extends BaseServiceImpl implements ThongBaoBanDauGiaService {
	private final ThongBaoBanDauGiaRepository thongBaoBanDauGiaRepository;
	private final ThongBaoBanDauGiaRequestMapper thongBaoBanDauGiaRequestMapper;
	private final ThongBaoBanDauGiaResponseMapper thongBaoBanDauGiaResponseMapper;

	private final BhQdPheDuyetKhbdgRepository qdPheDuyetKhbdgRepository;

	private final BhQdPheDuyetKhBdgThongTinTaiSanRepository taiSanRepository;

	private final BhQdPheDuyetKhBdgThongTinTaiSanResponseMapper taiSanResponseMapper;
	private final BhThongBaoBdgKtRepository thongBaoBdgKtRepository;
	private final BhBbBanDauGiaRepository bhBbBanDauGiaRepository;

	private final FileDinhKemService fileDinhKemService;
	private final QlnvDmVattuRepository dmVattuRepository;
	private final BanDauGiaPhanLoTaiSanRepository phanLoTaiSanRepository;

	private final BanDauGiaPhanLoTaiSanResponseMapper phanLoTaiSanResponseMapper;

	private final BhQdPheDuyetKhbdgService bhQdPheDuyetKhbdgService;

	private static final String SHEET_NAME = "Danh sách thông báo bán đấu giá";

	@Override
	public ThongBaoBanDauGiaResponse create(ThongBaoBanDauGiaRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		ThongBaoBanDauGia thongBaoBanDauGia = thongBaoBanDauGiaRequestMapper.toEntity(req);
		thongBaoBanDauGia.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		thongBaoBanDauGia.setMaDonVi(userInfo.getDvql());
		thongBaoBanDauGia.setCapDonVi(userInfo.getCapDvi());
		thongBaoBanDauGia = thongBaoBanDauGiaRepository.save(thongBaoBanDauGia);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), thongBaoBanDauGia.getId(), ThongBaoBanDauGia.TABLE_NAME);
		thongBaoBanDauGia.setFileDinhKems(fileDinhKems);

		ThongBaoBanDauGiaResponse response = thongBaoBanDauGiaResponseMapper.toDto(thongBaoBanDauGia);

		if (thongBaoBanDauGia.getQdPheDuyetKhBdgId() == null)
			throw new Exception("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");
		Optional<BhQdPheDuyetKhbdg> qdPheDuyetKhbdgOptional = qdPheDuyetKhbdgRepository.findById(thongBaoBanDauGia.getQdPheDuyetKhBdgId());
		if (!qdPheDuyetKhbdgOptional.isPresent())
			throw new Exception("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");

		response.setTenQdPheDuyetKhBd(qdPheDuyetKhbdgOptional.get().getSoQuyetDinh());
		if (!CollectionUtils.isEmpty(req.getTaiSanIdList())) {
			List<BhQdPheDuyetKhBdgThongTinTaiSan> taiSanList = taiSanRepository.findByIdIn(req.getTaiSanIdList());

			for (BhQdPheDuyetKhBdgThongTinTaiSan taiSan : taiSanList) {
				taiSan.setThongBaoBdgId(thongBaoBanDauGia.getId());
			}
			taiSanList = taiSanRepository.saveAll(taiSanList);

			if (!CollectionUtils.isEmpty(taiSanList)) {
				response.setTaiSanBdgList(taiSanResponseMapper.toDto(taiSanList));
			}
		}

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
		List<ThongBaoBanDauGia> thongBaoBanDauGiaList = thongBaoBanDauGiaRepository.findByIdIn(ids);
		if (CollectionUtils.isEmpty(thongBaoBanDauGiaList)) {
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		}

		if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");
		log.info("Delete file dinh kem");
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(KeHoachBanDauGia.TABLE_NAME));

		log.info("Delete ke hoach ban dau gia");
		thongBaoBanDauGiaRepository.deleteAll(thongBaoBanDauGiaList);
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
		ThongBaoBanDauGiaResponse response = thongBaoBanDauGiaResponseMapper.toDto(thongBaoBanDauGia);
		response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(thongBaoBanDauGia.getTrangThai()));
		response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(thongBaoBanDauGia.getTrangThai()));
		QlnvDmVattu dmVattu = dmVattuRepository.findByMa(thongBaoBanDauGia.getMaVatTuCha());
		if (dmVattu != null) {
			response.setTenVatTuCha(dmVattu.getTen());
		}
		if (thongBaoBanDauGia.getQdPheDuyetKhBdgId() == null)
			throw new Exception("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");
		Optional<BhQdPheDuyetKhbdg> qdPheDuyetKhbdgOptional = qdPheDuyetKhbdgRepository.findById(thongBaoBanDauGia.getQdPheDuyetKhBdgId());
		if (!qdPheDuyetKhbdgOptional.isPresent())
			throw new Exception("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");

		response.setTenQdPheDuyetKhBd(qdPheDuyetKhbdgOptional.get().getSoQuyetDinh());

		log.info("Lấy thông tin tài sản bán đấu giá");
		List<BhQdPheDuyetKhBdgThongTinTaiSan> taiSanList = taiSanRepository.findByThongBaoBdgIdIn(Collections.singleton(thongBaoBanDauGia.getId()));

		if (!CollectionUtils.isEmpty(taiSanList)) {
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanResponseList = taiSanResponseMapper.toDto(taiSanList);
			bhQdPheDuyetKhbdgService.buildThongTinKho(taiSanResponseList);
			Set<String> maChungLoaiHHList = taiSanList.stream().map(BhQdPheDuyetKhBdgThongTinTaiSan::getChungLoaiHh).collect(Collectors.toSet());
			Set<QlnvDmVattu> dmVattuList = dmVattuRepository.findByMaIn(maChungLoaiHHList);
			Map<String, QlnvDmVattu> vattuMap = dmVattuList.stream().collect(Collectors.toMap(QlnvDmVattu::getMa, Function.identity(), (ex1, ex2) -> ex1));

			taiSanResponseList.forEach(item -> {
				item.setTenChungLoaiHh(Optional.ofNullable(vattuMap.get(item.getChungLoaiHh())).map(QlnvDmVattu::getTen).orElse(null));
			});
			response.setTaiSanBdgList(taiSanResponseList);
		}
		response.setFileDinhKems(fileDinhKemService.search(thongBaoBanDauGia.getId(), Collections.singleton(ThongBaoBanDauGia.TABLE_NAME)));
		thongBaoBdgKtRepository.findFirstByThongBaoBdgId(thongBaoBanDauGia.getId())
				.ifPresent(t -> {
					response.setTbBdgKtId(t.getId());
					response.setMaTbBdgKt(t.getMaThongBao());
				});

		bhBbBanDauGiaRepository.findFirstByThongBaoBdgId(thongBaoBanDauGia.getId())
				.ifPresent(t -> {
					response.setBbBdgId(t.getId());
					response.setSoBbBdg(t.getSoBienBan());
				});

		this.setThongTinDonVi(response, thongBaoBanDauGia.getMaDonVi());
		return response;
	}

	@Override
	public ThongBaoBanDauGiaResponse updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<ThongBaoBanDauGia> optional = thongBaoBanDauGiaRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		ThongBaoBanDauGia thongBaoBanDauGia = optional.get();
		//validate Trạng Thái
		String trangThai = NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		thongBaoBanDauGia.setTrangThai(trangThaiId);
		thongBaoBanDauGia = thongBaoBanDauGiaRepository.save(thongBaoBanDauGia);
		return thongBaoBanDauGiaResponseMapper.toDto(thongBaoBanDauGia);
	}

	@Override
	public boolean exportToExcel(ThongBaoBanDauGiaSearchRequest req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<ThongBaoBanDauGiaSearchResponse> list = this.search(req).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		String[] rowsName = new String[]{ExcelHeaderConst.STT,
				ExcelHeaderConst.MA_THONG_BAO_BDG,
				ExcelHeaderConst.SO_QD_PHE_DUYET_KH_BDG,
				ExcelHeaderConst.NGAY_TO_CHUC_BDG,
				ExcelHeaderConst.TRICH_YEU,
				ExcelHeaderConst.HINH_THUC_DAU_GIA,
				ExcelHeaderConst.PHUONG_THUC_DG,
				ExcelHeaderConst.LOAI_HANG_HOA,
				ExcelHeaderConst.NAM_KE_HOACH,
				ExcelHeaderConst.THONG_BAO_DAU_GIA_KHONG_THANH,
				ExcelHeaderConst.BIEN_BAN_BDG,
				ExcelHeaderConst.SO_QD_PHE_DUYET_KQ_BDG,
				ExcelHeaderConst.TRANG_THAI,
		};
		String filename = "thong_bao_bdg.xlsx";

		List<Object[]> dataList = new ArrayList<>();

		try {
			for (int i = 0; i < list.size(); i++) {
				dataList.add(list.get(i).toExcel(rowsName, i));
			}

			ExportExcel ex = new ExportExcel(SHEET_NAME, filename, rowsName, dataList, response);
			ex.export();
		} catch (Exception e) {
			log.error("Error export", e);
			return false;
		}
		return true;
	}
}
