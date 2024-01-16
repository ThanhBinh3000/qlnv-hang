package com.tcdt.qlnvhang.controller.nhaphang;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.object.HhSlNhapHangReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.HhDxuatKhLcntHdrService;
import com.tcdt.qlnvhang.service.HhSlNhapHangService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.SL_NH)
@Api(tags = "Nhập hàng - Đấu thầu - Kế hoạch lcnt - Đề xuất kế hoạch lựa chọn nhà thầu lương thực và vật tư")
public class HhSlNhapHangController {

	@Autowired
	private HhSlNhapHangService service;

	@ApiOperation(value = "Tạo mới", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
			@RequestBody HhSlNhapHangReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid HttpServletRequest request,
			@RequestBody HhSlNhapHangReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Cập nhật trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

//	@ApiOperation(value = "Tra cứu", response = List.class)
//	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseStatus(HttpStatus.OK)
//	public ResponseEntity<BaseResponse> selectAll(@Valid HttpServletRequest request,
//			@RequestBody HhDxuatKhLcntSearchReq objReq) {
//		BaseResponse resp = new BaseResponse();
//		try {
//			resp.setData(service.timKiem(objReq));
//			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//		} catch (Exception e) {
//			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//			resp.setMsg(e.getMessage());
//			log.error("Tra cứu trace: {}", e);
//		}
//		return ResponseEntity.ok(resp);
//	}


	@ApiOperation(value = "Lấy chi tiết", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID", example = "1", required = true) @PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(id));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa", response = List.class)
	@PostMapping(value = "/count-sl-ctkh-qd", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> soLuongKeHoachNam(HttpServletRequest request,
												   @Valid @RequestBody CountKhlcntSlReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.countSoLuongKeHoachNam(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa", response = List.class)
	@PostMapping(value = "/count-sl-ctkh-qd-by-id-qd", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> soLuongKeHoachNamByIdQd(HttpServletRequest request,
														  @Valid @RequestBody CountKhlcntSlReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.countSoLuongKeHoachNamByIdQd(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa", response = List.class)
	@PostMapping(value = "/count-sl-ctkh-kh", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> soLuongKeHoachNamTheoKh(HttpServletRequest request,
												   @Valid @RequestBody CountKhlcntSlReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.countSoLuongKeHoachNamTheoKh(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

}
