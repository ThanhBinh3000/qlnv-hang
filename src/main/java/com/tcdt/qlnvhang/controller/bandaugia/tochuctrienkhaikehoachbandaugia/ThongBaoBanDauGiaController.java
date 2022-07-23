package com.tcdt.qlnvhang.controller.bandaugia.tochuctrienkhaikehoachbandaugia;


import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaResponse;
import com.tcdt.qlnvhang.service.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/thong-bao-ban-dau-gia")
@Slf4j
@Api(tags = "Thông báo đấu giá tài sản")
@RequiredArgsConstructor
public class ThongBaoBanDauGiaController extends BaseController {
	private final ThongBaoBanDauGiaService thongBaoBanDauGiaService;

	@ApiOperation(value = "Tạo mới thông báo bán đấu giá", response = ThongBaoBanDauGiaResponse.class)
	@PostMapping()
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody ThongBaoBanDauGiaRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			ThongBaoBanDauGiaResponse res = thongBaoBanDauGiaService.create(req);
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

	@ApiOperation(value = "Sửa thông báo bán đấu giá", response = ThongBaoBanDauGiaResponse.class)
	@PutMapping()
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody ThongBaoBanDauGiaRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			ThongBaoBanDauGiaResponse res = thongBaoBanDauGiaService.update(req);
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

	@ApiOperation(value = "Xoá thông báo bán đấu giá", response = Boolean.class)
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = thongBaoBanDauGiaService.delete(id);
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

	@ApiOperation(value = "Xoá danh sách thông báo bán đấu giá", response = Boolean.class)
	@DeleteMapping()
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = thongBaoBanDauGiaService.deleteMultiple(req.getIds());
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

//	@ApiOperation(value = "Search thông báo bán đấu giá", response = Page.class)
//	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<BaseResponse> search(KeHoachBanDauGiaSearchRequest req) {
//		BaseResponse resp = new BaseResponse();
//		try {
//			Page<KeHoachBanDauGiaResponse> res = thongBaoBanDauGiaService.search(req);
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
//
//	@ApiOperation(value = "Update trạng thái kế hoạch bán đấu giá hàng hóa", response = Page.class)
//	@PutMapping(value = "/trang-thai", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<BaseResponse> updateTrangThai(@RequestParam Long id,
//														@RequestParam String trangThaiId) {
//		BaseResponse resp = new BaseResponse();
//		try {
//			KeHoachBanDauGiaResponse res = keHoachBanDauGiaService.updateTrangThai(id, trangThaiId);
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
//
//	@ApiOperation(value = "Export kế hoạch bán đấu giá hàng hóa", response = List.class)
//	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseStatus(HttpStatus.OK)
//	public void exportToExcel(HttpServletResponse response, @RequestBody KeHoachBanDauGiaSearchRequest req) {
//
//		try {
//			keHoachBanDauGiaService.exportToExcel(req, response);
//		} catch (Exception e) {
//			log.error("Error can not export", e);
//		}
//	}
}
