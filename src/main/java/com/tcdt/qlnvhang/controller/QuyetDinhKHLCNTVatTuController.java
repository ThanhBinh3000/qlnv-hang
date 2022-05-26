package com.tcdt.qlnvhang.controller;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.QuyetDinhKHLCNTVatTuReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.QuyetDinhKHLCNTGoiThauVatTuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/qd-ke-hoach-lua-chon-nha-thau-vat-tu")
@Slf4j
@Api(tags = "Quản lý quyết định kế hoạch lựa chọn nhà thầu vật tư")
public class QuyetDinhKHLCNTVatTuController extends BaseController {
	@Autowired
	private QuyetDinhKHLCNTGoiThauVatTuService service;

	@ApiOperation(value = "Tạo mới quyết định kế hoạch lựa chọn nhà thầu vật tư", response = List.class)
	@PostMapping()
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody QuyetDinhKHLCNTVatTuReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới quyết định kế hoạch lựa chọn nhà thầu vật tư error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật quyết định kế hoạch lựa chọn nhà thầu vật tư", response = List.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody QuyetDinhKHLCNTVatTuReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Cập nhật quyết định kế hoạch lựa chọn nhà thầu vật tư error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa quyết định kế hoạch lựa chọn nhà thầu vật tư", response = List.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(id);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xóa quyết định kế hoạch lựa chọn nhà thầu vật tư error", e);
		}
		return ResponseEntity.ok(resp);
	}

}
