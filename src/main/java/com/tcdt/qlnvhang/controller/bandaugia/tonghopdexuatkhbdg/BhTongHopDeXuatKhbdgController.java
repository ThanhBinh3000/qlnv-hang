package com.tcdt.qlnvhang.controller.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgRequest;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchResponse;
import com.tcdt.qlnvhang.service.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/tong-hop-de-xuat-ke-hoach-ban-dau-gia")
@Slf4j
@Api(tags = "Tổng hợp đề xuất kế hoạch bán đấu giá")
@RequiredArgsConstructor
public class BhTongHopDeXuatKhbdgController extends BaseController {
	@Autowired
	private final BhTongHopDeXuatKhbdgService tongHopDeXuatKhbdgService;

	@ApiOperation(value = "Tạo mới tổng hợp đề xuất kế hoạch bán đấu giá", response = BhTongHopDeXuatKhbdgResponse.class)
	@PostMapping()
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody BhTongHopDeXuatKhbdgRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			BhTongHopDeXuatKhbdgResponse res = tongHopDeXuatKhbdgService.create(req);
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

	@ApiOperation(value = "Update tổng hợp đề xuất kế hoạch bán đấu giá", response = BhTongHopDeXuatKhbdgResponse.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody BhTongHopDeXuatKhbdgRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			BhTongHopDeXuatKhbdgResponse res = tongHopDeXuatKhbdgService.update(req);
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

	@ApiOperation(value = "Xoá thông tin tổng hợp đề xuất kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = tongHopDeXuatKhbdgService.delete(id);
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

	@ApiOperation(value = "Xoá danh sách thông tin tổng hợp đề xuất kế hoạch bán đấu giá hàng hóa", response = Boolean.class)
	@DeleteMapping()
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = tongHopDeXuatKhbdgService.deleteMultiple(req.getIds());
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
//
//	@ApiOperation(value = "Search thông tin tổng hợp đề xuất kế hoạch bán đấu giá hàng hóa", response = Page.class)
//	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<BaseResponse> search(BhTongHopDeXuatKhbdgSearchRequest req) {
//		BaseResponse resp = new BaseResponse();
//		try {
//			Page<BhTongHopDeXuatKhbdgSearchResponse> res = tongHopDeXuatKhbdgService.search(req);
//			resp.setData(res);
//			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//		} catch (Exception e) {
//			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//			resp.setMsg(e.getMessage());
//			log.error(e.getMessage());
//		}
//		return ResponseEntity.ok(resp);
//	}

	@ApiOperation(value = "Chi tiết thông tin tổng hợp đề xuất kế hoạch bán đấu giá hàng hóa", response = Page.class)
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			BhTongHopDeXuatKhbdgResponse res = tongHopDeXuatKhbdgService.detail(id);
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

	@ApiOperation(value = "Export thông tin tổng hợp đề xuất kế hoạch bán đấu giá hàng hóa", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(HttpServletResponse response, @RequestBody BhTongHopDeXuatKhbdgSearchRequest req) {
		try {
			tongHopDeXuatKhbdgService.exportToExcel(req, response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}
	}


	@ApiOperation(value = "Phê duyệt/ từ chối Quản lý Tổng hợp đề xuất kế hoạch bán đấu giá", response = List.class)
	@PutMapping("/trang-thai")
	public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(tongHopDeXuatKhbdgService.updateStatusQd(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Phê duyệt/ từ chối Tổng hợp đề xuất kế hoạch bán đấu giá: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu danh sách tổng hợp đề xuất kế hoạch bán đấu giá", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> searchPage(HttpServletRequest request, @RequestBody BhTongHopDeXuatKhbdgSearchRequest objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(tongHopDeXuatKhbdgService.searchPage(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu gạo trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

}
