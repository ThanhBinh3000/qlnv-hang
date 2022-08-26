package com.tcdt.qlnvhang.service.impl;


import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgCt;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia.*;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgCtRepository;
import com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgCtResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgSearchResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCtResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgService;
import com.tcdt.qlnvhang.service.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExcelHeaderConst;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class BhQdPheDuyetKhbdgServiceImpl extends BaseServiceImpl implements BhQdPheDuyetKhbdgService {
	private final BhQdPheDuyetKhbdgRepository qdPheDuyetKhbdgRepository;

	private final FileDinhKemService fileDinhKemService;
	private static final String SHEET_NAME = "Quyết định phê duyệt kế hoạch bán đấu giá";

	private final BhQdPheDuyetKhbdgResponseMapper qdPheduyetKhbdgResponseMapper;
	private final BhQdPheDuyetKhbdgRequestMapper qdPheduyetKhbdgRequestMapper;

	private final BhTongHopDeXuatKhbdgRepository tongHopDeXuatKhbdgRepository;

	private final QlnvDmVattuRepository dmVattuRepository;

	private final BhQdPheDuyetKhbdgCtResponseMapper chiTietResponseMapper;
	private final BhQdPheDuyetKhbdgCtRequestMapper chiTietRequestMapper;
	private final BhQdPheDuyetKhbdgCtRepository chiTietRepository;

	private final BanDauGiaPhanLoTaiSanRepository phanLoTaiSanRepository;

	private final BhTongHopDeXuatKhbdgService bhTongHopDeXuatKhbdgService;

	private final KhPhanLoTaiSanToQdPheDuyetKhBdgThongTinTaiSanMapper taiSanBdgMapper;

	private final BhQdPheDuyetKhBdgThongTinTaiSanResponseMapper thongTinTaiSanResponseMapper;
	private final BhQdPheDuyetKhBdgThongTinTaiSanRequestMapper thongTinTaiSanRequestMapper;

	private final BhQdPheDuyetKhBdgThongTinTaiSanRepository thongTinTaiSanRepository;
	private final BhQdPheDuyetKhBdgThongTinTaiSanRepository bhQdPheDuyetKhBdgThongTinTaiSanRepository;
	private final KtNganLoRepository ktNganLoRepository;
	private final KtDiemKhoRepository ktDiemKhoRepository;
	private final KtNhaKhoRepository ktNhaKhoRepository;
	private final QlnvDmDonviRepository dmDonviRepository;

	private final KeHoachBanDauGiaRepository keHoachBanDauGiaRepository;

	@Override
	public BhQdPheDuyetKhbdgResponse create(BhQdPheDuyetKhbdgRequest req) throws Exception {
		if (req == null) return null;
		this.validateRequest(req);

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		BhQdPheDuyetKhbdg theEntity = qdPheduyetKhbdgRequestMapper.toEntity(req);
		theEntity.setTrangThai(TrangThaiEnum.DU_THAO.getId());
		theEntity.setNgayTao(LocalDate.now());
		theEntity.setNguoiTaoId(userInfo.getId());
		theEntity.setMaDonVi(userInfo.getDvql());
		theEntity.setCapDonVi(userInfo.getCapDvi());
		theEntity = qdPheDuyetKhbdgRepository.save(theEntity);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), theEntity.getId(), BhQdPheDuyetKhbdg.TABLE_NAME);
		theEntity.setFileDinhKems(fileDinhKems);

		if (CollectionUtils.isEmpty(req.getChiTietList())) return qdPheduyetKhbdgResponseMapper.toDto(theEntity);
		log.debug("Create chi tiết");
		if (!CollectionUtils.isEmpty(req.getChiTietList())) {
			List<BhQdPheDuyetKhbdgCt> chiTietList = createChiTietQd(req, theEntity);
			theEntity.setChiTietList(chiTietList);

		}
		return qdPheduyetKhbdgResponseMapper.toDto(theEntity);
	}

	private void validateRequest(BhQdPheDuyetKhbdgRequest request) throws Exception {
		if (!CollectionUtils.isEmpty(request.getChiTietList())) {
			Set<Long> bhDgKeHoachIdList = request.getChiTietList().stream().map(BhQdPheDuyetKhbdgCtRequest::getBhDgKeHoachId).collect(Collectors.toSet());
			List<KeHoachBanDauGia> keHoachBanDauGiaList = keHoachBanDauGiaRepository.findByIdIn(bhDgKeHoachIdList);
			Map<Long, KeHoachBanDauGia> keHoachBanDauGiaMap = keHoachBanDauGiaList.stream().collect(Collectors.toMap(KeHoachBanDauGia::getId, Function.identity(),
					(existing, replacement) -> existing));
			Set<Long> bhDgKeHoachIdListNotExist = bhDgKeHoachIdList.stream().filter(entry -> keHoachBanDauGiaMap.get(entry) == null).collect(Collectors.toSet());
			if (!CollectionUtils.isEmpty(bhDgKeHoachIdListNotExist)) throw new Exception("Kế hoạch bán đấu giá không tồn tại " + bhDgKeHoachIdListNotExist);
		}
	}

	private List<BhQdPheDuyetKhbdgCt> createChiTietQd(BhQdPheDuyetKhbdgRequest req, BhQdPheDuyetKhbdg theEntity) {
		List<BhQdPheDuyetKhbdgCt> chiTietList = new ArrayList<>();
		for (BhQdPheDuyetKhbdgCtRequest chiTietRequest : req.getChiTietList()) {
			//1. Save chi tiết
			BhQdPheDuyetKhbdgCt chiTiet = chiTietRequestMapper.toEntity(chiTietRequest);
			chiTiet.setQuyetDinhPheDuyetId(theEntity.getId());
			chiTiet = chiTietRepository.save(chiTiet);
			//2. Save thông tin tài sản
			List<BhQdPheDuyetKhBdgThongTinTaiSan> thongTinTaiSanList = thongTinTaiSanRequestMapper.toEntity(chiTietRequest.getThongTinTaiSans());
			for (BhQdPheDuyetKhBdgThongTinTaiSan thongTinTaiSan : thongTinTaiSanList) {
				thongTinTaiSan.setQdPheDuyetKhbdgChiTietId(chiTiet.getId());
			}
			thongTinTaiSanList = thongTinTaiSanRepository.saveAll(thongTinTaiSanList);
			chiTiet.setThongTinTaiSans(thongTinTaiSanList);
			chiTietList.add(chiTiet);
		}
		chiTietList = chiTietRepository.saveAll(chiTietList);

		return chiTietList;
	}

	@Override
	public BhQdPheDuyetKhbdgResponse update(BhQdPheDuyetKhbdgRequest req) throws Exception {
		if (req == null) return null;
		this.validateRequest(req);

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<BhQdPheDuyetKhbdg> optional = qdPheDuyetKhbdgRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");
		BhQdPheDuyetKhbdg theEntity = optional.get();

		log.info("Update qdpd ke hoach ban dau gia");
		qdPheduyetKhbdgRequestMapper.partialUpdate(theEntity, req);

		theEntity.setNgaySua(LocalDate.now());
		theEntity.setNguoiSuaId(userInfo.getId());
		theEntity = qdPheDuyetKhbdgRepository.save(theEntity);

		Set<Long> chiTietIds = req.getChiTietList().stream().map(BhQdPheDuyetKhbdgCtRequest::getId).collect(Collectors.toSet());
		if (!CollectionUtils.isEmpty(chiTietIds)) {
			//Clean thông tin tài sản
			thongTinTaiSanRepository.deleteAllByQdPheDuyetKhbdgChiTietIdIn(chiTietIds);
			//Clean Chi tiết
			chiTietRepository.deleteAllByIdIn(chiTietIds);
			//Create chi tiết
			List<BhQdPheDuyetKhbdgCt> chiTietList = createChiTietQd(req, theEntity);
			theEntity.setChiTietList(chiTietList);
		}
		return qdPheduyetKhbdgResponseMapper.toDto(theEntity);
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

		List<BhQdPheDuyetKhbdg> entityList = qdPheDuyetKhbdgRepository.findByIdIn(ids);

		if (CollectionUtils.isEmpty(entityList)) throw new Exception("Bad request.");


		log.info("Delete file dinh kem");
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(BhQdPheDuyetKhbdg.TABLE_NAME));
		Set<Long> chiTietIds = entityList.stream().map(BhQdPheDuyetKhbdg::getId).collect(Collectors.toSet());
		if (CollectionUtils.isEmpty(chiTietIds)) return true;

		List<BhQdPheDuyetKhbdgCt> chiTietList = chiTietRepository.findByIdIn(chiTietIds);

		//Clean thông tin tài sản
		List<BhQdPheDuyetKhBdgThongTinTaiSan> thongTinTaiSanList = thongTinTaiSanRepository.findByQdPheDuyetKhbdgChiTietIdIn(chiTietIds);
		if (!CollectionUtils.isEmpty(thongTinTaiSanList)) {
			thongTinTaiSanRepository.deleteAll(thongTinTaiSanList);
		}
		//Clean Chi tiết
		if (!CollectionUtils.isEmpty(chiTietList)) {
			chiTietRepository.deleteAll(chiTietList);
		}
		log.info("Delete quyết định phê duyệt kế hoạch bán đấu giá");
		qdPheDuyetKhbdgRepository.deleteAllByIdIn(ids);
		return true;
	}

	@Override
	public Page<BhQdPheDuyetKhbdgSearchResponse> search(BhQdPheDuyetKhbdgSearchRequest req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());

		return qdPheDuyetKhbdgRepository.search(req, req.getPageable());
	}

	@Override
	public BhQdPheDuyetKhbdgResponse detail(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();

		if (userInfo == null) throw new Exception("Bad request.");

		if (!(Contains.CAP_TONG_CUC.equalsIgnoreCase(userInfo.getCapDvi()) || Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi())))
			throw new Exception("Không được phép truy cập");

		Optional<BhQdPheDuyetKhbdg> optional = qdPheDuyetKhbdgRepository.findById(id);
		if (!optional.isPresent()) throw new Exception("quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");
		BhQdPheDuyetKhbdg theEntity = optional.get();

		BhQdPheDuyetKhbdgResponse response = qdPheduyetKhbdgResponseMapper.toDto(theEntity);

		Optional<BhTongHopDeXuatKhbdg> tongHopDeXuatOpt = tongHopDeXuatKhbdgRepository.findById(theEntity.getTongHopDeXuatKhbdgId());
		if (!tongHopDeXuatOpt.isPresent()) {
			throw new EntityNotFoundException("Tổng hợp đề xuất kế hoạch bán đấu giá không tồn tại");
		}
		response.setMaTongHopDeXuatkhbdg(tongHopDeXuatOpt.get().getMaTongHop());

		QlnvDmVattu dmVattu = dmVattuRepository.findByMa(theEntity.getMaVatTuCha());
		if (dmVattu != null) {
			response.setTenVatTuCha(dmVattu.getTen());
		}

		if (Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
			List<BhQdPheDuyetKhBdgThongTinTaiSan> taiSanBdgCuc = bhQdPheDuyetKhBdgThongTinTaiSanRepository.findTaiSanBdgCuc(theEntity.getTongHopDeXuatKhbdgId(), userInfo.getDvql());
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanCucRes = thongTinTaiSanResponseMapper.toDto(taiSanBdgCuc);
			this.buildThongTinKho(taiSanCucRes);
			response.setThongTinTaiSanCucs(taiSanCucRes);
		} else if (Contains.CAP_TONG_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
			response.setChiTietList(this.getThongTinTaiSanTongCuc(theEntity.getId()));
		}
		return response;
	}

	private List<BhQdPheDuyetKhbdgCtResponse> getThongTinTaiSanTongCuc(Long qdPdKhbdgId) {
		List<BhQdPheDuyetKhbdgCt> qdPheDuyetKhbdgCtList = chiTietRepository.findByQuyetDinhPheDuyetIdIn(Collections.singleton(qdPdKhbdgId));

		if (CollectionUtils.isEmpty(qdPheDuyetKhbdgCtList)) return Collections.emptyList();

		Set<Long> chiTietIds = qdPheDuyetKhbdgCtList.stream().map(BhQdPheDuyetKhbdgCt::getId).collect(Collectors.toSet());

		List<BhQdPheDuyetKhBdgThongTinTaiSan> thongTinTaiSanList = thongTinTaiSanRepository.findByQdPheDuyetKhbdgChiTietIdIn(chiTietIds);


		Map<Long, List<BhQdPheDuyetKhBdgThongTinTaiSan>> taiSanMap = thongTinTaiSanList.stream()
				.collect(groupingBy(BhQdPheDuyetKhBdgThongTinTaiSan::getBhDgKehoachId));

		List<BhQdPheDuyetKhbdgCtResponse> responseList = qdPheDuyetKhbdgCtList.stream().map(entry -> {
			BhQdPheDuyetKhbdgCtResponse chiTiet = chiTietResponseMapper.toDto(entry);
			if (taiSanMap.get(entry.getBhDgKeHoachId()) == null) return chiTiet;
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanList = thongTinTaiSanResponseMapper.toDto(taiSanMap.get(entry.getBhDgKeHoachId()));
			this.buildThongTinKho(taiSanList);
			chiTiet.setThongTinTaiSans(taiSanList);
			return chiTiet;
		}).collect(Collectors.toList());

		return responseList;
	}

	@Override
	public boolean exportToExcel(BhQdPheDuyetKhbdgSearchRequest req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BhQdPheDuyetKhbdgSearchResponse> list = this.search(req).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		String[] rowsName = new String[]{ExcelHeaderConst.STT,
				ExcelHeaderConst.SO_QUYET_DINH,
				ExcelHeaderConst.NGAY_KY,
				ExcelHeaderConst.TRICH_YEU,
				ExcelHeaderConst.MA_TONG_HOP,
				ExcelHeaderConst.NAM_KE_HOACH,
				ExcelHeaderConst.LOAI_HANG_HOA,
				ExcelHeaderConst.TRANG_THAI};
		String filename = "Quyet_dinh_phe_duyet_ke_hoach_ban_dau_gia.xlsx";

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

	@Override
	public List<BhQdPheDuyetKhbdgCtResponse> getThongTinPhuLuc(Long bhTongHopDeXuatId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();

		if (userInfo == null) throw new Exception("Bad request.");
		if (!Contains.CAP_TONG_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		if (Objects.isNull(bhTongHopDeXuatId)) throw new Exception("bhTongHopDeXuatId không được để trống");

		BhTongHopDeXuatKhbdgResponse tongHopDeXuatKhbdgResponse = bhTongHopDeXuatKhbdgService.detail(bhTongHopDeXuatId);

		log.info("Lấy thông tin chi tiết tổng hợp đề xuất kế hoạch bán đấu giá");
		List<BhTongHopDeXuatCtResponse> chiTietList = tongHopDeXuatKhbdgResponse.getChiTietList();

		if (CollectionUtils.isEmpty(chiTietList)) return Collections.emptyList();

		List<BhQdPheDuyetKhbdgCtResponse> responseList = chiTietList.stream().map(BhQdPheDuyetKhbdgCtResponse::new).collect(Collectors.toList());

		log.info("Lấy thông tin tài sản của đề xuất kế hoạch bán đấu giá");
		List<Long> bhDgKeHoachIdList = chiTietList.stream()
				.map(BhTongHopDeXuatCtResponse::getBhDgKeHoachId)
				.collect(Collectors.toList());
		;

		if (CollectionUtils.isEmpty(bhDgKeHoachIdList)) return responseList;

		List<BanDauGiaPhanLoTaiSan> phanLoTaiSanList = phanLoTaiSanRepository.findByBhDgKehoachIdIn(bhDgKeHoachIdList);

		List<BhQdPheDuyetKhBdgThongTinTaiSan> qdPheDuyetKhBdgThongTinTaiSanList = taiSanBdgMapper.toEntity(phanLoTaiSanList);

		List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanResponseList = thongTinTaiSanResponseMapper.toDto(qdPheDuyetKhBdgThongTinTaiSanList);

		log.info("Group tài sản theo bhKeHoachBdgId để trả về trong response");
		//key = bhKeHoachBdgId, value = List BhQdPheDuyetKhBdgThongTinTaiSanResponse;
		Map<Long, List<BhQdPheDuyetKhBdgThongTinTaiSanResponse>> taiSanMap = taiSanResponseList.stream()
				.collect(groupingBy(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getBhDgKehoachId));

		for (int i = 0; i < responseList.size(); i++) {
			BhQdPheDuyetKhbdgCtResponse entry = responseList.get(i);
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> res = taiSanMap.get(entry.getBhDgKeHoachId());
			if (res == null) responseList.remove(i);
			this.buildThongTinKho(res);
			entry.setThongTinTaiSans(res);
		}

		return responseList;
	}

	@Override
	public boolean updateStatusQd(StatusReq stReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<BhQdPheDuyetKhbdg> optional = qdPheDuyetKhbdgRepository.findById(stReq.getId());
		if (!optional.isPresent())
			throw new Exception("Biên bản bán đấu giá không tồn tại.");

		BhQdPheDuyetKhbdg theEntity = optional.get();

		String trangThai = NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(stReq.getTrangThai());
		if (org.springframework.util.StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");

		theEntity.setTrangThai(stReq.getTrangThai());
		theEntity.setNguoiPheDuyetId(userInfo.getId());
		theEntity.setNgayPduyet(LocalDate.now());
		theEntity.setLyDoTuChoi(stReq.getLyDo());
		qdPheDuyetKhbdgRepository.save(theEntity);
		return true;
	}

	private void buildThongTinKho(List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> responses) {
		if (org.springframework.util.CollectionUtils.isEmpty(responses)) return;
		List<String> maLoKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaLoKho).collect(Collectors.toList());
		List<String> maNhaKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaNhaKho).collect(Collectors.toList());
		List<String> maDiemKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaDiemKho).collect(Collectors.toList());
		Set<String> maChiCucList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaChiCuc).collect(Collectors.toSet());


		Map<String, KtNganLo> mapNganLo = ktNganLoRepository.findByMaNganloIn(maLoKhoList)
				.stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));

		Map<String, KtDiemKho> mapDiemKho = ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhoList)
				.stream().collect(Collectors.toMap(KtDiemKho::getMaDiemkho, Function.identity()));

		Map<String, KtNhaKho> mapNhaKho = ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhoList)
				.stream().collect(Collectors.toMap(KtNhaKho::getMaNhakho, Function.identity()));

		Map<String, QlnvDmDonvi> mapChiCuc = dmDonviRepository.findByMaDviIn(maChiCucList)
				.stream().collect(Collectors.toMap(QlnvDmDonvi::getMaDvi, Function.identity()));

		for (BhQdPheDuyetKhBdgThongTinTaiSanResponse item : responses) {
			KtNganLo nganLo = mapNganLo.get(item.getMaLoKho());
			KtNhaKho nhaKho = mapNhaKho.get(item.getMaNhaKho());
			KtDiemKho diemKho = mapDiemKho.get(item.getMaDiemKho());
			QlnvDmDonvi chiCuc = mapChiCuc.get(item.getMaChiCuc());
			if (nganLo != null) item.setTenLoKho(nganLo.getTenNganlo());
			if (nhaKho != null) item.setTenNhaKho(nhaKho.getTenNhakho());
			if (diemKho != null) item.setTenDiemKho(diemKho.getTenDiemkho());
			if (chiCuc != null) item.setTenChiCuc(chiCuc.getTenDvi());
		}
	}
}
