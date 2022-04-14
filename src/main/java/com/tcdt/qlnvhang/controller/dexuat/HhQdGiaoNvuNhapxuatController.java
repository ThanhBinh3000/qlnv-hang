package com.tcdt.qlnvhang.controller.dexuat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.DX_KH + PathContains.NHAP_XUAT)
@Api(tags = "Thông tin quyết định giao nhiệm vụ nhập xuất")
public class HhQdGiaoNvuNhapxuatController {

	@Autowired
	private HhQdGiaoNvuNhapxuatService service;

	@ApiOperation(value = "Tạo mới thông tin quyết định giao nhiệm vụ nhập xuất", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,
			@Valid @RequestBody HhQdGiaoNvuNhapxuatHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới thông tin quyết định giao nhiệm vụ nhập xuất trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật thông tin quyết định giao nhiệm vụ nhập xuất", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody HhQdGiaoNvuNhapxuatHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Cập nhật thông tin quyết định giao nhiệm vụ nhập xuất trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định giao nhiệm vụ nhập xuất", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID thông tin quyết định giao nhiệm vụ nhập xuất", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết thông tin quyết định giao nhiệm vụ nhập xuất trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin quyết định giao nhiệm vụ nhập xuất", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.approve(stReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Phê duyệt thông tin quyết định giao nhiệm vụ nhập xuất trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin quyết định giao nhiệm vụ nhập xuất", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(idSearchReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xoá thông tin quyết định giao nhiệm vụ nhập xuất trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu thông tin quyết định giao nhiệm vụ nhập xuất", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody HhQdNhapxuatSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.colection(objReq, request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (

		Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu thông tin quyết định giao nhiệm vụ nhập xuất trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin đơn vị thực hiện", response = List.class)
	@GetMapping(value = "/don-vi-thuc-hien/" + PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> dviThucHienDetail(@PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.dviThQdDetail(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết thông tin đơn vị thực hiện trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}
}
