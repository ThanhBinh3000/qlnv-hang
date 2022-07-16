package com.tcdt.qlnvhang.controller.kehoachbanhangdaugia;


import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.kehoachbanhangdaugia.BhDgKehoachReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.kehoachbanhangdaugia.BhDgKehoachRes;
import com.tcdt.qlnvhang.service.kehoachbanhangdaugia.KeHoachBanDauGiaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ke-hoach-ban-dau-gia")
@Slf4j
@Api(tags = "Kế hoạch bán đấu giá hàng hóa")
@RequiredArgsConstructor
public class BhDgKehoachController extends BaseController {
	private final KeHoachBanDauGiaService keHoachBanDauGiaService;

	@ApiOperation(value = "Tạo mới kế hoạch bán đấu giá hàng hóa", response = BhDgKehoachRes.class)
	@PostMapping()
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody BhDgKehoachReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BhDgKehoachRes res = keHoachBanDauGiaService.create(req);
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

	@ApiOperation(value = "Sửa thông tin kế hoạch bán đấu giá hàng hóa", response = BhDgKehoachRes.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody BhDgKehoachReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BhDgKehoachRes res = keHoachBanDauGiaService.update(req);
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
