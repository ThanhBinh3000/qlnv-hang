package com.tcdt.qlnvhang.service.impl;

import java.util.*;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.HhQdPduyetKqlcntDtlReq;
import com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcntHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdPduyetKqlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdPduyetKqlcntSearchReq;
import com.tcdt.qlnvhang.secification.HhQdPduyetKqlcntSpecification;
import com.tcdt.qlnvhang.service.HhQdPduyetKqlcntHdrService;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Service
public class HhQdPduyetKqlcntHdrServiceImpl extends BaseServiceImpl implements HhQdPduyetKqlcntHdrService {

	@Autowired
	private HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;

	@Autowired
	private HhQdPduyetKqlcntDtlRepository hhQdPduyetKqlcntDtlRepository;

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HhDthauRepository hhDthauRepository;

	@Override
	public HhQdPduyetKqlcntHdr create(HhQdPduyetKqlcntHdrReq objReq) throws Exception {
		// Is Vật Tư
		if (objReq.getLoaiVthh().startsWith("02")){
			return createVatTu(objReq);
		}else{
			return createLT(objReq);
		}
	}

	private HhQdPduyetKqlcntHdr createLT(HhQdPduyetKqlcntHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())){
			throw new Exception("Loại vật tư hàng hóa không phù hợp");
		}

		List<HhQdKhlcntHdr> checkSoCc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdPdKhlcnt());
		if (checkSoCc.isEmpty()){
			throw new Exception(
					"Số quyết định phê duyệt kế hoạch lựa chọn nhà thầu " + objReq.getSoQdPdKhlcnt() + " không tồn tại");
		}

		Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getSoQd());
		if (checkSoQd.isPresent()){
			throw new Exception(
					"Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getSoQd() + " đã tồn tại");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKquaLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinKquaLcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKquaLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhQdPduyetKqlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPduyetKqlcntHdr.class);

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setChildren(fileDinhKemList);

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(dataMap);

//		Optional<HhQdKhlcntDsgthau> gt = hhQdKhlcntDsgthauRepository.findById(objReq.getIdGoiThau());
//		HhQdPduyetKqlcntDtl dtl = ObjectMapperUtils.map(gt.get(), HhQdPduyetKqlcntDtl.class);
//		dtl.setId(null);
//		dtl.setIdQdPdHdr(dataMap.getId());
//		dtl.setIdGoiThau(objReq.getIdGoiThau());
//		hhQdPduyetKqlcntDtlRepository.save(dtl);

		for (HhQdPduyetKqlcntDtlReq qdPdKq : objReq.getDetailList()){
			HhQdPduyetKqlcntDtl qdPdKqDtl = ObjectMapperUtils.map(qdPdKq, HhQdPduyetKqlcntDtl.class);
			qdPdKqDtl.setId(null);
			qdPdKqDtl.setIdQdPdHdr(dataMap.getId());
			qdPdKqDtl.setIdGoiThau(qdPdKq.getIdGt());
			hhQdPduyetKqlcntDtlRepository.save(qdPdKqDtl);
		}

		return createCheck;
	}

	private HhQdPduyetKqlcntHdr createVatTu(HhQdPduyetKqlcntHdrReq objReq) throws Exception {

		List<HhQdKhlcntHdr> checkSoCc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdPdKhlcnt());
		if (checkSoCc.isEmpty()){
			throw new Exception(
					"Số quyết định phê duyệt kế hoạch lựa chọn nhà thầu " + objReq.getSoQdPdKhlcnt() + " không tồn tại");
		}

		Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getSoQd());
		if (checkSoQd.isPresent()){
			throw new Exception(
					"Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getSoQd() + " đã tồn tại");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKquaLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinKquaLcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKquaLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhQdPduyetKqlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPduyetKqlcntHdr.class);

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setMaDvi(getUser().getDvql());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setChildren(fileDinhKemList);

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(dataMap);

		for (HhQdPduyetKqlcntDtlReq qdPdKq : objReq.getDetailList()){
			HhQdPduyetKqlcntDtl qdPdKqDtl = ObjectMapperUtils.map(qdPdKq, HhQdPduyetKqlcntDtl.class);
			qdPdKqDtl.setId(null);
			qdPdKqDtl.setIdQdPdHdr(dataMap.getId());
			qdPdKqDtl.setIdGoiThau(qdPdKq.getIdGt());
			hhQdPduyetKqlcntDtlRepository.save(qdPdKqDtl);
		}

		return createCheck;
	}

	@Override
	public HhQdPduyetKqlcntHdr update(HhQdPduyetKqlcntHdrReq objReq) throws Exception {
		// Is Vật Tư
		if (objReq.getLoaiVthh().startsWith("02")){
			return updateVatTu(objReq);
		}else{
			return updateLT(objReq);
		}
	}

	private HhQdPduyetKqlcntHdr updateLT(HhQdPduyetKqlcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhQdPduyetKqlcntHdr> qOptional = hhQdPduyetKqlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

//		if (!qOptional.get().getSoQd().equals(objReq.getSoQd())) {
//			Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getCanCu());
//			if (!checkSoQd.isPresent())
//				throw new Exception(
//						"Số quyết định phê duyệt kế hoạch lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");
//		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKquaLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinKquaLcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKquaLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhQdPduyetKqlcntHdr dataDB = qOptional.get();
		HhQdPduyetKqlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPduyetKqlcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setChildren(fileDinhKemList);

		if (objReq.getDetailList() != null) {
			// Add danh sach goi thau
			List<HhQdPduyetKqlcntDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetailList(), HhQdPduyetKqlcntDtl.class);
			UnitScaler.reverseFormatList(dtls, Contains.DVT_TAN);
//			dataDB.setChildren1(dtls);
		}

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(dataDB);

		Optional<HhQdKhlcntDsgthau> gt = hhQdKhlcntDsgthauRepository.findById(objReq.getIdGoiThau());
		HhQdPduyetKqlcntDtl dtl = ObjectMapperUtils.map(gt.get(), HhQdPduyetKqlcntDtl.class);
		dtl.setId(null);
		dtl.setIdQdPdHdr(dataMap.getId());
		dtl.setIdGoiThau(objReq.getIdGoiThau());
		hhQdPduyetKqlcntDtlRepository.save(dtl);

		return createCheck;
	}

	private HhQdPduyetKqlcntHdr updateVatTu(HhQdPduyetKqlcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhQdPduyetKqlcntHdr> qOptional = hhQdPduyetKqlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinKquaLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinKquaLcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKquaLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhQdPduyetKqlcntHdr dataDB = qOptional.get();
		HhQdPduyetKqlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPduyetKqlcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setChildren(fileDinhKemList);

		if (objReq.getDetailList() != null) {
			// Add danh sach goi thau
			List<HhQdPduyetKqlcntDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetailList(), HhQdPduyetKqlcntDtl.class);
			UnitScaler.reverseFormatList(dtls, Contains.DVT_TAN);
//			dataDB.setChildren1(dtls);
		}

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(dataDB);

		return createCheck;
	}

	@Override
	public HhQdPduyetKqlcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdPduyetKqlcntHdr> qOptional = hhQdPduyetKqlcntHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();


		qOptional.get().setTenVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));

		qOptional.get().setHhQdPduyetKqlcntDtlList(hhQdPduyetKqlcntDtlRepository.findByIdQdPdHdr(Long.parseLong(ids)));

		// Quy doi don vi kg = tan
//		UnitScaler.formatList(qOptional.get().getChildren1(), Contains.DVT_TAN);

		return qOptional.get();
	}

	@Override
	public Page<HhQdPduyetKqlcntHdr> colection(HhQdPduyetKqlcntSearchReq objReq) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhQdPduyetKqlcntHdr> dataPage = hhQdPduyetKqlcntHdrRepository
				.findAll(HhQdPduyetKqlcntSpecification.buildSearchQuery(objReq), pageable);

		return dataPage;
	}

	@Override
	public HhQdPduyetKqlcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId())){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		Optional<HhQdPduyetKqlcntHdr> optional = hhQdPduyetKqlcntHdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
			case Contains.BAN_HANH + Contains.DUTHAO:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
		default:
			throw new Exception("Phê duyệt không thành công");
		}

		optional.get().setTrangThai(stReq.getTrangThai());
		if(stReq.getTrangThai().equals(Contains.BAN_HANH)){
			// IS VẬT TƯ
			if(optional.get().getLoaiVthh().startsWith("02")){
				List<HhQdPduyetKqlcntDtl> qdPdKqLcntList = hhQdPduyetKqlcntDtlRepository.findByIdQdPdHdr(optional.get().getId());
				for(HhQdPduyetKqlcntDtl kqLcnt : qdPdKqLcntList){
					hhQdKhlcntDsgthauRepository.updateGoiThau(kqLcnt.getIdGoiThau(),kqLcnt.getTrungThau() == 1 ? Contains.TRUNGTHAU : Contains.HUYTHAU,kqLcnt.getLyDoHuy());
				}
			}else{
				hhQdKhlcntDsgthauRepository.updateGoiThau(optional.get().getIdGoiThau(),optional.get().getTrungThau() == 1 ? Contains.TRUNGTHAU : Contains.HUYTHAU,optional.get().getLyDoHuy());
			}
		}

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(optional.get());

		return createCheck;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdPduyetKqlcntHdr> optional = hhQdPduyetKqlcntHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		}

		if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
			throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
		}

		hhQdPduyetKqlcntHdrRepository.delete(optional.get());
	}


	@Override
	public Page<HhQdPduyetKqlcntHdr> timKiemPage(HhQdPduyetKqlcntSearchReq req, HttpServletResponse response) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		return hhQdPduyetKqlcntHdrRepository.selectPage(req.getNamKhoach(),req.getLoaiVthh(),convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getSoQd(),req.getTrangThai(), pageable);

	}

	@Override
	public Page<HhQdPduyetKqlcntRes> timKiemPageCustom(HhQdPduyetKqlcntSearchReq req, HttpServletResponse response) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		String cDvi = getUser().getCapDvi();
		Page<HhQdPduyetKqlcntRes> page;
		if(Contains.CAP_TONG_CUC.equals(cDvi)){
			page = hhQdPduyetKqlcntHdrRepository.customQuerySearchTongCuc(req.getNamKhoach(),req.getLoaiVthh(),req.getTrichYeu(),pageable);
		}else{
			page = hhQdPduyetKqlcntHdrRepository.customQuerySearchCuc(req.getNamKhoach(),req.getLoaiVthh(),req.getTrichYeu(),pageable);
		}
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDviLquan = getListDanhMucDviLq("NT");
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> mapVthh = getListDanhMucHangHoa();

		page.forEach(f -> {
			f.setTenHdong(StringUtils.isEmpty(f.getLoaiHdong()) ? null : hashMapLoaiHdong.get(f.getLoaiHdong()));
			f.setTenNhaThau(StringUtils.isEmpty(f.getIdNhaThau()) ? null : hashMapDviLquan.get(String.valueOf(Double.parseDouble(f.getIdNhaThau().toString()))));
			f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
			f.setTenVthh(mapVthh.get(f.getLoaiVthh()));
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setSoGoiThau(hhQdPduyetKqlcntDtlRepository.countByIdQdPdHdr(f.getId()));
		});
		return page;
	}

	@Override
	public List<HhQdPduyetKqlcntHdr> timKiemAll(HhQdPduyetKqlcntSearchReq req) throws Exception {
		return hhQdPduyetKqlcntHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(),convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getSoQd(), req.getTrangThai());
	}

	@Override
	public void exportList(@Valid @RequestBody HhQdPduyetKqlcntSearchReq objReq, HttpServletResponse response) throws  Exception{
		String cDvi= getUser().getCapDvi();
		if(Contains.CAP_TONG_CUC.equals(cDvi)){
			this.exportToExcelTongCuc(objReq,response);
		}else {
			this.exportToExcelCuc(objReq,response);
		}

	}
	public void exportToExcelTongCuc(@Valid @RequestBody HhQdPduyetKqlcntSearchReq objReq, HttpServletResponse response) throws  Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		objReq.setPaggingReq(paggingReq);
		Page<HhQdPduyetKqlcntRes> page = this.timKiemPageCustom(objReq, response);
		List<HhQdPduyetKqlcntRes> data = page.getContent();

		String title = "Quyết định phê duyệt kết quả lựa chọn nhà thầu";
		String[] rowsName = new String[]{"STT", "Số QĐ", "Đơn vị", "Ngày QĐ", "Năm kế hoạch", "Trích yếu", "Số QD PD KHLCNT", "Loại hàng hóa", "Số gói thầu", "Trạng thái"};
		String fileName = "danh-sach-phe-duyet-tt-lcnt-tong-cuc.xlsx";
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdPduyetKqlcntRes dx = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dx.getSoQd();
			objs[2] = dx.getMaDvi();
			objs[3] = dx.getNgayQd();
			objs[4] = dx.getNamKhoach();
			objs[5] = dx.getTrichYeu();
			objs[6] = dx.getSoQdPdKhlcnt();
			objs[7] = dx.getLoaiVthh();
			objs[8] = dx.getSoGoiThau();
			objs[9] = dx.getTrangThai();
			dataList.add(objs);

		}
		ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
		ex.export();

	}
	public void exportToExcelCuc( @Valid @RequestBody HhQdPduyetKqlcntSearchReq objReq, HttpServletResponse response) throws  Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		objReq.setPaggingReq(paggingReq);
		Page<HhQdPduyetKqlcntRes> page = this.timKiemPageCustom(objReq, response);
		List<HhQdPduyetKqlcntRes> data = page.getContent();

		String title = "Quyết định phê duyệt kết quả lựa chọn nhà thầu";
		String[] rowsName = new String[]{"STT", "Số QĐ", "Ngày QĐ", "Trích yếu", "Tên gói thầu", "Trúng/hủy thầu", "Tên đơn vị trúng thầu", "Lý do hủy thầu", "Giá gói thầu (đồng)", "Loại hợp đồng", "Thời gian thực hiện hợp đồng (ngày)", "Trạng thái"};
		String fileName = "danh-sach-phe-duyet-tt-lcnt-cuc.xlsx";
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdPduyetKqlcntRes dx = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dx.getSoQd();
			objs[2] = dx.getNgayQd();
			objs[3] = dx.getTrichYeu();
			objs[4] = dx.getTenGthau();
			objs[5] = dx.getStatusGthau();
			objs[6] = dx.getIdNhaThau();
			objs[7] = dx.getLyDoHuy();
			objs[8] = dx.getDonGiaTrcVat();
			objs[9] = dx.getLoaiHdong();
			objs[10] = dx.getTgianThienHd();
			objs[11] = dx.getTrangThai();
			dataList.add(objs);

		}
		ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
		ex.export();

	}
	}
