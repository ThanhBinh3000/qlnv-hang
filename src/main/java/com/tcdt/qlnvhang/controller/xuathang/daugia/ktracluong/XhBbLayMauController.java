package com.tcdt.qlnvhang.controller.xuathang.daugia.ktracluong;


import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauSearchRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauSearchResponse;
import com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.DAU_GIA + PathContains.BBAN_LAY_MAU )
@Slf4j
@Api(tags = "Xuất hàng - Bán đấu giá - Kế hoạch bán đấu giá - Biên bản lấy mẫu")
public class XhBbLayMauController extends BaseController {
	@Autowired
	private XhBbLayMauService service;

	@ApiOperation(value = "Tạo mới Biên bản lấy mẫu", response = XhBbLayMauResponse.class)
	@PostMapping(value=  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> create(@RequestBody XhBbLayMauRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Create error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa Biên bản lấy mẫu", response = XhBbLayMauResponse.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhBbLayMauRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Update error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá Biên bản lấy mẫu", response = Boolean.class)
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(id);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá danh sách Biên bản lấy mẫu", response = Boolean.class)
	@DeleteMapping()
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
//			Boolean res = service.deleteMultiple(req.getIds());
//			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Search Biên bản lấy mẫu", response = Page.class)
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(XhBbLayMauSearchRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
//			Page<XhBbLayMauSearchResponse> res = service.search(req);
//			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Search Error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Thông tin chi tiết Biên bản lấy mẫu", response = Boolean.class)
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
//			XhBbLayMauResponse res = service.detail(id);
//			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Update trạng thái Biên bản lấy mẫu", response = XhBbLayMauResponse.class)
	@PutMapping(value = "/trang-thai", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateTrangThai(@RequestParam Long id,
														@RequestParam String trangThaiId) {
		BaseResponse resp = new BaseResponse();
		try {
//			XhBbLayMauResponse res = service.updateTrangThai(id, trangThaiId);
//			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export kế hoạch Biên bản lấy mẫu", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(HttpServletResponse response, @RequestBody XhBbLayMauSearchRequest req) {

		try {
//			service.exportToExcel(req, response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}
	}

}



