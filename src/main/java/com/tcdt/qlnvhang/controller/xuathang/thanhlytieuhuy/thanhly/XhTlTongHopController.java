package com.tcdt.qlnvhang.controller.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.XUAT_THANH_LY + PathContains.TONG_HOP)
@Slf4j
@Api(tags = "Xuất hàng DTQG - Xuất thanh lý - Danh sách hàng cần thanh lý")
public class XhTlTongHopController {
  @Autowired
  XhTlTongHopService xhTlTongHopService;

  @ApiOperation(value = "Tra cứu", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser, @RequestBody XhTlTongHopRequest objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhTlTongHopService.searchPage(currentUser, objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu thông tin : {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tạo mới", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> insert(@Valid @RequestBody XhTlTongHopRequest objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhTlTongHopService.save(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tạo mới thông tin  : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy chi tiết", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(@ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("id") Long id) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhTlTongHopService.detail(id));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy chi tiết thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Trình duyệt", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> updateStatus(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody StatusReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhTlTongHopService.approve(currentUser, stReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Phê duyệt thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }


  @ApiOperation(value = "Xoá thông tin", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhTlTongHopService.delete(idSearchReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin : {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá danh sách thông tin", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhTlTongHopService.deleteMulti(idSearchReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  /*@ApiOperation(value = "Kết xuất danh sách", response = List.class)
  @PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void exportList(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody SearchXhTlQuyetDinh objReq, HttpServletResponse response) throws Exception {
    try {
      xhTlTongHopService.export(currentUser, objReq, response);

    } catch (Exception e) {
      log.error("Kết xuất danh sách dánh sách : {}", e);
      final Map<String, Object> body = new HashMap<>();
      body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      body.put("msg", e.getMessage());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);

    }
  }*/
}
