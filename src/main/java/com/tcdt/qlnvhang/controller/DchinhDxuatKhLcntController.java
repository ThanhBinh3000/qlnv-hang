package com.tcdt.qlnvhang.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.DchinhDxKhLcntHdrReq;
import com.tcdt.qlnvhang.service.DchinhDxuatKhLcntService;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntHdr;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;

import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.QL_QD_LCNT + PathContains.DIEU_CHINH)
@Api(tags = "Điều chỉnh đề xuất kế hoạch lựa chọn nhà thầu")
public class DchinhDxuatKhLcntController extends BaseController {

	@Autowired
	private DchinhDxuatKhLcntService dchinhDxuatKhLcntService;

	@ApiOperation(value = "Tra cứu Quyết định điều chỉnh phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdLcntHdrDChinhSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			Page<HhDchinhDxKhLcntHdr> qhKho = dchinhDxuatKhLcntService.getAllPage(objReq);
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

	@ApiOperation(value = "Tạo Quyết định điều chỉnh qđ phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> createDchinh(HttpServletRequest request, @Valid @RequestBody DchinhDxKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(dchinhDxuatKhLcntService.save(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
//
//	@ApiOperation(value = "Cập nhật Quyết định điều chỉnh qđ phê duyệt KHLCNT", response = List.class)
//	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntHdrReq objReq) {
//		BaseResponse resp = new BaseResponse();
//		try {
//			if (StringUtils.isEmpty(objReq.getId()))
//				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//			Optional<HhQdKhlcntHdr> HhQdKhlcntHdr = qlnvQdLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
//
//			if (!HhQdKhlcntHdr.isPresent())
//				throw new Exception("Không tìm thấy dữ liệu cần sửa");
//
//			HhQdKhlcntHdr dataDTB = HhQdKhlcntHdr.get();
//
//			if (!Contains.QUYET_DINH_DC.equalsIgnoreCase(dataDTB.getLoaiQd()))
//				throw new Exception("Không tìm thấy dữ liệu cần sửa");
//
////			String soQdinhGoc = dataDTB.getSoQdinhGoc();
////			String soQdGiaoCtkh = dataDTB.getSoQdGiaoCtkh();
////			String nguonvon = dataDTB.getNguonvon();
//
//			HhQdKhlcntHdr dataMap = new ModelMapper().map(objReq, HhQdKhlcntHdr.class);
//
//			updateObjectToObject(dataDTB, dataMap);
//			dataDTB.setNgaySua(getDateTimeNow());
//			dataDTB.setNguoiSua(getUserName(request));
////			dataDTB.setSoQdinhGoc(soQdinhGoc);
////			dataDTB.setSoQdGiaoCtkh(soQdGiaoCtkh);
////			dataDTB.setNguonvon(nguonvon);
////			dataDTB.setLoaiDieuChinh(Contains.QUYET_DINH_DC);
//
//			List<QlnvQdLcntDtlReq> dtlReqList = objReq.getDetail();
//			List<HhDchinhDxKhLcntDtl> details = new ArrayList<>();
//			if (dtlReqList != null) {
//				List<HhDchinhDxKhLcntDtlCtiet> detailChild;
//				for (QlnvQdLcntDtlReq dtlReq : dtlReqList) {
//					List<QlnvQdLcntDtlCtietReq> cTietReq = dtlReq.getDetail();
//					HhDchinhDxKhLcntDtl detail = ObjectMapperUtils.map(dtlReq, HhDchinhDxKhLcntDtl.class);
//					detailChild = new ArrayList<HhDchinhDxKhLcntDtlCtiet>();
//					if (cTietReq != null)
//						detailChild = ObjectMapperUtils.mapAll(cTietReq, HhDchinhDxKhLcntDtlCtiet.class);
////					detail.setDetailList(detailChild);
//					details.add(detail);
//				}
//				dataMap.setDetailList(details);
//			}
//
//			qlnvQdLcntHdrRepository.save(dataDTB);
//			resp.setData(dataDTB);
//			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//		} catch (Exception e) {
//			// TODO: handle exception
//			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//			resp.setMsg(e.getMessage());
//			log.error(e.getMessage());
//		}
//		return ResponseEntity.ok(resp);
//	}
//
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định điều chỉnh qđ phê duyệt KHLCNT", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Quyết định điều chỉnh qđ phê duyệt KHLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
//			Optional<QlnvQdLcntHdr> qOptional = qlnvQdLcntHdrRepository.findById(Long.parseLong(ids));
//			if (!qOptional.isPresent())
//				throw new UnsupportedOperationException("Không tồn tại bản ghi");
//			resp.setData(qOptional);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Quyết định điều chỉnh qđ phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(HttpServletRequest request,@Valid  @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(dchinhDxuatKhLcntService.approve(stReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin Quyết định điều chỉnh qđ phê duyệt KHLCNT", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
//			Optional<HhQdKhlcntHdr> QlnvQdLcntHdr = qlnvQdLcntHdrRepository.findById(Long.parseLong(ids));
//			if (!QlnvQdLcntHdr.isPresent())
//				throw new Exception("Không tìm thấy dữ liệu cần xoá");
//			qlnvQdLcntHdrRepository.delete(QlnvQdLcntHdr.get());

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