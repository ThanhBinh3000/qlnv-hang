package com.tcdt.qlnvhang.controller.dexuat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhDxKhLcntThopHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntDsChuaThReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.HhDxKhLcntThopHdrService;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.DX_KH + PathContains.URL_THOP_DATA)
@Api(tags = "Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu")
public class HhDxKhLcntThopHdrController {

	@Autowired
	private HhDxKhLcntThopHdrService service;

	@ApiOperation(value = "Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = "dx-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> sumarryData(HttpServletRequest request,
			@Valid @RequestBody HhDxKhLcntTChiThopReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.sumarryData(objReq, request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu Gạo trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tạo mới tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,
			@Valid @RequestBody HhDxKhLcntThopHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(objReq, request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới tổng hợp đề xuất kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody HhDxKhLcntThopHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Cập nhật tổng hợp đề xuất kế hoạch lựa chọn nhà thầu gạo trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectPage(HttpServletRequest request,@RequestBody HhDxKhLcntThopSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.timKiemPage(request,objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu gạo trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID đề xuất kế hoạch lựa chọn nhà thầu Gạo", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu gạo trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá đề xuất kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(idSearchReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xoá đề xuất kế hoạch lựa chọn nhà thầu gạo trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Danh sách đề xuất kế hoạch LCNT chưa/đã tổng hợp", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU + "/ds-chua-th", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> findByStatus(@RequestBody @Valid HhDxKhLcntDsChuaThReq objReq,
			HttpServletRequest request) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.findByStatus(objReq, request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Danh sách đề xuất kế hoạch LCNT chưa/đã tổng hợp trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Kết xuất danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(PathContains.URL_KET_XUAT)
	@ResponseStatus(HttpStatus.OK)
	public void exportDsThDxKhLcnt(@Valid @RequestBody HhDxKhLcntThopSearchReq searchReq, HttpServletResponse response)
			throws Exception {
		try {
			service.exportDsThDxKhLcnt(searchReq, response);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Kết xuất danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu trace: {}", e);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			body.put("msg", e.getMessage());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		}
	}

	@ApiOperation(value = "Tra cứu danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TAT_CA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(HttpServletRequest request,@RequestBody HhDxKhLcntThopSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.timKiemAll(request,objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu gạo trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}
}
