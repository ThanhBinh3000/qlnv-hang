package com.tcdt.qlnvhang.controller.nhaphang.dauthau.hopdong;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.request.object.HhHopDongDtlReq;
import com.tcdt.qlnvhang.request.search.ListHdSearhReq;
import com.tcdt.qlnvhang.service.impl.HhHopDongServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhHopDongHdrReq;
import com.tcdt.qlnvhang.request.search.HhHopDongSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.HhHopDongService;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.DX_KH + PathContains.HOP_DONG)
@Api(tags = "Nhập hàng - Đấu thầu - Hợp đồng - Hợp đồng/Phụ lục hợp đồng")
public class HhHopDongController {

  @Autowired
  private HhHopDongService service;

  @Autowired
  private HhHopDongServiceImpl hhHopDongService;

  @ApiOperation(value = "Tạo mới thông tin ", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> insert(HttpServletRequest request, @Valid @RequestBody HhHopDongHdrReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.create(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tạo mới thông tin  trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Cập nhật thông tin", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> update(HttpServletRequest request, @Valid @RequestBody HhHopDongHdrReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.update(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Cập nhật thông tin trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy chi tiết thông tin", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(
      @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids") String ids) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.detail(ids));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy chi tiết thông tin trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      service.approve(stReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Phê duyệt thông tin trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá thông tin", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      service.delete(idSearchReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin trace: {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá danh sách thông tin", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      service.deleteMulti(idSearchReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin trace: {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy chi tiết thông tin theo số", response = List.class)
  @PostMapping(value = PathContains.URL_CHI_TIET + "/so-hd", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> findBySoHd(@Valid @RequestBody StrSearchReq strSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.findBySoHd(strSearchReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy chi tiết thông tin trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tra cứu thông tin", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(HttpServletResponse response,
                                                @Valid @RequestBody HhHopDongSearchReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.selectPage(objReq, response));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (

        Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu thông tin trace: {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "list hơp đồng select quyet dinh", response = List.class)
  @PostMapping(value = PathContains.LIST_HD, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> listHd(@Valid @RequestBody ListHdSearhReq obj) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(hhHopDongService.listHopDong(obj.getMaDvi(), obj.getLoaiVthh()));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (

        Exception e) {
      e.printStackTrace();
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu thông tin trace: {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Kết xuất danh sách mua", response = List.class)
  @PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void exportList(@Valid @RequestBody HhHopDongSearchReq objReq, HttpServletResponse response) throws Exception {
    try {
      service.exportList(objReq, response);

    } catch (Exception e) {
      log.error("Kết xuất danh sách dánh sách mua trace: {}", e);
      final Map<String, Object> body = new HashMap<>();
      body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      body.put("msg", e.getMessage());

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);

    }
  }

  @ApiOperation(value = "Danh sách chọn", response = List.class)
  @PostMapping(value = "/ds-qd-giao-nv-nh", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> lookupData( @Valid @RequestBody HhHopDongSearchReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.lookupData(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu thông tin trace: {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xem truoc", response = List.class)
  @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> preview(@RequestBody HhHopDongHdrReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(service.preview(req));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lưu số tiền tính phạt", response = List.class)
  @PostMapping(value = "/tinh-tien-phat", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> saveSoTienTinhPhat( @RequestBody HhHopDongDtlReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      service.saveSoTienTinhPhat(objReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lưu số tiền tính phạt trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }
}
