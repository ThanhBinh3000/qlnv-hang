package com.tcdt.qlnvhang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.tcdt.qlnvhang.repository.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinPaKhlcntHdr;
import com.tcdt.qlnvhang.repository.HhDxKhLcntThopHdrRepository;
import com.tcdt.qlnvhang.repository.HhPaKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhPaKhlcntDtlReq;
import com.tcdt.qlnvhang.request.object.HhPaKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhPaKhLcntDsChuaQdReq;
import com.tcdt.qlnvhang.request.search.HhPaKhlcntSearchReq;
import com.tcdt.qlnvhang.secification.HhPaKhlcntSpecification;
import com.tcdt.qlnvhang.service.HhPaKhlcntHdrService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhPaKhlcntHdrServiceImpl extends BaseServiceImpl implements HhPaKhlcntHdrService {

	@Autowired
	private HhPaKhlcntHdrRepository hhPaKhlcntHdrRepository;

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Override
	public HhPaKhlcntHdr create(HhPaKhlcntHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
		if (!qOptional.isPresent())
			throw new Exception("Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu không tồn tại");

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
		List<FileDKemJoinPaKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinPaKhlcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinPaKhlcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhPaKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhPaKhlcntHdr.class);

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setChildren(fileDinhKemList);

		if (objReq.getDetail() != null) {
			List<HhPaKhlcntDsgthau> detailChild;
			List<HhPaKhlcntDtlReq> dtlReqList = objReq.getDetail();
			for (HhPaKhlcntDtlReq dtlReq : dtlReqList) {
				HhPaKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhPaKhlcntDtl.class);
				detailChild = new ArrayList<HhPaKhlcntDsgthau>();
				if (dtlReq.getDetail() != null)
					detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhPaKhlcntDsgthau.class);
				UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
				detail.setChildren(detailChild);
				dataMap.addChild1(detail);
			}
		}

		HhPaKhlcntHdr createCheck = hhPaKhlcntHdrRepository.save(dataMap);
		if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
			hhDxKhLcntThopHdrRepository.updateTongHop(createCheck.getIdThHdr(), Contains.ACTIVE);
		}

		this.setDataQuyetDinh(createCheck);
		return createCheck;
	}

	@Override
	public HhPaKhlcntHdr update(HhPaKhlcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhPaKhlcntHdr> qOptional = hhPaKhlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

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
		List<FileDKemJoinPaKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinPaKhlcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinPaKhlcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		objReq.setIdThHdr(null);
		HhPaKhlcntHdr dataDB = qOptional.get();
		HhPaKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhPaKhlcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setChildren(fileDinhKemList);

		if (objReq.getDetail() != null) {
			List<HhPaKhlcntDsgthau> detailChild;
			List<HhPaKhlcntDtlReq> dtlReqList = objReq.getDetail();
			for (HhPaKhlcntDtlReq dtlReq : dtlReqList) {
				HhPaKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhPaKhlcntDtl.class);
				detailChild = new ArrayList<HhPaKhlcntDsgthau>();
				if (dtlReq.getDetail() != null)
					detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhPaKhlcntDsgthau.class);
				UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
				detail.setChildren(detailChild);
				dataDB.addChild1(detail);
			}
		}

		HhPaKhlcntHdr createCheck = hhPaKhlcntHdrRepository.save(dataDB);

		this.setDataQuyetDinh(createCheck);
		return createCheck;
	}

	@Override
	public HhPaKhlcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhPaKhlcntHdr> qOptional = hhPaKhlcntHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhPaKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhPaKhlcntDtl.class);
		for (HhPaKhlcntDtl dtl : dtls2) {
			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
		}

		HhPaKhlcntHdr hdrThop = qOptional.get();

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		hdrThop.setTenLoaiVthh(Contains.getLoaiVthh(hdrThop.getLoaiVthh()));
		hdrThop.setTenHthucLcnt(mapDmuc.get(hdrThop.getHthucLcnt()));
		hdrThop.setTenPthucLcnt(mapDmuc.get(hdrThop.getPthucLcnt()));
		hdrThop.setTenLoaiHdong(mapDmuc.get(hdrThop.getLoaiHdong()));
		hdrThop.setTenNguonVon(mapDmuc.get(hdrThop.getNguonVon()));

		this.setDataQuyetDinh(hdrThop);
		return hdrThop;
	}

	@Override
	public Page<HhPaKhlcntHdr> colection(HhPaKhlcntSearchReq objReq) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhPaKhlcntHdr> dataPage = hhPaKhlcntHdrRepository.findAll(HhPaKhlcntSpecification.buildSearchQuery(objReq),
				pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		for (HhPaKhlcntHdr hdr : dataPage.getContent()) {
			hdr.setTenHthucLcnt(mapDmuc.get(hdr.getHthucLcnt()));
			hdr.setTenPthucLcnt(mapDmuc.get(hdr.getPthucLcnt()));
			hdr.setTenLoaiHdong(mapDmuc.get(hdr.getLoaiHdong()));
			hdr.setTenNguonVon(mapDmuc.get(hdr.getNguonVon()));
			this.setDataQuyetDinh(hdr);
		}
		return dataPage;
	}

	@Override
	public List<HhPaKhlcntHdr> dsChuaQd(HhPaKhLcntDsChuaQdReq objReq) throws Exception {
		List<HhPaKhlcntHdr> panList = hhPaKhlcntHdrRepository.findAll(HhPaKhlcntSpecification.buildDsChuaQd(objReq));

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();
		for (HhPaKhlcntHdr hdr : panList) {
			hdr.setTenHthucLcnt(mapDmuc.get(hdr.getHthucLcnt()));
			hdr.setTenPthucLcnt(mapDmuc.get(hdr.getPthucLcnt()));
			hdr.setTenLoaiHdong(mapDmuc.get(hdr.getLoaiHdong()));
			hdr.setTenNguonVon(mapDmuc.get(hdr.getNguonVon()));
			this.setDataQuyetDinh(hdr);
		}
		return panList;
	}

	@Override
	public void delete(@Valid IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhPaKhlcntHdr> optional = hhPaKhlcntHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (optional.get().getQuyetDinh().equals(Contains.ACTIVE))
			throw new Exception("Phương án đã được lên quyết định, không được phép xóa");

		hhPaKhlcntHdrRepository.delete(optional.get());

	}

	@Override
	public void exportToExcel(HhPaKhlcntSearchReq objReq, HttpServletResponse response) throws Exception {
		// Tao form excel
		String title = "Danh sách gói thầu";
		String[] rowsName = new String[] { "STT", "Ngày tạo phương án", "Năm kế hoạch", "Hình thức LCNT",
				"Phương thức LCNT", "Loại hợp đồng", "Nguồn vốn", "Tiêu chuẩn chất lượng", "Trạng thái" };
		List<HhPaKhlcntHdr> paList = hhPaKhlcntHdrRepository.findAll(HhPaKhlcntSpecification.buildSearchQuery(objReq));

		if (paList.isEmpty())
			throw new UnsupportedOperationException("Không tìm thấy dữ liệu");

		String filename = "PHUONGAN_KHLCNT_" + getDateTimeNow() + ".xlsx";

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < paList.size(); i++) {
			HhPaKhlcntHdr hdr = paList.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = hdr.getNgayTao();
			objs[2] = hdr.getNamKhoach();
			objs[3] = mapDmuc.get(hdr.getHthucLcnt());
			objs[4] = mapDmuc.get(hdr.getPthucLcnt());
			objs[5] = mapDmuc.get(hdr.getLoaiHdong());
			objs[6] = mapDmuc.get(hdr.getNguonVon());
			objs[7] = "Tiêu chuẩn chất lượng";// TODO: chinh sua lai khi thong nhat duoc cach lay bo tieu chuan
			objs[8] = hdr.getQuyetDinh().equals(Contains.ACTIVE) ? "Đã được tạo quyết định"
					: "Chưa được tạo quyết định";
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	private void setDataQuyetDinh(HhPaKhlcntHdr pa) {
		if (Contains.ACTIVE.equalsIgnoreCase(pa.getQuyetDinh())) {
//			Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findByIdPaHdr(pa.getId());
//			optional.ifPresent(qd -> {
//				pa.setSoQd(qd.getSoQd());
//				pa.setQdId(qd.getId());
//			});
		}
	}

}
