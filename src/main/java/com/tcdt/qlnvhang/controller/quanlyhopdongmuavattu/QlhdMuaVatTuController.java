package com.tcdt.qlnvhang.controller.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuRequestDTO;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavattu.QlhdMuavatTuService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PathContains.QLHD_MUA_VAT_TU)
@Api(tags = "Quản lý hợp đồng mua vật tư")
public class QlhdMuaVatTuController {

	@Autowired
	private QlhdMuavatTuService service;

	@ApiOperation(value = "Tạo mới quản lý hợp đồng mua vật tư", response = List.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(@Valid @RequestBody QlhdMuaVatTuRequestDTO request) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Quan ly hop dong mua vat tu loi: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

}
