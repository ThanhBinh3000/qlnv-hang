package com.tcdt.qlnvhang.service.impl;

import java.util.*;

import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes;
import com.tcdt.qlnvhang.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcnt;
import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcntHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdPduyetKqlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdPduyetKqlcntSearchReq;
import com.tcdt.qlnvhang.secification.HhQdPduyetKqlcntSpecification;
import com.tcdt.qlnvhang.service.HhQdPduyetKqlcntHdrService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

import javax.servlet.http.HttpServletRequest;

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
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())){
			throw new Exception("Loại vật tư hàng hóa không phù hợp");
		}

		Optional<HhQdKhlcntHdr> checkSoCc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdPdKhlcnt());
		if (!checkSoCc.isPresent()){
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
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setChildren(fileDinhKemList);

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(dataMap);

		Optional<HhQdKhlcntDsgthau> gt =  hhQdKhlcntDsgthauRepository.findById(objReq.getIdGoiThau());
		HhQdPduyetKqlcntDtl dtl = ObjectMapperUtils.map(gt.get(), HhQdPduyetKqlcntDtl.class);
		dtl.setId(null);
		dtl.setIdQdPdHdr(dataMap.getId());
		dtl.setIdGoiThau(objReq.getIdGoiThau());
		hhQdPduyetKqlcntDtlRepository.save(dtl);

		return createCheck;
	}

	@Override
	public HhQdPduyetKqlcntHdr update(HhQdPduyetKqlcntHdrReq objReq) throws Exception {
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

		return createCheck;
	}

	@Override
	public HhQdPduyetKqlcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdPduyetKqlcntHdr> qOptional = hhQdPduyetKqlcntHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

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
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhQdPduyetKqlcntHdr> optional = hhQdPduyetKqlcntHdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
			case Contains.BAN_HANH + Contains.MOI_TAO:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
		default:
			throw new Exception("Phê duyệt không thành công");
		}

		optional.get().setTrangThai(stReq.getTrangThai());
		if( stReq.getTrangThai().equals(Contains.BAN_HANH)){
			hhQdKhlcntDsgthauRepository.updateGoiThau(optional.get().getIdGoiThau(),optional.get().getTrungThau() ? Contains.GT_TRUNG_THAU : Contains.GT_HUY_THAU,optional.get().getLyDoHuy());
		}
//		if (optional.get().getChildren1() != null) {
//
//			Optional<HhDthau> dThau = hhDthauRepository.findBySoQd(optional.get().getSoQd());
//			List<HhDthauGthau> dThauChild = dThau.get().getChildren();
//
//			List<HhQdPduyetKqlcntDtl> dtlsKqua = optional.get().getChildren1();
//			for (HhQdPduyetKqlcntDtl kqua : dtlsKqua) {
//				HhDthauKquaLcnt kqLcnt = new HhDthauKquaLcnt();
////				kqLcnt.setParent(dThau.get().getChildren());
//
//				List<FileDKemJoinKquaLcnt> fileDkKqLcnts = ObjectMapperUtils.mapAll(optional.get().getChildren(),
//						FileDKemJoinKquaLcnt.class);
//				fileDkKqLcnts.forEach(f -> {
//					f.setDataType(HhDthauKquaLcnt.TABLE_NAME);
//					f.setCreateDate(new Date());
//				});
//				kqLcnt.setChildren(fileDkKqLcnts);
//			}
//		}

		HhQdPduyetKqlcntHdr createCheck = hhQdPduyetKqlcntHdrRepository.save(optional.get());

		return createCheck;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdPduyetKqlcntHdr> optional = hhQdPduyetKqlcntHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");

		hhQdPduyetKqlcntHdrRepository.delete(optional.get());

	}

	@Override
	public Page<HhQdPduyetKqlcntHdr> timKiemPage(HhQdPduyetKqlcntSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		return hhQdPduyetKqlcntHdrRepository.selectPage(req.getNamKhoach(),req.getLoaiVthh(),convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getSoQd(),req.getTrangThai(), pageable);

	}

	@Override
	public Page<HhQdPduyetKqlcntRes> timKiemPageCustom(HhQdPduyetKqlcntSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		Page<HhQdPduyetKqlcntRes> page =  hhQdPduyetKqlcntHdrRepository.customQuerySearch(req.getNamKhoach(),req.getLoaiVthh(),req.getTrichYeu(),pageable);
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDviLquan = getListDanhMucDviLq("NT");
		page.forEach(f -> {
			f.setTenHdong(StringUtils.isEmpty(f.getLoaiHdong()) ? null : hashMapLoaiHdong.get(f.getLoaiHdong()));
			f.setTenNhaThau(StringUtils.isEmpty(f.getIdNhaThau()) ? null : hashMapDviLquan.get(String.valueOf(Double.parseDouble(f.getIdNhaThau().toString()))));
		});
		return page;
	}

	@Override
	public List<HhQdPduyetKqlcntHdr> timKiemAll(HhQdPduyetKqlcntSearchReq req) throws Exception {
		return hhQdPduyetKqlcntHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(),convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getSoQd(), req.getTrangThai());
	}

}
