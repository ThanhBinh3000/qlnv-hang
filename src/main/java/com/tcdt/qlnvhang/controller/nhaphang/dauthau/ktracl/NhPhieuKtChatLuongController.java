package com.tcdt.qlnvhang.controller.nhaphang.dauthau.ktracl;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemtracl.NhPhieuKtChatLuongService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.QL_PHIEU_KIEM_TRA_CHAT_LUONG_HANG_LT)
@Api(tags = "Nhập hàng - Đấu Thầu - Kiểm tra chất lượng - Phiếu kiểm tra chất lượng")
public class NhPhieuKtChatLuongController {

	@Autowired
	private NhPhieuKtChatLuongService service;

	@ApiOperation(value = "Tạo mới", response = List.class)
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
			log.error("Tạo mới", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody QlpktclhPhieuKtChatLuongRequestDto request) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Sửa", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(@RequestBody QlpktclhPhieuKtChatLuongRequestDto req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.searchPage(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Chi tiết", response = List.class)
	@GetMapping(PathContains.URL_CHI_TIET + "/{id}")
	public ResponseEntity<BaseResponse> getDetail(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(id));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
			log.error("Chi tiết error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Gửi duyệt/Phê duyệt/Từ chối", response = List.class)
	@PostMapping(PathContains.URL_PHE_DUYET)
	public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody QlpktclhPhieuKtChatLuongRequestDto req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.approve(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
			log.error("Gửi duyệt/Phê duyệt/Từ chối error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa", response = List.class)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> deleteQd(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(idSearchReq.getId());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
			log.error("Xóa error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody QlpktclhPhieuKtChatLuongRequestDto req) {

		try {
			service.export(req,response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}

	}

	@ApiOperation(value = "Delete multiple", response = List.class)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("so-luong-nhap-kho")
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody QlpktclhPhieuKtChatLuongRequestDto req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.getSoLuongNhapKho(req.getIdDdiemGiaoNvNh()));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			resp.setMsg(e.getMessage());
			log.error("Delete multiple lỗi ", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/delete/multiple")
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			service.deleteMulti(req.getIds());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			resp.setMsg(e.getMessage());
			log.error("Delete multiple lỗi ", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xem trước", response = List.class)
	@PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> preview(@RequestBody QlpktclhPhieuKtChatLuongRequestDto objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.preview(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xem trước: {?}", e);
		}
		return ResponseEntity.ok(resp);
	}

//	@ApiOperation(value = "Get số", response = List.class)
//	@GetMapping("/so")
//	public ResponseEntity<BaseResponse> getSo() {
//		BaseResponse resp = new BaseResponse();
//		try {
//			resp.setData(service.getSo());
//			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//		} catch (Exception e) {
//			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//			resp.setMsg(e.getMessage());
//			log.error("Get số lỗi", e);
//		}
//		return ResponseEntity.ok(resp);
//	}
}
