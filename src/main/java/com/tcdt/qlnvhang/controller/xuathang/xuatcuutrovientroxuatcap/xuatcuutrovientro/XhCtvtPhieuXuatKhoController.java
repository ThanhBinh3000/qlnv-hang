package com.tcdt.qlnvhang.controller.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoService;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = PathContains.XUAT_CUU_TRO +PathContains.XUAT_KHO+PathContains.PHIEU_XUAT_KHO)
@Slf4j
@Api(tags = "Xuất hàng DTQG - Xuất cứu trợ viện trợ - Phiếu xuất kho")

public class XhCtvtPhieuXuatKhoController extends BaseController {

  @Autowired
  XhCtvtPhieuXuatKhoService xhCtvtPhieuXuatKhoService;


  @ApiOperation(value = "Tra cứu thông tin đề xuất", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser,
                                                @RequestBody SearchXhCtvtPhieuXuatKho objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhCtvtPhieuXuatKhoService.searchPage(currentUser, objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (
        Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu thông tin : {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tạo mới thông tin đề xuất ", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhCtvtPhieuXuatKhoReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhCtvtPhieuXuatKhoService.save(currentUser, objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tạo mới thông tin  : {}", e);
    }
    return ResponseEntity.ok(resp);
  }


  @ApiOperation(value = "Cập nhật thông tin đề xuất", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhCtvtPhieuXuatKhoReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhCtvtPhieuXuatKhoService.update(currentUser, objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Cập nhật thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy chi tiết thông tin đề xuất", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(
      @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids") List<Long> ids) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhCtvtPhieuXuatKhoService.detail(ids).get(0));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy chi tiết thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> updateStatus(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody StatusReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      XhCtvtPhieuXuatKho result = xhCtvtPhieuXuatKhoService.approve(currentUser, stReq);
      resp.setData(result);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Phê duyệt thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }


  @ApiOperation(value = "Xoá thông tin đề xuất", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhCtvtPhieuXuatKhoService.delete(idSearchReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin : {}", e);
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá danh sách thông tin đề xuất", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhCtvtPhieuXuatKhoService.deleteMulti(idSearchReq);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Kết xuất danh sách mua", response = List.class)
  @PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void exportList(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody SearchXhCtvtPhieuXuatKho objReq, HttpServletResponse response) throws Exception {
    try {
      xhCtvtPhieuXuatKhoService.export(currentUser, objReq, response);

    } catch (Exception e) {
      log.error("Kết xuất danh sách dánh sách mua : {}", e);
      final Map<String, Object> body = new HashMap<>();
      body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      body.put("msg", e.getMessage());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);

    }
  }
  @ApiOperation(value = "Xem truoc", response = List.class)
  @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> preview(@RequestBody HashMap<String, Object> body) {
    BaseResponse resp = new BaseResponse();
    try {

      resp.setData(xhCtvtPhieuXuatKhoService.preview(body));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }
}