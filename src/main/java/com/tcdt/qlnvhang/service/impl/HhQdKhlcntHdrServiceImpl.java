package com.tcdt.qlnvhang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.tcdt.qlnvhang.repository.HhDxKhLcntThopHdrRepository;
import com.tcdt.qlnvhang.table.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;
import com.tcdt.qlnvhang.repository.HhPaKhlcntHdrRepository;
import com.tcdt.qlnvhang.repository.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDtlReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.secification.HhQdKhlcntSpecification;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhQdKhlcntHdrServiceImpl extends BaseServiceImpl implements HhQdKhlcntHdrService {

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhPaKhlcntHdrRepository hhPaKhlcntHdrRepository;

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;


	@Override
	public HhQdKhlcntHdr create(HhQdKhlcntHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
		if (checkSoQd.isPresent()) {
			throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
		}
//
//		if (ObjectUtils.isEmpty(objReq.getIdPaHdr()) || objReq.getIdPaHdr() <= 0) {
//			throw new Exception("Không tìm thấy phương án kế hoạch lựa chọn nhà thầu");
//		}

		Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
		if (!qOptional.isPresent()){
			throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
		}

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();
		if (objReq.getHthucLcnt() == null || !mapDmuc.containsKey(objReq.getHthucLcnt())){
			throw new Exception("Hình thức lựa chọn nhà thầu không phù hợp");
		}

		if (objReq.getPthucLcnt() == null || !mapDmuc.containsKey(objReq.getPthucLcnt())){
			throw new Exception("Phương thức đấu thầu không phù hợp");
		}

		if (objReq.getLoaiHdong() == null || !mapDmuc.containsKey(objReq.getLoaiHdong())){
			throw new Exception("Loại hợp đồng không phù hợp");
		}

		if (objReq.getNguonVon() == null || !mapDmuc.containsKey(objReq.getNguonVon())){
			throw new Exception("Nguồn vốn không phù hợp");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinQdKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinQdKhlcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdKhlcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhQdKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);

		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setChildren(fileDinhKemList);
		dataMap.setIdThHdr(qOptional.get().getId());
//		dataMap.setSoPhAn(qOptional.get().getSoPhAn());

		if (objReq.getDetail() != null) {
			List<HhQdKhlcntDsgthau> detailChild;
			List<HhQdKhlcntDtlReq> dtlReqList = objReq.getDetail();
			for (HhQdKhlcntDtlReq dtlReq : dtlReqList) {
				HhQdKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhQdKhlcntDtl.class);
				detailChild = new ArrayList<HhQdKhlcntDsgthau>();
				if (dtlReq.getDetail() != null)
					detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhQdKhlcntDsgthau.class);
				UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
				detail.setChildren(detailChild);
				dataMap.addChild1(detail);
			}
		}

		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataMap);

		return createCheck;
	}

	@Override
	public HhQdKhlcntHdr update(HhQdKhlcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (!qOptional.get().getSoQd().equals(objReq.getSoQd())) {
			Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
			if (checkSoQd.isPresent())
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
		}

		if (ObjectUtils.isEmpty(objReq.getIdPaHdr()) || objReq.getIdPaHdr() <= 0)
			throw new Exception("Không tìm thấy phương án kế hoạch lựa chọn nhà thầu");

		Optional<HhPaKhlcntHdr> qOpPa = hhPaKhlcntHdrRepository.findById(objReq.getIdPaHdr());
		if (!qOpPa.isPresent())
			throw new Exception("Không tìm thấy phương án kế hoạch lựa chọn nhà thầu");

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();
		if (objReq.getHthucLcnt() == null || !mapDmuc.containsKey(objReq.getHthucLcnt()))
			throw new Exception("Hình thức lựa chọn nhà thầu không phù hợp");

		if (objReq.getPthucLcnt() == null || !mapDmuc.containsKey(objReq.getPthucLcnt()))
			throw new Exception("Phương thức đấu thầu không phù hợp");

		if (objReq.getLoaiHdong() == null || !mapDmuc.containsKey(objReq.getLoaiHdong()))
			throw new Exception("Loại hợp đồng không phù hợp");

		if (objReq.getNguonVon() == null || !mapDmuc.containsKey(objReq.getNguonVon()))
			throw new Exception("Nguồn vốn không phù hợp");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinQdKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinQdKhlcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdKhlcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhQdKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhQdKhlcntHdr dataDB = qOptional.get();
		HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setChildren(fileDinhKemList);
		dataDB.setSoPhAn(qOpPa.get().getSoPhAn());

		if (objReq.getDetail() != null) {
			List<HhQdKhlcntDsgthau> detailChild;
			List<HhQdKhlcntDtlReq> dtlReqList = objReq.getDetail();
			for (HhQdKhlcntDtlReq dtlReq : dtlReqList) {
				HhQdKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhQdKhlcntDtl.class);
				detailChild = new ArrayList<HhQdKhlcntDsgthau>();
				if (dtlReq.getDetail() != null)
					detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhQdKhlcntDsgthau.class);
				UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
				detail.setChildren(detailChild);
				dataDB.addChild1(detail);
			}
		}

		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);

		return createCheck;
	}

	@Override
	public HhQdKhlcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhQdKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhQdKhlcntDtl.class);
		for (HhQdKhlcntDtl dtl : dtls2) {
			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
		}

		return qOptional.get();
	}

	@Override
	public HhQdKhlcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(Long.valueOf(stReq.getId()));
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

		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(optional.get());
		if (createCheck.getIdPaHdr() > 0 && optional.get().getTrangThai().equals(Contains.DUYET)) {
			hhPaKhlcntHdrRepository.updateTongHop(createCheck.getIdPaHdr(), Contains.ACTIVE);
		}
		return createCheck;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");

		hhQdKhlcntHdrRepository.delete(optional.get());

	}

	@Override
	public HhQdKhlcntHdr detailNumber(String soQd) throws Exception {
		if (StringUtils.isEmpty(soQd))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findBySoQd(soQd);

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhQdKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhQdKhlcntDtl.class);
		for (HhQdKhlcntDtl dtl : dtls2) {
			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
		}
		return qOptional.get();
	}

	@Override
	public Page<HhQdKhlcntHdr> colection(HhQdKhlcntSearchReq objReq) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhQdKhlcntHdr> dataPage = hhQdKhlcntHdrRepository.findAll(HhQdKhlcntSpecification.buildSearchQuery(objReq),
				pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		for (HhQdKhlcntHdr hdr : dataPage.getContent()) {
			hdr.setHthucLcnt(mapDmuc.get(hdr.getHthucLcnt()));
			hdr.setPthucLcnt(mapDmuc.get(hdr.getPthucLcnt()));
			hdr.setLoaiHdong(mapDmuc.get(hdr.getLoaiHdong()));
			hdr.setNguonVon(mapDmuc.get(hdr.getNguonVon()));
		}

		return dataPage;
	}

	@Override
	public void exportToExcel(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception {
		// Tao form excel
		String title = "Danh sách QĐ phê duyệt KHLCNT";
		String[] rowsName = new String[] { "STT", "Số quyết định", "Ngày QĐ", "Về việc", "Số QĐ giao chỉ tiêu",
				"Loại hàng DTQG", "Tên loại hàng", "Tiêu chuẩn chất lượng", "Nguồn vốn", "Trạng thái" };
		List<HhQdKhlcntHdr> dsgtDtls = hhQdKhlcntHdrRepository
				.findAll(HhQdKhlcntSpecification.buildSearchQuery(searchReq));

		if (dsgtDtls.isEmpty())
			throw new UnsupportedOperationException("Không tìm thấy dữ liệu");

		String filename = "Quyetdinhkehoachlcnt.xlsx";

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < dsgtDtls.size(); i++) {
			HhQdKhlcntHdr dsgtDtl = dsgtDtls.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dsgtDtl.getSoQd();
			objs[2] = convertDateToString(dsgtDtl.getNgayQd());
			objs[3] = dsgtDtl.getVeViec();
			objs[4] = "01/QD-TCDT";// TODO: lam min lai
			objs[5] = mapDmuc.get(dsgtDtl.getLoaiVthh());
			objs[6] = "Tiêu chuẩn chất lượng";
			objs[7] = mapDmuc.get(dsgtDtl.getNguonVon());
			objs[7] = mapDmuc.get(dsgtDtl.getTrangThai());

			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<HhQdKhlcntHdr> getAllPage(HhQdKhlcntSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		return hhQdKhlcntHdrRepository.selectPage(req.getNamKhoach(),req.getLoaiVthh(), req.getSoQd(), convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()), pageable);
	}

	@Override
	public List<HhQdKhlcntHdr> getAll(HhQdKhlcntSearchReq req) throws Exception {
		return hhQdKhlcntHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(), req.getSoQd(), convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()));
	}

}
