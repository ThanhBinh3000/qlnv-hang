package com.tcdt.qlnvhang.controller.quanlyphieukiemtrachatluonghang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.QL_PHIEU_KIEM_TRA_CHAT_LUONG_HANG_LT)
@Api(tags = "Quản lý phiếu kiểm tra chất lượng hàng lương thực")
public class QlpktclhPhieuKtChatLuongController {

	@Autowired
	private QlpktclhPhieuKtChatLuongService service;

	@ApiOperation(value = "Tạo mới phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> insert(@Valid @RequestBody QlpktclhPhieuKtChatLuongRequestDto request) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới phiếu kiểm tra chất lượng hàng lương thực", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@PutMapping
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody QlpktclhPhieuKtChatLuongRequestDto request) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Sửa phiếu kiểm tra chất lượng hàng lương thực", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(@RequestBody QlpktclhPhieuKtChatLuongFilterRequestDto req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.timKiem(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu phiếu kiểm tra chất lượng hàng lương thực", e);
		}
		return ResponseEntity.ok(resp);
	}
}
