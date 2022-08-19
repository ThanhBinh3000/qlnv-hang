package com.tcdt.qlnvhang.controller;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.CountReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.count.CountNhapHangService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = PathContains.NHAP_HANG)
@Api(tags = "Count Nhập Hàng")
public class CountNhapHangController {
	@Autowired
	private CountNhapHangService countNhapHangService;

	@ApiOperation(value = "Count kiểm tra chất lượng", response = Page.class)
	@GetMapping("/kiem-tra-chat-luong/count")
	public ResponseEntity<BaseResponse> countKtcl(CountReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BaseNhapHangCount res = countNhapHangService.countKtcl(req);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Count nhập kho", response = Page.class)
	@GetMapping("/nhap-kho/count")
	public ResponseEntity<BaseResponse> countNhapKho(CountReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BaseNhapHangCount res = countNhapHangService.countNhapKho(req);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}


