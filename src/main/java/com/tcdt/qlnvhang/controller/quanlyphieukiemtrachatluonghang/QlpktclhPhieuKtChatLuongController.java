package com.tcdt.qlnvhang.controller.quanlyphieukiemtrachatluonghang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongFilterRequestDto;
import com.tcdt.qlnvhang.request.phieuktracluong.QlpktclhPhieuKtChatLuongRequestDto;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.QL_PHIEU_KIEM_TRA_CHAT_LUONG_HANG_LT)
@Api(tags = "Nhập hàng - Đấu Thầu - Phiếu kiểm tra chất lượng")
public class QlpktclhPhieuKtChatLuongController {

	@Autowired
	private QlpktclhPhieuKtChatLuongService service;

	@ApiOperation(value = "Tạo mới phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
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
			log.error("Tạo mới phiếu kiểm tra chất lượng hàng lương thực", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
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
			log.error("Sửa phiếu kiểm tra chất lượng hàng lương thực", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
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
			log.error("Tra cứu phiếu kiểm tra chất lượng hàng lương thực", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Chi tiết phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@GetMapping(PathContains.URL_CHI_TIET + "/{id}")
	public final ResponseEntity<BaseResponse> getDetail(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(id));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
			log.error("Chi tiết phiếu kiểm tra chất lượng hàng lương thực error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Gửi duyệt/Phê duyệt/Từ chối phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@PostMapping(PathContains.URL_PHE_DUYET)
	public final ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody QlpktclhPhieuKtChatLuongRequestDto req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.approve(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
			log.error("Gửi duyệt/Phê duyệt/Từ chối phiếu kiểm tra chất lượng hàng lương thực error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
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
			log.error("Xóa phiếu kiểm tra chất lượng hàng lương thực error", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody QlpktclhPhieuKtChatLuongRequestDto req) {

		try {
			service.export(req);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}

	}

	@ApiOperation(value = "Delete multiple phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/delete/multiple")
	public final ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			service.deleteMulti(req.getIds());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			resp.setMsg(e.getMessage());
			log.error("Delete multiple phiếu kiểm tra chất lượng hàng lương thực lỗi ", e);
		}
		return ResponseEntity.ok(resp);
	}

//	@ApiOperation(value = "Get số phiếu kiểm tra chất lượng hàng lương thực", response = List.class)
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
//			log.error("Get số phiếu kiểm tra chất lượng hàng lương thực lỗi", e);
//		}
//		return ResponseEntity.ok(resp);
//	}
}
