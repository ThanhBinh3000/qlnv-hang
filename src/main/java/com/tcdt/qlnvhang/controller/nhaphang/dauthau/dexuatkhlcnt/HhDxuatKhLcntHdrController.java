package com.tcdt.qlnvhang.controller.nhaphang.dauthau.dexuatkhlcnt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.HhDxuatKhLcntHdrService;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.DX_KH + PathContains.DX_KH_LCNT)
@Api(tags = "Đề xuất kế hoạch lựa chọn nhà thầu lương thực và vật tư")
public class HhDxuatKhLcntHdrController {

	@Autowired
	private HhDxuatKhLcntHdrService service;

	@ApiOperation(value = "Tạo mới đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
			@RequestBody HhDxuatKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.create(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid HttpServletRequest request,
			@RequestBody HhDxuatKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.update(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Cập nhật đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid HttpServletRequest request,
			@RequestBody HhDxuatKhLcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.timKiem(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu đề xuất kế hoạch lựa chọn nhà thầu lương thực from quyết định", response = List.class)
	@PostMapping(value = "/select-dropdown", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectDropdown( @RequestBody HhDxuatKhLcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.selectDropdown(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tra cứu đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID đề xuất kế hoạch lựa chọn nhà thầu lương thực", example = "1", required = true) @PathVariable("id") Long id) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detail(id));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.delete(idSearchReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xoá đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá danh sách đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> deleteMulti(@RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			service.deleteMulti(idSearchReq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Xoá đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 đề xuất kế hoạch lựa chọn nhà thầu lương thực", response = List.class)
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
			log.error("Phê duyệt đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Kết xuất danh sách gói thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(PathContains.URL_KET_XUAT + "/ds-goi-thau")
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response)
			throws Exception {
		try {
			service.exportToExcel(searchReq, response);
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

	@ApiOperation(value = "Kết xuất danh sách đề xuất kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(PathContains.URL_KET_XUAT)
	@ResponseStatus(HttpStatus.OK)
	public void exportDsKhlcnt(@Valid @RequestBody HhDxuatKhLcntSearchReq searchReq, HttpServletResponse response)
			throws Exception {
		try {
			service.exportList(searchReq, response);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Kết xuất danh sách đề xuất kế hoạch lựa chọn nhà thầu trace: {}", e);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			body.put("msg", e.getMessage());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		}
	}

	@ApiOperation(value = "Tạo mới đề xuất kế hoạch lựa chọn nhà thầu vật tư", response = List.class)
	@PostMapping(value = PathContains.VAT_TU+PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insertVatTu(@Valid HttpServletRequest request,
											   @RequestBody HhDxuatKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.createVatTu(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhập đề xuất kế hoạch lựa chọn nhà thầu vật tư", response = List.class)
	@PostMapping(value = PathContains.VAT_TU+PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> updateVatTu(@Valid HttpServletRequest request,
													@RequestBody HhDxuatKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.updateVatTu(objReq));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Tạo mới đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu vật tư", response = List.class)
	@GetMapping(value = PathContains.VAT_TU+PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailVatTu(
			@ApiParam(value = "ID đề xuất kế hoạch lựa chọn nhà thầu lương thực", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			resp.setData(service.detailVatTu(ids));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error("Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu lương thực trace: {}", e);
		}
		return ResponseEntity.ok(resp);
	}

}
