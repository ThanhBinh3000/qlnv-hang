package com.tcdt.qlnvhang.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvQdLcntHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvQdLcntVtuHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntDtlCtietReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntHdrReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntVtuDtlCtietReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntVtuDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntVtuHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntVtuHdrDChinhSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvQdLcntSpecification;
import com.tcdt.qlnvhang.secification.QlnvQdLcntVtuSpecification;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntHdr;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.QL_QD_LCNT_VTU + PathContains.DIEU_CHINH)
@Api(tags = "Quyết định điều chỉnh phê duyệt KHLCNT vật tư")
public class QlnvQdLcntVtuHdrDChinhController extends BaseController {
	@Autowired
	private QlnvQdLcntVtuHdrRepository qlnvQdLcntVtuHdrRepository;
	
	/**
	 * DIEU CHINH- search
	 * @param objReq
	 * @return
	 */
	@ApiOperation(value = "Tra cứu Quyết định điều chỉnh phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdLcntVtuHdrDChinhSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
			objReq.setLoaiQd(Contains.QUYET_DINH_DC);

			Page<QlnvQdLcntVtuHdr> qhKho = qlnvQdLcntVtuHdrRepository.findAll(QlnvQdLcntVtuSpecification.buildSearchQuery(objReq), pageable);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}
	/**
	 * DIEU CHINH - TAO MOI
	 * @param request
	 * @param objReq
	 * @return
	 */
	@ApiOperation(value = "Tạo Quyết định điều chỉnh qđ phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> createDchinh(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Tạo thất bại, không tìm thấy dữ liệu");
			Optional<QlnvQdLcntVtuHdr> optional = qlnvQdLcntVtuHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần tạo");
			
			QlnvQdLcntVtuHdr dataDTB = optional.get();
			
			// build request
//			List<QlnvQdLcntDtl> khoachList = dataDTB.getDetailList();
//			QlnvQdLcntHdrReq hdrReq = new QlnvQdLcntHdrReq();
//			List<QlnvQdLcntDtlReq> dtlReqList = new ArrayList<QlnvQdLcntDtlReq>();
//			for (QlnvQdLcntDtl qlnvKhoachLcntHdr : khoachList) {
//				QlnvQdLcntDtlReq dtlReq = new QlnvQdLcntDtlReq();
//				List<QlnvQdLcntDtlCtietReq> detail = new ArrayList<QlnvQdLcntDtlCtietReq>();
//				for (QlnvQdLcntDtlCtiet dtl : qlnvKhoachLcntHdr.getDetailList()) {
//					QlnvQdLcntDtlCtietReq ctietReq = new QlnvQdLcntDtlCtietReq();
//					ctietReq.setDiaChi(dtl.getDiaChi());
//					ctietReq.setDonGia(dtl.getDonGia());
//					ctietReq.setDviTinh(dtl.getDviTinh());
//					ctietReq.setMaDvi(dtl.getMaDvi());
//					ctietReq.setSoDxuat(dtl.getSoDxuat());
//					ctietReq.setSoDuyet(dtl.getSoDuyet());
//					ctietReq.setThuesuat(dtl.getThuesuat());
//					ctietReq.setThanhtien(dtl.getThanhtien());
//					ctietReq.setTongtien(dtl.getTongtien());
//					detail.add(ctietReq);
//				}
//				dtlReq.setDetail(detail);
//				dtlReq.setMaDvi(qlnvKhoachLcntHdr.getMaDvi());
//				dtlReq.setSoDxuat(qlnvKhoachLcntHdr.getSoDxuat());
//				dtlReqList.add(dtlReq);
//				
//			}
//			hdrReq.setDetail(dtlReqList);
//			hdrReq.setLoaiHanghoa(dataDTB.getLoaiHanghoa());
//			hdrReq.setNguonvon(dataDTB.getNguonvon());
//			hdrReq.setLoaiQd(Contains.QUYET_DINH_DC);
//			hdrReq.setSoQdGiaoCtkh(dataDTB.getSoQdGiaoCtkh());
//			hdrReq.setMaHanghoa(dataDTB.getMaHanghoa());
//			hdrReq.setSoQdinhGoc(dataDTB.getSoQdinh());
//			hdrReq.setTuNgayThien(dataDTB.getTuNgayThien());
//			hdrReq.setDenNgayThien(dataDTB.getDenNgayThien());
			
			// TODO: review 
			objReq.setSoQdinhGoc(dataDTB.getSoQdinh());
			objReq.setLoaiQd(Contains.QUYET_DINH_DC);
			
			// create new qd
			QlnvQdLcntVtuHdr qdDc = new ModelMapper().map(objReq, QlnvQdLcntVtuHdr.class);
			qdDc.setId(null);
			qdDc.setNgayTao(getDateTimeNow());
			qdDc.setTrangThai(Contains.TAO_MOI);
			qdDc.setNguoiTao(getUserName(request));
			qdDc.setNgayQd(getDateTimeNow());
			
			List<QlnvQdLcntVtuDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdLcntVtuDtl> details = new ArrayList<>();
			if (dtlReqList != null) {
				List<QlnvQdLcntVtuDtlCtiet> detailChild;
				for (QlnvQdLcntVtuDtlReq dtlReq : dtlReqList) {
					List<QlnvQdLcntVtuDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdLcntVtuDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdLcntVtuDtl.class);
					detailChild = new ArrayList<QlnvQdLcntVtuDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdLcntVtuDtlCtiet.class);
					detail.setDetailList(detailChild);
					details.add(detail);
				}
				qdDc.setDetailList(details);
			}
			qlnvQdLcntVtuHdrRepository.save(qdDc);
			
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(qdDc);
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Cập nhật Quyết định điều chỉnh qđ phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Optional<QlnvQdLcntVtuHdr> optional = qlnvQdLcntVtuHdrRepository.findById(Long.valueOf(objReq.getId()));
			
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			
			QlnvQdLcntVtuHdr dataDTB = optional.get();
			
			if (!Contains.QUYET_DINH_DC.equalsIgnoreCase(dataDTB.getLoaiQd()))
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			
			String soQdinhGoc = dataDTB.getSoQdinhGoc();
			
			QlnvQdLcntVtuHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntVtuHdr.class);
			
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(getDateTimeNow());
			dataDTB.setNguoiSua(getUserName(request));
			dataDTB.setSoQdinhGoc(soQdinhGoc);
			dataDTB.setLoaiDieuChinh(Contains.QUYET_DINH_DC);
			
			List<QlnvQdLcntVtuDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdLcntVtuDtl> details = new ArrayList<>();
			if (dtlReqList != null) {
				List<QlnvQdLcntVtuDtlCtiet> detailChild;
				for (QlnvQdLcntVtuDtlReq dtlReq : dtlReqList) {
					List<QlnvQdLcntVtuDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdLcntVtuDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdLcntVtuDtl.class);
					detailChild = new ArrayList<QlnvQdLcntVtuDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdLcntVtuDtlCtiet.class);
					detail.setDetailList(detailChild);
					details.add(detail);
				}
				dataMap.setDetailList(details);
			}
			
			qlnvQdLcntVtuHdrRepository.save(dataDTB);
			resp.setData(dataDTB);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định điều chỉnh qđ phê duyệt KHLCNT vật tư", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Quyết định phê duyệt KHLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntVtuHdr> qOptional = qlnvQdLcntVtuHdrRepository.findById(Long.parseLong(ids));
			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qOptional);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Quyết định điều chỉnh qđ phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(HttpServletRequest request,@Valid  @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdLcntVtuHdr> qdLcnt = qlnvQdLcntVtuHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qdLcnt.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");
			qdLcnt.get().setTrangThai(stReq.getTrangThai());
			String status = stReq.getTrangThai();
			Calendar cal = Calendar.getInstance();
			switch(status) {
				case Contains.CHO_DUYET:
					qdLcnt.get().setNguoiGuiDuyet(getUserName(request));
					qdLcnt.get().setNgayGuiDuyet(cal.getTime());
					break;
				case Contains.DUYET:
					qdLcnt.get().setNguoiPduyet(getUserName(request));
					qdLcnt.get().setNgayPduyet(cal.getTime());
					break;
				case Contains.TU_CHOI:
					qdLcnt.get().setLdoTuchoi(stReq.getLyDo());
					break;
				default:
					break;
			}
			qlnvQdLcntVtuHdrRepository.save(qdLcnt.get());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Xoá thông tin Quyết định điều chỉnh qđ phê duyệt KHLCNT vật tư", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntVtuHdr> QlnvQdLcntHdr = qlnvQdLcntVtuHdrRepository.findById(Long.parseLong(ids));
			if (!QlnvQdLcntHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			qlnvQdLcntVtuHdrRepository.delete(QlnvQdLcntHdr.get());
			
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}
}