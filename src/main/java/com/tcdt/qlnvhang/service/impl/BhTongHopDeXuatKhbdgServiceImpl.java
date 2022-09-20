package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCt;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCtRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCtResponseMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponseMapper;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCtRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExcelHeaderConst;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class BhTongHopDeXuatKhbdgServiceImpl extends BaseServiceImpl implements BhTongHopDeXuatKhbdgService {
	private final BhTongHopDeXuatKhbdgRepository deXuatKhbdgRepository;
	private final BhTongHopDeXuatCtRepository chiTietRepository;

	private final QlnvDmVattuRepository dmVattuRepository;

	private KeHoachBanDauGiaRepository keHoachBanDauGiaRepository;


	private static final String SHEET_NAME = "Danh sách tổng hợp đề xuất KHBDG";

	private final BhTongHopDeXuatKhbdgResponseMapper tongHopDeXuatKhbdgResponseMapper;
	private final BhTongHopDeXuatKhbdgRequestMapper tongHopDeXuatKhbdgRequestMapper;

	private final BhTongHopDeXuatCtResponseMapper chiTietResponseMapper;
	private final BhTongHopDeXuatCtRequestMapper chiTietRequestMapper;

	@Override
	public BhTongHopDeXuatKhbdgResponse create(BhTongHopDeXuatKhbdgRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		BhTongHopDeXuatKhbdg theEntity = tongHopDeXuatKhbdgRequestMapper.toEntity(req);
		theEntity.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		theEntity.setNgayTao(LocalDate.now());
		theEntity.setNguoiTaoId(userInfo.getId());
		theEntity.setMaDonVi(userInfo.getDvql());
		theEntity.setCapDonVi(userInfo.getCapDvi());
		theEntity = deXuatKhbdgRepository.save(theEntity);
		theEntity.setMaTongHop(theEntity.getId().toString());
		theEntity = deXuatKhbdgRepository.save(theEntity);

		log.debug("Create chi tiết");
		List<BhTongHopDeXuatCt> chiTietList = null;
		if (!CollectionUtils.isEmpty(req.getChiTietList())) {
			chiTietList = chiTietRequestMapper.toEntity(req.getChiTietList());

			for (BhTongHopDeXuatCt entry : chiTietList) {
				entry.setBhTongHopDeXuatId(theEntity.getId());
			}
			chiTietList = chiTietRepository.saveAll(chiTietList);
			theEntity.setChiTietList(chiTietList);
		}
		if(theEntity.getId() > 0 && theEntity.getChiTietList().size() > 0){
			List<String> soKhList = theEntity.getChiTietList().stream().map(BhTongHopDeXuatCt::getSoKeHoach)
					.collect(Collectors.toList());
			keHoachBanDauGiaRepository.updateTongHop(soKhList, Contains.DATONGHOP);
		}
		return tongHopDeXuatKhbdgResponseMapper.toDto(theEntity);
	}

	@Override
	public BhTongHopDeXuatKhbdgResponse update(BhTongHopDeXuatKhbdgRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<BhTongHopDeXuatKhbdg> optional = deXuatKhbdgRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Tổng hợp đề xuất ban dau gia");
		BhTongHopDeXuatKhbdg theEntity = optional.get();

		log.info("Update Tổng hợp đề xuất ban dau gia");
		tongHopDeXuatKhbdgRequestMapper.partialUpdate(theEntity, req);

		theEntity.setNgaySua(LocalDate.now());
		theEntity.setNguoiSuaId(userInfo.getId());
		theEntity = deXuatKhbdgRepository.save(theEntity);

		chiTietRepository.deleteAllByBhTongHopDeXuatId(theEntity.getId());

		List<BhTongHopDeXuatCt> chiTietList = null;
		if (!CollectionUtils.isEmpty(req.getChiTietList())) {
			chiTietList = chiTietRequestMapper.toEntity(req.getChiTietList());
			chiTietList = chiTietRepository.saveAll(chiTietList);
			theEntity.setChiTietList(chiTietList);
		}
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

		log.info("Delete info");
		chiTietRepository.deleteAllByBhTongHopDeXuatIdIn(ids);

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

	@Override
	public BhTongHopDeXuatKhbdgResponse detail(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");


		Optional<BhTongHopDeXuatKhbdg> optional = deXuatKhbdgRepository.findById(id);
		if (!optional.isPresent()) throw new Exception("Tổng hợp đề xuất kế hoạch bán đấu giá không tồn tại");
		BhTongHopDeXuatKhbdg deXuatKhbdg = optional.get();

		List<BhTongHopDeXuatCt> chiTietList = chiTietRepository.findByBhTongHopDeXuatId(deXuatKhbdg.getId());

		if (!CollectionUtils.isEmpty(chiTietList)) {
			deXuatKhbdg.setChiTietList(chiTietList);
		}

		BhTongHopDeXuatKhbdgResponse response = tongHopDeXuatKhbdgResponseMapper.toDto(deXuatKhbdg);

		QlnvDmVattu dmVattu = dmVattuRepository.findByMa(deXuatKhbdg.getLoaiVthh());
		if (dmVattu != null) {
			response.setTenVatTuCha(dmVattu.getTen());
		}
		if (CollectionUtils.isEmpty(response.getChiTietList())) return response;

		response.getChiTietList().forEach(entry -> {
			if (entry.getMaDonVi() == null) return;
			entry.setTenDonVi(this.getMapTenDvi().get(entry.getMaDonVi()));
		});

		return response;
	}
	@Override
	public boolean exportToExcel(BhTongHopDeXuatKhbdgSearchRequest req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<BhTongHopDeXuatKhbdgSearchResponse> list = this.search(req).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		String[] rowsName = new String[]{ExcelHeaderConst.STT,
				ExcelHeaderConst.NGAY_TONG_HOP,
				ExcelHeaderConst.NOI_DUNG_TONG_HOP,
				ExcelHeaderConst.NAM_KE_HOACH,
				ExcelHeaderConst.SO_QD_PHE_DUYET_KH_BDG,
				ExcelHeaderConst.LOAI_HANG_HOA,
				ExcelHeaderConst.TRANG_THAI};
		String filename = "tong_hop_de_xuat_kh_bdg.xlsx";

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

	public boolean updateStatusQd(StatusReq stReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Optional<BhTongHopDeXuatKhbdg> optional = deXuatKhbdgRepository.findById(stReq.getId());
		if (!optional.isPresent())
			throw new Exception("Biên bản bán đấu giá không tồn tại.");

		BhTongHopDeXuatKhbdg theEntity = optional.get();

		String trangThai = NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(stReq.getTrangThai());
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");

		theEntity.setTrangThai(stReq.getTrangThai());
		theEntity.setNguoiPduyetId(userInfo.getId());
		theEntity.setNgayPduyet(LocalDate.now());
		theEntity.setLyDoTuChoi(stReq.getLyDo());
		deXuatKhbdgRepository.save(theEntity);
		return true;
	}

	@Override
	public Page<BhTongHopDeXuatKhbdg> searchPage(HttpServletRequest request, BhTongHopDeXuatKhbdgSearchRequest req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		Page<BhTongHopDeXuatKhbdg> page = deXuatKhbdgRepository.select(
				req.getNamKeHoach(),
				req.getLoaiVthh(),
				req.getCloaiVthh(),
				convertDateToString(req.getNgayTongHopTuNgay()),
				convertDateToString(req.getNgayTongHopDenNgay()),
				req.getNoiDungTongHop(),
				req.getTrangThai(),
				pageable);
		List<Long>listId=page.getContent().stream().map(BhTongHopDeXuatKhbdg::getId).collect(Collectors.toList());
		List<BhTongHopDeXuatCt> tongHopDeXuatCtMap=chiTietRepository.findByBhTongHopDeXuatIdIn(listId);
		Map<Long, List<BhTongHopDeXuatCt>> mapChiTiet=tongHopDeXuatCtMap.stream()
				.collect(groupingBy(BhTongHopDeXuatCt::getBhTongHopDeXuatId));
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		page.getContent().forEach(f -> {
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setChiTietList(mapChiTiet.get(f.getId()));
		});
		return page;
	}
}
