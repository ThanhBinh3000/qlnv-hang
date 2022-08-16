package com.tcdt.qlnvhang.service.impl;


import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgRequestMapper;
import com.tcdt.qlnvhang.mapper.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgResponseMapper;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgSearchResponse;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.ExcelHeaderConst;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


	@Override
	public BhQdPheDuyetKhbdgResponse create(BhQdPheDuyetKhbdgRequest req) throws Exception {
		if (req == null) return null;

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

		return qdPheduyetKhbdgResponseMapper.toDto(theEntity);
	}

	@Override
	public BhQdPheDuyetKhbdgResponse update(BhQdPheDuyetKhbdgRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<BhQdPheDuyetKhbdg> optional = qdPheDuyetKhbdgRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhQdPheDuyetKhbdg theEntity = optional.get();

		log.info("Update ke hoach ban dau gia");
		qdPheduyetKhbdgRequestMapper.partialUpdate(theEntity, req);

		theEntity.setNgaySua(LocalDate.now());
		theEntity.setNguoiSuaId(userInfo.getId());
		theEntity = qdPheDuyetKhbdgRepository.save(theEntity);

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

		log.info("Delete file dinh kem");
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(BhQdPheDuyetKhbdg.TABLE_NAME));

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

		return response;
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
	public BhQdPheDuyetKhbdgResponse updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<BhQdPheDuyetKhbdg> optional = qdPheDuyetKhbdgRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Kế hoạch bán đấu giá không tồn tại");
		BhQdPheDuyetKhbdg qdPheduyetKhbdg = optional.get();
		//validate Trạng Thái
		String trangThai = NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		qdPheduyetKhbdg.setTrangThai(trangThaiId);
		qdPheduyetKhbdg = qdPheDuyetKhbdgRepository.save(qdPheduyetKhbdg);
		return qdPheduyetKhbdgResponseMapper.toDto(qdPheduyetKhbdg);
	}
}
