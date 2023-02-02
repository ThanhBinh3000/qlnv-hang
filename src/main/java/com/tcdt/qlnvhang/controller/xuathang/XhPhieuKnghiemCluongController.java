package com.tcdt.qlnvhang.controller.xuathang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuKnghiemCluongSearchReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import com.tcdt.qlnvhang.response.xuathang.phieuknghiemcluonghang.XhPhieuKnghiemCluongRes;
import com.tcdt.qlnvhang.service.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = PathContains.XUAT_HANG_PHIEU_KIEM_NGHIEM_CHAT_LUONG)
@Api(tags = "Quản lý Xuất hàng Phiếu Kiểm nghiệm chất lượng hàng")
public class XhPhieuKnghiemCluongController {
	@Autowired
	private XhPhieuKnghiemCluongService service;

	@ApiOperation(value = "Tạo mới Phiếu Kiểm nghiệm chất lượng hàng", response = BienBanLayMauRes.class)
	@PostMapping
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody XhPhieuKnghiemCluongReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			XhPhieuKnghiemCluongRes res = service.create(req);
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
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhPhieuKnghiemCluongReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			XhPhieuKnghiemCluongRes res = service.update(req);
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
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			Boolean res = service.delete(id);
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
	public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			XhPhieuKnghiemCluongRes res = service.detail(id);
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
			Boolean res = service.updateStatus(req);
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
	public ResponseEntity<BaseResponse> search(XhPhieuKnghiemCluongSearchReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			Page<XhPhieuKnghiemCluongRes> res = service.search(req);
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
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.deleteMultiple(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg("Delete multiple Phiếu Kiểm nghiệm chất lượng hàng lỗi");
			log.error("Delete multiple Phiếu Kiểm nghiệm chất lượng hàng lỗi", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export Phiếu Kiểm nghiệm chất lượng hàng", response = List.class)
	@PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody XhPhieuKnghiemCluongSearchReq req) {

		try {
			service.exportToExcel(req, response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}

	}
}


