package com.tcdt.qlnvhang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinPhuLuc;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.HhPhuLucHdRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.object.HhPhuLucHdReq;
import com.tcdt.qlnvhang.request.search.HhPhuLucHdSearchReq;
import com.tcdt.qlnvhang.secification.HhPhuLucHdSpecification;
import com.tcdt.qlnvhang.service.HhPhuLucHdService;
import com.tcdt.qlnvhang.table.HhDdiemNhapKhoPluc;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhPhuLucHd;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;

@Service
public class HhPhuLucHdServiceImpl extends BaseServiceImpl implements HhPhuLucHdService {
	@Autowired
	private HhPhuLucHdRepository hhPhuLucHdRepository;

	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public HhPhuLucHd create(HhPhuLucHdReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
		if (!qOpHdong.isPresent())
			throw new Exception("Hợp đồng số " + objReq.getSoHd() + " không tồn tại");

		Optional<HhPhuLucHd> qOpPluc = hhPhuLucHdRepository.findBySoPluc(objReq.getSoPluc());
		if (qOpPluc.isPresent())
			throw new Exception("Phụ lục số " + objReq.getSoPluc() + " đã tồn tại");

		HhPhuLucHd dataMap = ObjectMapperUtils.map(objReq, HhPhuLucHd.class);
		dataMap.setTuNgayTrcDc(qOpHdong.get().getTuNgayHluc());
		dataMap.setDenNgayTrcDc(qOpHdong.get().getDenNgayHluc());
		dataMap.setSoNgayTrcDc(minusDate(qOpHdong.get().getTuNgayHluc(), qOpHdong.get().getDenNgayHluc()));
		dataMap.setSoNgaySauDc(minusDate(objReq.getTuNgaySauDc(), objReq.getDenNgaySauDc()));

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setMaDvi(getDvql(req));

		// Add thong tin dieu chinh dia diem nhap
		List<HhDdiemNhapKhoReq> dtlReqList = objReq.getDetail();
		if (dtlReqList != null) {
			List<HhDdiemNhapKhoPluc> dtls1 = ObjectMapperUtils.mapAll(dtlReqList, HhDdiemNhapKhoPluc.class);
			dtls1.forEach(f -> {
				f.setType(Contains.PHU_LUC);
			});
			dataMap.setChildren(dtls1);
		}

		// File dinh kem cua phu luc
		List<FileDKemJoinPhuLuc> dtls1 = new ArrayList<FileDKemJoinPhuLuc>();
		if (objReq.getFileDinhKems() != null) {
			dtls1 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinPhuLuc.class);
			dtls1.forEach(f -> {
				f.setDataType(HhPhuLucHd.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataMap.setChildren1(dtls1);

		return hhPhuLucHdRepository.save(dataMap);
	}

	@Override
	public HhPhuLucHd update(HhPhuLucHdReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
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

		// Add thong tin dieu chinh dia diem nhap
		List<HhDdiemNhapKhoReq> dtlReqList = objReq.getDetail();
		if (dtlReqList != null) {
			List<HhDdiemNhapKhoPluc> dtls1 = ObjectMapperUtils.mapAll(dtlReqList, HhDdiemNhapKhoPluc.class);
			dtls1.forEach(f -> {
				f.setType(Contains.PHU_LUC);
			});
			dataDB.setChildren(dtls1);
		}

		// File dinh kem cua phu luc
		List<FileDKemJoinPhuLuc> dtls1 = new ArrayList<FileDKemJoinPhuLuc>();
		if (objReq.getFileDinhKems() != null) {
			dtls1 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinPhuLuc.class);
			dtls1.forEach(f -> {
				f.setDataType(HhPhuLucHd.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataDB.setChildren1(dtls1);

		return hhPhuLucHdRepository.save(dataDB);
	}

	@Override
	public HhPhuLucHd detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhPhuLucHd> qOptional = hhPhuLucHdRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		return qOptional.get();
	}

	@Override
	public HhPhuLucHd approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhPhuLucHd> optional = hhPhuLucHdRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		String status = stReq.getTrangThai() + optional.get().getTrangThai();
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
		}

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
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhPhuLucHd> dataPage = hhPhuLucHdRepository.findAll(HhPhuLucHdSpecification.buildSearchQuery(objReq),
				pageable);

		return dataPage;
	}

}
