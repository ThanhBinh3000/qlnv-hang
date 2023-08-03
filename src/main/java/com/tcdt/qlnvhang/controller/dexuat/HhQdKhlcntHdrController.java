package com.tcdt.qlnvhang.controller.dexuat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.DX_KH + PathContains.QD_LCNT)
@Api(tags = "Nhập hàng - Đấu thầu - Kế hoạch lcnt - Quyết định phê duyệt kế hoạch lựa chọn nhà thầu")
public class HhQdKhlcntHdrController {

	@Autowired
	private HhQdKhlcntHdrService service;

	@ApiOperation(value = "Tạo mới Quyết định phê duyệt kế hoạch lựa chọn nhà thầu lương thực", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(
			@Valid @RequestBody HhQdKhlcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody HhQdKhlcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Cập nhật Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody HhQdKhlcntSearchReq objReq, HttpServletResponse response) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.getAllPage(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID phương án kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = "/chi-tiet-cuc/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailDtl( @PathVariable("ids") Long ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detailDtl(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/goi-thau/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailGoiThau(
			@ApiParam(value = "ID phương án kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detailGoiThau(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 Quyết định phê duyệt kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.approve(stReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Phê duyệt quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(idSearchReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xoá quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá danh sách quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> deleteMuilti(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.deleteMulti(idSearchReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xoá quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu theo số quyết định", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/so-qd" + "/{soQd}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailNumber(
			@ApiParam(value = "Số quyết định phê duyệt kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("soQd") String soQd) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detailNumber(soQd));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu theo số quyết định trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Kết xuất danh sách QĐ phê duyệt KHLCNT", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(PathContains.URL_KET_XUAT)
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(@Valid @RequestBody HhQdKhlcntSearchReq objReq, HttpServletResponse response)
			throws Exception {
		try {
			service.exportList(objReq, response);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Kết xuất danh sách gói thầu trace: {}", e);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			body.put("msg", e.getMessage());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		}
	}

	@ApiOperation(value = "Lấy tất cả Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TAT_CA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> getAll(HttpServletRequest request,
												  @Valid @RequestBody HhQdKhlcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.getAll(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xem trước", response = List.class)
	@PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> previewVt(@RequestBody HhQdKhlcntHdrReq objReq) {
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
