package com.tcdt.qlnvhang.controller.khlcnt;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.khlcnt.KhLuaChonNhaThauRes;
import com.tcdt.qlnvhang.service.khlcnt.KhLuaChonNhaThauService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PathContains.KH_LCNT_VT)
@Api(tags = "Quản lý kế hoạch lựa chọn nhà thầu vật tư")
public class KhLuaChonNhaThauController {
	@Autowired
	private KhLuaChonNhaThauService khLuaChonNhaThauService;

	@ApiOperation(value = "Tạo mới Kế hoạch lựa chọn nhà thầu vật tư", response = ListResponse.class)
	@PostMapping
	public final ResponseEntity<BaseResponse> create(@RequestBody KhLuaChonNhaThauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(khLuaChonNhaThauService.create(req));
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa Kế hoạch lựa chọn nhà thầu vật tư", response = KhLuaChonNhaThauRes.class)
	@PutMapping
	public final ResponseEntity<BaseResponse> update(@RequestBody KhLuaChonNhaThauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(khLuaChonNhaThauService.update(req));
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá Kế hoạch lựa chọn nhà thầu vật tư", response = Boolean.class)
	@DeleteMapping
	public final ResponseEntity<BaseResponse> delete(@RequestParam Long khLcntId) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(khLuaChonNhaThauService.delete(khLcntId));
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tìm kiếm Kế hoạch lựa chọn nhà thầu vật tư", response = ListResponse.class)
	@GetMapping
	public final ResponseEntity<BaseResponse> search(KhLuaChonNhaThauSearchReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(khLuaChonNhaThauService.search(req));
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Chi tiết Kế hoạch lựa chọn nhà thầu vật tư", response = KhLuaChonNhaThauRes.class)
	@GetMapping("/{id}")
	public final ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex) {
		BaseResponse resp = new BaseResponse();
		try {
			if (pageSize == null)
				pageSize = 10;

			if (pageIndex == null)
				pageIndex = 0;

			resp.setData(khLuaChonNhaThauService.getDetail(id, PageRequest.of(pageIndex, pageSize)));
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Gửi duyệt/Phê duyệt/Từ chối Kế hoạch lựa chọn nhà thầu vật tư", response = Boolean.class)
	@PutMapping("/status")
	public final ResponseEntity<BaseResponse> updateStatus(@RequestBody StatusReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(khLuaChonNhaThauService.updateStatus(req));
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}
