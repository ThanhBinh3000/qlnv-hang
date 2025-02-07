package com.tcdt.qlnvhang.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinPhuLuc;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhPhuLucHdRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.object.HhHopDongDtlReq;
import com.tcdt.qlnvhang.request.object.HhPhuLucHdReq;
import com.tcdt.qlnvhang.request.search.HhPhuLucHdSearchReq;
import com.tcdt.qlnvhang.secification.HhPhuLucHdSpecification;
import com.tcdt.qlnvhang.service.HhPhuLucHdService;

@Service
public class HhPhuLucHdServiceImpl extends BaseServiceImpl implements HhPhuLucHdService {
	@Autowired
	private HhPhuLucHdRepository hhPhuLucHdRepository;

	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private HttpServletRequest req;
	@Autowired
	private FileDinhKemService fileDinhKemService;

	@Override
	public HhPhuLucHd create(HhPhuLucHdReq objReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (objReq.getLoaiVthh() == null)
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
		if (!qOpHdong.isPresent())
			throw new Exception("Hợp đồng số " + objReq.getSoHd() + " không tồn tại");

		Optional<HhPhuLucHd> qOpPluc = hhPhuLucHdRepository.findBySoPluc(objReq.getSoPluc());
		if (qOpPluc.isPresent())
			throw new Exception("Phụ lục số " + objReq.getSoPluc() + " đã tồn tại");

		HhPhuLucHd dataMap = ObjectMapperUtils.map(objReq, HhPhuLucHd.class);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setMaDvi(userInfo.getDvql());

		// Add thong tin dieu chinh dia diem nhap
//		List<HhHopDongDtlReq> dtlReqList = objReq.getDetail();
//		List<HhPhuLucHdDtl> details = new ArrayList<>();
//		if (dtlReqList != null) {
//			List<HhDdiemNhapKhoPluc> detailChild;
//			for (HhHopDongDtlReq dtlReq : dtlReqList) {
//				List<HhDdiemNhapKhoReq> cTietReq = dtlReq.getDetail();
//				HhPhuLucHdDtl detail = ObjectMapperUtils.map(dtlReq, HhPhuLucHdDtl.class);
//				detail.setType(Contains.PHU_LUC);
//				detailChild = new ArrayList<HhDdiemNhapKhoPluc>();
//				if (cTietReq != null)
//					detailChild = ObjectMapperUtils.mapAll(cTietReq, HhDdiemNhapKhoPluc.class);
//				detailChild.forEach(f -> {
//					f.setType(Contains.PHU_LUC);
//				});
//
//				detail.setChildren(detailChild);
//				details.add(detail);
//			}
//			dataMap.setChildren(details);
//		}

		// File dinh kem cua phu luc
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), HhPhuLucHd.TABLE_NAME);
		}
		return hhPhuLucHdRepository.save(dataMap);
	}

	@Override
	public HhPhuLucHd update(HhPhuLucHdReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null)
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhPhuLucHd> qOptional = hhPhuLucHdRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (!qOptional.get().getSoHd().equals(objReq.getSoHd())) {
			Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
			if (!qOpHdong.isPresent())
				throw new Exception("Hợp đồng số " + objReq.getSoHd() + " không tồn tại");
		}

		if (!qOptional.get().getSoPluc().equals(objReq.getSoPluc())) {
			Optional<HhPhuLucHd> qOpPluc = hhPhuLucHdRepository.findBySoPluc(objReq.getSoPluc());
			if (qOpPluc.isPresent())
				throw new Exception("Phụ lục số " + objReq.getSoPluc() + " đã tồn tại");
		}

		HhPhuLucHd dataDB = qOptional.get();
		HhPhuLucHd dataMap = ObjectMapperUtils.map(objReq, HhPhuLucHd.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());

//		// Add thong tin dieu chinh dia diem nhap
//		List<HhHopDongDtlReq> dtlReqList = objReq.getDetail();
//		List<HhPhuLucHdDtl> details = new ArrayList<>();
//		if (dtlReqList != null) {
//			List<HhDdiemNhapKhoPluc> detailChild;
//			for (HhHopDongDtlReq dtlReq : dtlReqList) {
//				List<HhDdiemNhapKhoReq> cTietReq = dtlReq.getDetail();
//				HhPhuLucHdDtl detail = ObjectMapperUtils.map(dtlReq, HhPhuLucHdDtl.class);
//				detail.setType(Contains.PHU_LUC);
//				detailChild = new ArrayList<HhDdiemNhapKhoPluc>();
//				if (cTietReq != null)
//					detailChild = ObjectMapperUtils.mapAll(cTietReq, HhDdiemNhapKhoPluc.class);
//				detailChild.forEach(f -> {
//					f.setType(Contains.PHU_LUC);
//				});
//
//				detail.setChildren(detailChild);
//				details.add(detail);
//			}
////			dataDB.setChildren(details);
//		}

		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), HhPhuLucHd.TABLE_NAME);
		}

		return hhPhuLucHdRepository.save(dataDB);
	}

	@Override
	public HhPhuLucHd detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhPhuLucHd> qOptional = hhPhuLucHdRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		List<FileDinhKem> fileDinhKem = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhPhuLucHd.TABLE_NAME));
		qOptional.get().setFileDinhKems(fileDinhKem);

		return qOptional.get();
	}

	@Override
	public HhPhuLucHd approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhPhuLucHd> optional = hhPhuLucHdRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		/*String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
		case Contains.CHO_DUYET + Contains.MOI_TAO:
			optional.get().setNguoiGuiDuyet(getUser().getUsername());
			optional.get().setNgayGuiDuyet(getDateTimeNow());
			break;
		case Contains.TU_CHOI + Contains.CHO_DUYET:
			optional.get().setNguoiPduyet(getUser().getUsername());
			optional.get().setNgayPduyet(getDateTimeNow());
			optional.get().setLdoTuchoi(stReq.getLyDo());
			break;
		case Contains.DUYET + Contains.CHO_DUYET:
			optional.get().setNguoiPduyet(getUser().getUsername());
			optional.get().setNgayPduyet(getDateTimeNow());
			break;
		default:
			throw new Exception("Phê duyệt không thành công");
		}*/

		optional.get().setTrangThai(stReq.getTrangThai());
		return hhPhuLucHdRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhPhuLucHd> optional = hhPhuLucHdRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa phụ lục hợp đồng ở trạng thái bản nháp hoặc từ chối");

		hhPhuLucHdRepository.delete(optional.get());

	}

	@Override
	public List<HhPhuLucHd> findBySoHd(HhPhuLucHdSearchReq objReq) throws Exception {
		List<HhPhuLucHd> plucList = hhPhuLucHdRepository.findAll(HhPhuLucHdSpecification.buildSearchQuery(objReq));
		return plucList;
	}

	@Override
	public Page<HhPhuLucHd> colection(HhPhuLucHdSearchReq objReq) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

		Page<HhPhuLucHd> dataPage = hhPhuLucHdRepository.findAll(HhPhuLucHdSpecification.buildSearchQuery(objReq),
				pageable);

		return dataPage;
	}

}
