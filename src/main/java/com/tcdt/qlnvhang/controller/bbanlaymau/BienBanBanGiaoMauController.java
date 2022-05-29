package com.tcdt.qlnvhang.controller.bbanlaymau;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanBanGiaoMauReq;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauRes;
import com.tcdt.qlnvhang.service.bbanlaymau.BienBanBanGiaoMauService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = PathContains.BBAN_LAY_MAU)
@Api(tags = "Quản lý Biên bản bàn giao mẫu")
public class BienBanBanGiaoMauController {
	@Autowired
	private BienBanBanGiaoMauService bienBanBanGiaoMauService;

	@ApiOperation(value = "Tạo mới Biên bản bàn giao mẫu", response = BienBanBanGiaoMauRes.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody BienBanBanGiaoMauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BienBanBanGiaoMauRes res = bienBanBanGiaoMauService.create(req);
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

	@ApiOperation(value = "Sửa thông tin Biên bản bàn giao mẫu", response = BienBanBanGiaoMauRes.class)
	@PutMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody BienBanBanGiaoMauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BienBanBanGiaoMauRes res = bienBanBanGiaoMauService.update(req);
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

	@ApiOperation(value = "Xoá thông tin Biên bản bàn giao mẫu", response = Boolean.class)
	@DeleteMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@RequestParam("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = bienBanBanGiaoMauService.delete(id);
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

	@ApiOperation(value = "Chi tiết thông tin Biên bản bàn giao mẫu", response = Page.class)
	@GetMapping(value = PathContains.URL_CHI_TIET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> detail(@RequestParam("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			BienBanBanGiaoMauRes res = bienBanBanGiaoMauService.detail(id);
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

	@ApiOperation(value = "Gửi duyệt/Duyệt/Từ chối thông tin Biên bản bàn giao mẫu", response = Boolean.class)
	@PutMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(StatusReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = bienBanBanGiaoMauService.updateStatus(req);
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

	@ApiOperation(value = "Tra cứu thông tin Biên bản bàn giao mẫu", response = Page.class)
	@GetMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(BienBanBanGiaoMauSearchReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Page<BienBanBanGiaoMauRes> res = bienBanBanGiaoMauService.search(req);
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

