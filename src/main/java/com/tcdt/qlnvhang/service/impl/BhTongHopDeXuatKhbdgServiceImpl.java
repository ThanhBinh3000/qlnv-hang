package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponseMapper;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgService;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class BhTongHopDeXuatKhbdgServiceImpl extends BaseServiceImpl implements BhTongHopDeXuatKhbdgService {
	private final BhTongHopDeXuatKhbdgRepository deXuatKhbdgRepository;

	private final QlnvDmVattuRepository dmVattuRepository;


	private static final String SHEET_NAME = "Danh sách tổng hợp đề xuất KHBDG";

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
		theEntity.setMaTongHop(UUID.randomUUID().toString());
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

	@Override
	public BhTongHopDeXuatKhbdgResponse detail(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");


		Optional<BhTongHopDeXuatKhbdg> optional = deXuatKhbdgRepository.findById(id);
		if (!optional.isPresent()) throw new Exception("Tổng hợp đề xuất kế hoạch bán đấu giá không tồn tại");
		BhTongHopDeXuatKhbdg deXuatKhbdg = optional.get();

		BhTongHopDeXuatKhbdgResponse response = tongHopDeXuatKhbdgResponseMapper.toDto(deXuatKhbdg);

		QlnvDmVattu dmVattu = dmVattuRepository.findByMa(deXuatKhbdg.getMaVatTuCha());
		if (dmVattu != null) {
			response.setTenVatTuCha(dmVattu.getTen());
		}
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
	@Override
	public BhTongHopDeXuatKhbdgResponse updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<BhTongHopDeXuatKhbdg> optional = deXuatKhbdgRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhTongHopDeXuatKhbdg tongHopDeXuatKhbdg = optional.get();
		//validate Trạng Thái
		String trangThai = TrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		tongHopDeXuatKhbdg.setTrangThai(trangThaiId);
		tongHopDeXuatKhbdg = deXuatKhbdgRepository.save(tongHopDeXuatKhbdg);
		return tongHopDeXuatKhbdgResponseMapper.toDto(tongHopDeXuatKhbdg);
	}
}
