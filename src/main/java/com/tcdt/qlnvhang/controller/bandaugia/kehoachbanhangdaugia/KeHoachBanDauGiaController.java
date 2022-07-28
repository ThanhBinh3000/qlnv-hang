package com.tcdt.qlnvhang.controller.bandaugia.kehoachbanhangdaugia;


import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KehoachBanDauGiaRequest;
import com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.KeHoachBanDauGiaResponse;
import com.tcdt.qlnvhang.service.bandaugia.kehoachbanhangdaugia.KeHoachBanDauGiaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/ke-hoach-ban-dau-gia")
@Slf4j
@Api(tags = "Kế hoạch bán đấu giá hàng hóa")
@RequiredArgsConstructor
public class KeHoachBanDauGiaController extends BaseController {
	private final KeHoachBanDauGiaService keHoachBanDauGiaService;

	@ApiOperation(value = "Tạo mới kế hoạch bán đấu giá hàng hóa", response = KeHoachBanDauGiaResponse.class)
	@PostMapping()
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody KehoachBanDauGiaRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			KeHoachBanDauGiaResponse res = keHoachBanDauGiaService.create(req);
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

	@ApiOperation(value = "Sửa thông tin kế hoạch bán đấu giá hàng hóa", response = KeHoachBanDauGiaResponse.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody KehoachBanDauGiaRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			KeHoachBanDauGiaResponse res = keHoachBanDauGiaService.update(req);
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

	@ApiOperation(value = "Xoá thông tin kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = keHoachBanDauGiaService.delete(id);
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

	@ApiOperation(value = "Xoá danh sách thông tin kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@DeleteMapping()
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = keHoachBanDauGiaService.deleteMultiple(req.getIds());
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

	@ApiOperation(value = "Search thông tin kế hoạch bán đấu giá hàng hóa", response = Page.class)
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(KeHoachBanDauGiaSearchRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			Page<KeHoachBanDauGiaResponse> res = keHoachBanDauGiaService.search(req);
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

	@ApiOperation(value = "Update trạng thái kế hoạch bán đấu giá hàng hóa", response = Page.class)
	@PutMapping(value = "/trang-thai", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateTrangThai(@RequestParam Long id,
														@RequestParam String trangThaiId) {
		BaseResponse resp = new BaseResponse();
		try {
			KeHoachBanDauGiaResponse res = keHoachBanDauGiaService.updateTrangThai(id, trangThaiId);
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

	@ApiOperation(value = "Export kế hoạch bán đấu giá hàng hóa", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(HttpServletResponse response, @RequestBody KeHoachBanDauGiaSearchRequest req) {

		try {
			keHoachBanDauGiaService.exportToExcel(req, response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}
	}

	@ApiOperation(value = "Thông tin chi tiết kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			KeHoachBanDauGiaResponse res = keHoachBanDauGiaService.detail(id);
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
