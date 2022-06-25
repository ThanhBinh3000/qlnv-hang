package com.tcdt.qlnvhang.controller.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.PhieuKnghiemCluongHangReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.PhieuKnghiemCluongHangRes;
import com.tcdt.qlnvhang.service.phieuknghiemcluonghang.PhieuKnghiemCluongHangService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping(value = PathContains.KNGHIEM_CLUONG)
@Api(tags = "Quản lý Phiếu Kiểm nghiệm chất lượng hàng")
public class PhieuKnghiemCluongHangController {
	@Autowired
	private PhieuKnghiemCluongHangService phieuKnghiemCluongHangService;

	@ApiOperation(value = "Tạo mới Phiếu Kiểm nghiệm chất lượng hàng", response = BienBanLayMauRes.class)
	@PostMapping
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody PhieuKnghiemCluongHangReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			PhieuKnghiemCluongHangRes res = phieuKnghiemCluongHangService.create(req);
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

	@ApiOperation(value = "Sửa thông tin Phiếu Kiểm nghiệm chất lượng hàng", response = BienBanLayMauRes.class)
	@PutMapping
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody PhieuKnghiemCluongHangReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			PhieuKnghiemCluongHangRes res = phieuKnghiemCluongHangService.update(req);
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

	@ApiOperation(value = "Xoá thông tin Phiếu Kiểm nghiệm chất lượng hàng", response = Boolean.class)
	@DeleteMapping
	public ResponseEntity<BaseResponse> delete(@RequestParam("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = phieuKnghiemCluongHangService.delete(id);
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

	@ApiOperation(value = "Chi tiết Phiếu Kiểm nghiệm chất lượng hàng", response = Page.class)
	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse> detail(@RequestParam("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			PhieuKnghiemCluongHangRes res = phieuKnghiemCluongHangService.detail(id);
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

	@ApiOperation(value = "Gửi duyệt/Duyệt/Từ chối Phiếu Kiểm nghiệm chất lượng hàng", response = Boolean.class)
	@PutMapping("/status")
	public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = phieuKnghiemCluongHangService.updateStatus(req);
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

	@ApiOperation(value = "Tra cứu thông tin Phiếu Kiểm nghiệm chất lượng hàng", response = Page.class)
	@GetMapping
	public ResponseEntity<BaseResponse> search(PhieuKnghiemCluongHangSearchReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Page<PhieuKnghiemCluongHangRes> res = phieuKnghiemCluongHangService.search(req);
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

	@ApiOperation(value = "Delete multiple Phiếu Kiểm nghiệm chất lượng hàng", response = List.class)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/delete/multiple")
	public final ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(phieuKnghiemCluongHangService.deleteMultiple(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg("Delete multiple Phiếu Kiểm nghiệm chất lượng hàng lỗi");
			log.error("Delete multiple Phiếu Kiểm nghiệm chất lượng hàng lỗi", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export biên bản nhập đầy kho lương thực", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody PhieuKnghiemCluongHangSearchReq req) {

		try {
			response.setContentType("application/octet-stream");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=phieu_kiem_nghiem_chat_luong_hang_" + currentDateTime + ".xlsx";
			response.setHeader(headerKey, headerValue);
			phieuKnghiemCluongHangService.exportToExcel(req, response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}

	}
}


