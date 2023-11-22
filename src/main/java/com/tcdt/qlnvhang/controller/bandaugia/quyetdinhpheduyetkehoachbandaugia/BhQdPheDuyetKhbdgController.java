package com.tcdt.qlnvhang.controller.bandaugia.quyetdinhpheduyetkehoachbandaugia;


import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgCtResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgSearchResponse;
import com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgService;
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
@RequestMapping(value = "/qd-phe-duyet-ke-hoach-ban-dau-gia")
@Slf4j
@Api(tags = "Quyết định phê duyệt kế hoạch bán đấu giá")
@RequiredArgsConstructor
public class BhQdPheDuyetKhbdgController extends BaseController {
	private final BhQdPheDuyetKhbdgService qdPheDuyetKhbdgService;

	@ApiOperation(value = "Tạo mới quyết định phê duyệt kế hoạch bán đấu giá", response = BhQdPheDuyetKhbdgResponse.class)
	@PostMapping()
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody BhQdPheDuyetKhbdgRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			BhQdPheDuyetKhbdgResponse res = qdPheDuyetKhbdgService.create(req);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Create error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Update quyết định phê duyệt kế hoạch bán đấu giá", response = BhQdPheDuyetKhbdgResponse.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody BhQdPheDuyetKhbdgRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			BhQdPheDuyetKhbdgResponse res = qdPheDuyetKhbdgService.update(req);
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

	@ApiOperation(value = "Xoá thông tin quyết định phê duyệt kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = qdPheDuyetKhbdgService.delete(id);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Delete error ", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá danh sách thông tin quyết định phê duyệt kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@DeleteMapping()
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = qdPheDuyetKhbdgService.deleteMultiple(req.getIds());
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Delete error ", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Search thông tin quyết định phê duyệt kế hoạch bán đấu giá hàng hóa", response = Page.class)
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(BhQdPheDuyetKhbdgSearchRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			Page<BhQdPheDuyetKhbdgSearchResponse> res = qdPheDuyetKhbdgService.search(req);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Search error ", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Chi tiết thông tin quyết định phê duyệt kế hoạch bán đấu giá hàng hóa", response = Page.class)
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			BhQdPheDuyetKhbdgResponse res = qdPheDuyetKhbdgService.detail(id);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Detail error ", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export thông tin quyết định phê duyệt kế hoạch bán đấu giá hàng hóa", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(HttpServletResponse response, @RequestBody BhQdPheDuyetKhbdgSearchRequest req) {

		try {
			qdPheDuyetKhbdgService.exportToExcel(req, response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}
	}

	@ApiOperation(value = "Phê duyệt/ từ chối Quản lý Quyết định phê duyệt kế hoạch bán đấu giá", response = List.class)
	@PutMapping("/trang-thai")
	public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(qdPheDuyetKhbdgService.updateStatusQd(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Phê duyệt/ từ chối Tổng hợp đề xuất kế hoạch bán đấu giá: {}", e);
		}
		return ResponseEntity.ok(resp);
	}


	@ApiOperation(value = "Thông tin phụ lục quyết định phê duyệt kế hoạch bán đấu giá", response = Page.class)
	@GetMapping(value = "/phu-luc", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> getPhuLuc(@RequestParam Long bhTongHopDeXuatId) {
		BaseResponse resp = new BaseResponse();
		try {
			List<BhQdPheDuyetKhbdgCtResponse> res = qdPheDuyetKhbdgService.getThongTinPhuLuc(bhTongHopDeXuatId);
			resp.setData(res);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("The exception", e);
		}
		return ResponseEntity.ok(resp);
	}

}
