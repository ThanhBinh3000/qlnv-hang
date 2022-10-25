package com.tcdt.qlnvhang.service.impl;


import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgCt;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
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
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
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
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
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

	private final KtNganKhoRepository ktNganKhoRepository;


	private final KeHoachBanDauGiaRepository keHoachBanDauGiaRepository;

	@Override
	@Transactional
	public BhQdPheDuyetKhbdgResponse create(BhQdPheDuyetKhbdgRequest req) throws Exception {
		if (req == null) return null;
		this.validateRequest(req);

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		req.setId(null);
		BhQdPheDuyetKhbdg theEntity = qdPheduyetKhbdgRequestMapper.toEntity(req);
		theEntity.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
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
		//save lại trạng thái bản ghi tổng hợp thành dự thảo qd,cập nhật số qd vào bảng tổng hợp
		Optional<BhTongHopDeXuatKhbdg> bhTongHopDeXuatKhbdg = tongHopDeXuatKhbdgRepository.findById(theEntity.getTongHopDeXuatKhbdgId());
		if(bhTongHopDeXuatKhbdg.isPresent()){
			bhTongHopDeXuatKhbdg.get().setTrangThai(Contains.DADUTHAO_QD);
			bhTongHopDeXuatKhbdg.get().setSoQdPheDuyetKhbdg(theEntity.getSoQuyetDinh());
			tongHopDeXuatKhbdgRepository.save(bhTongHopDeXuatKhbdg.get());
		}
		//Cập nhật số qd phê duyệt vào bản ghi đề xuất
		List<Long> idsDexuat = theEntity.getChiTietList().stream().map(BhQdPheDuyetKhbdgCt::getBhDgKeHoachId).collect(Collectors.toList());
		List<KeHoachBanDauGia> listDx = keHoachBanDauGiaRepository.findByIdIn(idsDexuat);
		if(!CollectionUtils.isEmpty(listDx)){
			listDx.stream().map(item->{
				item.setSoQuyetDinhPheDuyet(req.getSoQuyetDinh());
				return item;
			});
			keHoachBanDauGiaRepository.saveAll(listDx);
		}
		//Cập nhật số qd phê duyệt vào bản ghi Tổng hợp đề xuất

		return qdPheduyetKhbdgResponseMapper.toDto(theEntity);
	}

	private void validateRequest(BhQdPheDuyetKhbdgRequest request) throws Exception {
		if (!CollectionUtils.isEmpty(request.getChiTietList())) {
			Set<Long> bhDgKeHoachIdList = request.getChiTietList().stream().map(BhQdPheDuyetKhbdgCtRequest::getBhDgKeHoachId).collect(Collectors.toSet());
			List<KeHoachBanDauGia> keHoachBanDauGiaList = keHoachBanDauGiaRepository.findByIdIn(bhDgKeHoachIdList);
			Map<Long, KeHoachBanDauGia> keHoachBanDauGiaMap = keHoachBanDauGiaList.stream().collect(Collectors.toMap(KeHoachBanDauGia::getId, Function.identity(),
					(existing, replacement) -> existing));
			Set<Long> bhDgKeHoachIdListNotExist = bhDgKeHoachIdList.stream().filter(entry -> keHoachBanDauGiaMap.get(entry) == null).collect(Collectors.toSet());
			if (!CollectionUtils.isEmpty(bhDgKeHoachIdListNotExist))
				throw new Exception("Kế hoạch bán đấu giá không tồn tại " + bhDgKeHoachIdListNotExist);
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
		theEntity = qdPheDuyetKhbdgRepository.save(theEntity);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), theEntity.getId(), BhQdPheDuyetKhbdg.TABLE_NAME);
		theEntity.setFileDinhKems(fileDinhKems);

		if (!CollectionUtils.isEmpty(req.getChiTietList())) {
			this.cleanDataBeforeUpdate(theEntity);
			//Create chi tiết
			List<BhQdPheDuyetKhbdgCt> chiTietList = createChiTietQd(req, theEntity);
			theEntity.setChiTietList(chiTietList);
		}

		return qdPheduyetKhbdgResponseMapper.toDto(theEntity);
	}

	private void cleanDataBeforeUpdate(BhQdPheDuyetKhbdg theEntity) {
		List<BhQdPheDuyetKhbdgCt> chiTietList = chiTietRepository.findByQuyetDinhPheDuyetIdIn(Collections.singleton(theEntity.getId()));
		if (CollectionUtils.isEmpty(chiTietList)) return;
		Set<Long> chiTietIds = chiTietList.stream().map(BhQdPheDuyetKhbdgCt::getId).collect(Collectors.toSet());
		List<BhQdPheDuyetKhBdgThongTinTaiSan> taiSanList = thongTinTaiSanRepository.findByQdPheDuyetKhbdgChiTietIdIn(chiTietIds);

		//Clean thông tin tài sản
		if (!CollectionUtils.isEmpty(taiSanList)) {
			thongTinTaiSanRepository.deleteAll(taiSanList);
		}

		//Clean chi tiết
		if (!CollectionUtils.isEmpty(chiTietList)) {
			chiTietRepository.deleteAll(chiTietList);
		}
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
		Set<Long> idList = entityList.stream().map(BhQdPheDuyetKhbdg::getId).collect(Collectors.toSet());
		if (CollectionUtils.isEmpty(idList)) return true;

		List<BhQdPheDuyetKhbdgCt> chiTietList = chiTietRepository.findByQuyetDinhPheDuyetIdIn(idList);

		Set<Long> chiTietIds = chiTietList.stream().map(BhQdPheDuyetKhbdgCt::getId).collect(Collectors.toSet());

		//Clean thông tin tài sản
		List<BhQdPheDuyetKhBdgThongTinTaiSan> thongTinTaiSanList = thongTinTaiSanRepository.findByQdPheDuyetKhbdgChiTietIdIn(chiTietIds);
		if (!CollectionUtils.isEmpty(thongTinTaiSanList)) {
			thongTinTaiSanRepository.deleteAll(thongTinTaiSanList);
		}
		//set lại soqd = null cho bản ghi dexuat
		List<Long> idsKeHoach = chiTietList.stream().map(BhQdPheDuyetKhbdgCt::getBhDgKeHoachId).collect(Collectors.toList());
		List<KeHoachBanDauGia> listKeHoach = keHoachBanDauGiaRepository.findByIdIn(idsKeHoach);
		if(!CollectionUtils.isEmpty(listKeHoach)){
			listKeHoach.stream().map(item->{
				item.setSoQuyetDinhPheDuyet(null);
				return item;
			});
			keHoachBanDauGiaRepository.saveAll(listKeHoach);
		}
		//Clean Chi tiết
		if (!CollectionUtils.isEmpty(chiTietList)) {
			chiTietRepository.deleteAll(chiTietList);
		}
		log.info("Delete quyết định phê duyệt kế hoạch bán đấu giá");
		qdPheDuyetKhbdgRepository.deleteAll(entityList);
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

		List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(theEntity.getId(), Collections.singleton(BhQdPheDuyetKhbdg.TABLE_NAME));
		if (!CollectionUtils.isEmpty(fileDinhKemList)) theEntity.setFileDinhKems(fileDinhKemList);

		BhQdPheDuyetKhbdgResponse response = qdPheduyetKhbdgResponseMapper.toDto(theEntity);
		if(theEntity.getTongHopDeXuatKhbdgId() == null){
			throw new EntityNotFoundException("Tổng hợp đề xuất kế hoạch bán đấu giá không tồn tại");
		}
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
			List<BhQdPheDuyetKhBdgThongTinTaiSan> taiSanBdgCuc = bhQdPheDuyetKhBdgThongTinTaiSanRepository.findTaiSanBdgCuc(theEntity.getId(), userInfo.getDvql());
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanCucRes = thongTinTaiSanResponseMapper.toDto(taiSanBdgCuc);
			this.buildThongTinKho(taiSanCucRes);

			Set<String> maChungLoaiHHList = taiSanBdgCuc.stream().map(BhQdPheDuyetKhBdgThongTinTaiSan::getChungLoaiHh).collect(Collectors.toSet());
			Set<QlnvDmVattu> dmVattuList = dmVattuRepository.findByMaIn(maChungLoaiHHList);
			Map<String, QlnvDmVattu> vattuMap = dmVattuList.stream().collect(Collectors.toMap(QlnvDmVattu::getMa, Function.identity(), (ex1, ex2) -> ex1));

			taiSanCucRes.forEach(item -> {
				item.setTenChungLoaiHh(Optional.ofNullable(vattuMap.get(item.getChungLoaiHh())).map(QlnvDmVattu::getTen).orElse(null));
			});

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

		Set<String> maChungLoaiHHList = thongTinTaiSanList.stream().map(BhQdPheDuyetKhBdgThongTinTaiSan::getChungLoaiHh).collect(Collectors.toSet());
		Set<QlnvDmVattu> dmVattuList = dmVattuRepository.findByMaIn(maChungLoaiHHList);

		Map<String, QlnvDmVattu> vattuMap = dmVattuList.stream().collect(Collectors.toMap(QlnvDmVattu::getMa, Function.identity(), (ex1, ex2) -> ex1));


		Map<Long, List<BhQdPheDuyetKhBdgThongTinTaiSan>> taiSanMap = thongTinTaiSanList.stream()
				.collect(groupingBy(BhQdPheDuyetKhBdgThongTinTaiSan::getBhDgKehoachId));

		List<BhQdPheDuyetKhbdgCtResponse> responseList = qdPheDuyetKhbdgCtList.stream().map(entry -> {
			BhQdPheDuyetKhbdgCtResponse chiTiet = chiTietResponseMapper.toDto(entry);
			//Tên đon vị
			if (chiTiet.getMaDonVi() != null) {
				chiTiet.setTenDonVi(this.getMapTenDvi().get(chiTiet.getMaDonVi()));
			}
			if (taiSanMap.get(entry.getBhDgKeHoachId()) == null) return chiTiet;
			//Build thông tin tài sản
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanList = Optional.ofNullable(taiSanMap.get(entry.getBhDgKeHoachId())).map(ts -> {
				List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> entryList = thongTinTaiSanResponseMapper.toDto(ts);
				entryList.forEach(item -> item.setTenChungLoaiHh(Optional.ofNullable(vattuMap.get(item.getChungLoaiHh())).map(QlnvDmVattu::getTen).orElse(null)));
				return entryList;
			}).orElse(null);

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

		if (CollectionUtils.isEmpty(bhDgKeHoachIdList)) return responseList;

		List<KeHoachBanDauGia> keHoachBanDauGiaList = keHoachBanDauGiaRepository.findByIdIn(bhDgKeHoachIdList);
		//key= id, vlue = kế hoạch
		Map<Long, KeHoachBanDauGia> keHoachBdgMap = keHoachBanDauGiaList.stream().collect(Collectors.toMap(KeHoachBanDauGia::getId, Function.identity(),
				(existing, replacement) -> existing));

		List<BanDauGiaPhanLoTaiSan> phanLoTaiSanList = phanLoTaiSanRepository.findByBhDgKehoachIdIn(bhDgKeHoachIdList);

		List<BhQdPheDuyetKhBdgThongTinTaiSan> qdPheDuyetKhBdgThongTinTaiSanList = taiSanBdgMapper.toEntity(phanLoTaiSanList);

		List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> taiSanResponseList = thongTinTaiSanResponseMapper.toDto(qdPheDuyetKhBdgThongTinTaiSanList);

		log.info("Group tài sản theo bhKeHoachBdgId để trả về trong response");
		//key = bhKeHoachBdgId, value = List BhQdPheDuyetKhBdgThongTinTaiSanResponse;
		Map<Long, List<BhQdPheDuyetKhBdgThongTinTaiSanResponse>> taiSanMap = taiSanResponseList.stream()
				.collect(groupingBy(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getBhDgKehoachId));

		responseList = responseList.stream().filter(entry -> keHoachBdgMap.get(entry.getBhDgKeHoachId()) != null).collect(Collectors.toList());

		for (BhQdPheDuyetKhbdgCtResponse entry : responseList) {
			List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> res = taiSanMap.get(entry.getBhDgKeHoachId());
			this.buildThongTinKho(res);
			entry.setThongTinTaiSans(res);
			entry.setSoKeHoach(keHoachBdgMap.get(entry.getBhDgKeHoachId()).getSoKeHoach());
		}

		return responseList;
	}

	@Override
	@Transactional
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
		Optional<BhTongHopDeXuatKhbdg> bhTongHopDeXuatKhbdg=  tongHopDeXuatKhbdgRepository.findById(theEntity.getTongHopDeXuatKhbdgId());
		if(bhTongHopDeXuatKhbdg.isPresent()){
			bhTongHopDeXuatKhbdg.get().setTrangThai(Contains.DABANHANH_QD);
			tongHopDeXuatKhbdgRepository.save(bhTongHopDeXuatKhbdg.get());
		}
		return true;
	}

	public void buildThongTinKho(List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> responses) {
		if (CollectionUtils.isEmpty(responses)) return;
		List<String> maLoKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaLoKho).collect(Collectors.toList());
		List<String> maNhaKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaNhaKho).collect(Collectors.toList());
		List<String> maDiemKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaDiemKho).collect(Collectors.toList());
		Set<String> maChiCucList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaChiCuc).collect(Collectors.toSet());
		Set<String> maNganKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaNganKho).collect(Collectors.toSet());


		Map<String, KtNganLo> mapNganLo = ktNganLoRepository.findByMaNganloIn(maLoKhoList)
				.stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));

		Map<String, KtDiemKho> mapDiemKho = ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhoList)
				.stream().collect(Collectors.toMap(KtDiemKho::getMaDiemkho, Function.identity()));

		Map<String, KtNhaKho> mapNhaKho = ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhoList)
				.stream().collect(Collectors.toMap(KtNhaKho::getMaNhakho, Function.identity()));

		Map<String, QlnvDmDonvi> mapChiCuc = dmDonviRepository.findByMaDviIn(maChiCucList)
				.stream().collect(Collectors.toMap(QlnvDmDonvi::getMaDvi, Function.identity()));

		Map<String, KtNganKho> mapNganKho = ktNganKhoRepository.findByMaNgankhoIn(maNganKhoList)
				.stream().collect(Collectors.toMap(KtNganKho::getMaNgankho, Function.identity()));

		for (BhQdPheDuyetKhBdgThongTinTaiSanResponse item : responses) {
			KtNganLo nganLo = mapNganLo.get(item.getMaLoKho());
			KtNhaKho nhaKho = mapNhaKho.get(item.getMaNhaKho());
			KtDiemKho diemKho = mapDiemKho.get(item.getMaDiemKho());
			QlnvDmDonvi chiCuc = mapChiCuc.get(item.getMaChiCuc());
			KtNganKho nganKho = mapNganKho.get(item.getMaNganKho());
			if (nganLo != null) item.setTenLoKho(nganLo.getTenNganlo());
			if (nhaKho != null) item.setTenNhaKho(nhaKho.getTenNhakho());
			if (diemKho != null) item.setTenDiemKho(diemKho.getTenDiemkho());
			if (chiCuc != null) item.setTenChiCuc(chiCuc.getTenDvi());
			if (nganKho != null) item.setTenNganKho(nganKho.getTenNgankho());
		}
	}
}
