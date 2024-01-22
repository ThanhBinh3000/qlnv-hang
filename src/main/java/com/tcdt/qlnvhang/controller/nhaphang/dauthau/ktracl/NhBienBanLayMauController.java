package com.tcdt.qlnvhang.controller.nhaphang.dauthau.ktracl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import com.tcdt.qlnvhang.service.nhaphang.bbanlaymau.BienBanLayMauService;
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
@RequestMapping(value = PathContains.BBAN_LAY_MAU)
@Api(tags = "Nhập hàng - Đấu thầu - Kiểm tra chất lượng - Biên bản lấy mẫu/bàn giao mẫu")
public class NhBienBanLayMauController {
	@Autowired
	private BienBanLayMauService service;

	@ApiOperation(value = "Tạo mới", response = BienBanLayMauRes.class)
	@PostMapping(value=  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody BienBanLayMauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa thông tin", response = BienBanLayMauRes.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid @RequestBody BienBanLayMauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			BienBanLayMau res = service.update(req);
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

	@ApiOperation(value = "Xoá thông tin", response = Boolean.class)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(idSearchReq.getId());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Chi tiết thông tin", response = Page.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> detail(@PathVariable("ids") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(id));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Gửi duyệt/Duyệt/Từ chối thông tin", response = Boolean.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@RequestBody BienBanLayMauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.approve(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu thông tin", response = Page.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> search(@RequestBody BienBanLayMauReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.searchPage(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu thông tin ", response = List.class)
	@PostMapping(value = PathContains.DANH_SACH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> list(@RequestBody BienBanLayMauReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.searchList(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch ( Exception e) {
			e.printStackTrace();
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu thông tin : {?}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Delete multiple", response = List.class)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value=  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
		BaseResponse resp = new BaseResponse();
		try {
			service.deleteMulti(req.getIds());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg("Delete multiple lỗi");
			log.error("Delete multiple lỗi", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Export", response = List.class)
	@PostMapping(value=  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody HhQdNhapxuatSearchReq req) {
		try {
			service.exportBblm(req,response);
		} catch (Exception e) {
			log.error("Error can not export", e);
		}
	}

	@ApiOperation(value = "Xem trước", response = List.class)
	@PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> preview(@RequestBody BienBanLayMauReq objReq) {
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
}

