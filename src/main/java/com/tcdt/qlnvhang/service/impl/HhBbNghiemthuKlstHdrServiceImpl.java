package com.tcdt.qlnvhang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinKeLot;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.request.search.HhBbNghiemthuKlstSearchReq;
import com.tcdt.qlnvhang.secification.HhBbNghiemthuKlstSpecification;
import com.tcdt.qlnvhang.service.HhBbNghiemthuKlstHdrService;
import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstDtl;
import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhBbNghiemthuKlstHdrServiceImpl extends BaseServiceImpl implements HhBbNghiemthuKlstHdrService {

	@Autowired
	private HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;

	@Override
	public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findBySoBb(objReq.getSoBb());
		if (qOptional.isPresent())
			throw new Exception("Số biên bản " + objReq.getSoBb() + " đã tồn tại");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhBbNghiemthuKlstHdr dataMap = new ModelMapper().map(objReq, HhBbNghiemthuKlstHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setChildren1(fileDinhKemList);

		// Add thong tin chung
		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
		dataMap.setChildren(dtls1);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		return hhBbNghiemthuKlstRepository.save(dataMap);

	}

	@Override
	public HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		if (!qOptional.get().getSoBb().equals(objReq.getSoBb())) {
			Optional<HhBbNghiemthuKlstHdr> qOpBban = hhBbNghiemthuKlstRepository.findBySoBb(objReq.getSoBb());
			if (qOpBban.isPresent())
				throw new Exception("Số biên bản " + objReq.getSoBb() + " đã tồn tại");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhBbNghiemthuKlstHdr dataDTB = qOptional.get();
		HhBbNghiemthuKlstHdr dataMap = ObjectMapperUtils.map(objReq, HhBbNghiemthuKlstHdr.class);

		updateObjectToObject(dataDTB, dataMap);

		dataDTB.setNgaySua(getDateTimeNow());
		dataDTB.setNguoiSua(getUser().getUsername());
		dataDTB.setChildren1(fileDinhKemList);

		// Add thong tin chung
		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
		dataDTB.setChildren(dtls1);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		return hhBbNghiemthuKlstRepository.save(dataDTB);

	}

	@Override
	public HhBbNghiemthuKlstHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhBbNghiemthuKlstDtl> dtls = ObjectMapperUtils.mapAll(qOptional.get().getChildren(),
				HhBbNghiemthuKlstDtl.class);
		UnitScaler.formatList(dtls, Contains.DVT_TAN);

		return qOptional.get();
	}

	@Override
	public Page<HhBbNghiemthuKlstHdr> colection(HhBbNghiemthuKlstSearchReq objReq, HttpServletRequest req)
			throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit);

		Page<HhBbNghiemthuKlstHdr> qhKho = hhBbNghiemthuKlstRepository
				.findAll(HhBbNghiemthuKlstSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapDmucDvi();
		for (HhBbNghiemthuKlstHdr hdr : qhKho.getContent()) {
			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
		}

		return qhKho;
	}

	@Override
	public HhBbNghiemthuKlstHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(stReq.getId()));
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
		return hhBbNghiemthuKlstRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(idSearchReq.getId());

		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				|| !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa với biên bản ở trạng thái bản nháp hoặc từ chối");

		hhBbNghiemthuKlstRepository.delete(optional.get());
	}

	@Override
	public void exportToExcel(IdSearchReq searchReq, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

}
