package com.tcdt.qlnvhang.controller.xuathang.suachuahang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.TrinhVaThamDinhReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.suachuahang.ScTrinhVaThamDinhServiceImpl;
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
@RequestMapping(PathContains.SUA_CHUA + PathContains.HO_SO)
@Slf4j
@Api(tags = "Sửa chữa hàng DTQG - Trình và thẩm định hàng DTQG cần sửa chữa")
public class ScTrinhThamDinhController {
  @Autowired
  ScTrinhVaThamDinhServiceImpl xhScHoSoService;

  @ApiOperation(value = "Tra cứu", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@RequestBody TrinhVaThamDinhReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhScHoSoService.searchPage(objReq));
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
  public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody TrinhVaThamDinhReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhScHoSoService.create(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tạo mới thông tin  : {}", e);
    }
    return ResponseEntity.ok(resp);
  }


  @ApiOperation(value = "Cập nhật", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> update(@Valid @RequestBody TrinhVaThamDinhReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhScHoSoService.update(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Cập nhật thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy chi tiết", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(@ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids") List<Long> ids) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhScHoSoService.detail(ids.get(0)));
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
  public ResponseEntity<BaseResponse> updateStatus(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody TrinhVaThamDinhReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhScHoSoService.approve(stReq);
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
  public ResponseEntity<BaseResponse> delete(@RequestParam("id") Long id) {
    BaseResponse resp = new BaseResponse();
    try {
      xhScHoSoService.delete(id);
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
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody DeleteReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      xhScHoSoService.deleteMulti(req.getIds());
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

//  @ApiOperation(value = "Kết xuất danh sách", response = List.class)
//  @PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
//  @ResponseStatus(HttpStatus.OK)
//  public void exportList(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhTlHoSoRequest objReq, HttpServletResponse response) throws Exception {
//    try {
//      xhScHoSoService.export(currentUser, objReq, response);
//
//    } catch (Exception e) {
//      log.error("Kết xuất danh sách dánh sách : {}", e);
//      final Map<String, Object> body = new HashMap<>();
//      body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//      body.put("msg", e.getMessage());
//      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//      response.setCharacterEncoding("UTF-8");
//      final ObjectMapper mapper = new ObjectMapper();
//      mapper.writeValue(response.getOutputStream(), body);
//
//    }
//  }

}
