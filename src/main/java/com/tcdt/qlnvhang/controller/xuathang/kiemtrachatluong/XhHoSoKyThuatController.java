package com.tcdt.qlnvhang.controller.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchHoSoKyThuatReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.kiemtrachatluong.XhHoSoKyThuatService;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat.XhHoSoKyThuatHdr;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = PathContains.KIEM_TRA_CHAT_LUONG + PathContains.HO_SO_KY_THUAT)
@Slf4j
@Api(tags = "Xuất hàng DTQG - Xuất cứu trợ viện trợ - Phiếu kiểm nghiệm chất lượng")
public class XhHoSoKyThuatController extends BaseController {

  @Autowired
  XhHoSoKyThuatService xhHoSoKyThuatService;


  @ApiOperation(value = "Tra cứu thông tin đề xuất", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser, @RequestBody SearchHoSoKyThuatReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhHoSoKyThuatService.searchPage(currentUser, objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu thông tin : {}", e);
    }

    return ResponseEntity.ok(resp);
  }


  @ApiOperation(value = "Tạo mới thông tin đề xuất ", response = List.class)
  @PostMapping(value =  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @RequestBody XhHoSoKyThuatHdr objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhHoSoKyThuatService.create(currentUser,objReq));
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
  public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @RequestBody XhHoSoKyThuatHdr objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhHoSoKyThuatService.update(currentUser, objReq));
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
  @PostMapping(value = PathContains.URL_CHI_TIET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detailXh(@CurrentUser CustomUserDetails currentUser, @RequestBody SearchHoSoKyThuatReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhHoSoKyThuatService.detailXh(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy chi tiết thông tin : {}", e);
    }
    return ResponseEntity.ok(resp);
  }
//
//  @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin", response = List.class)
//  @PostMapping(value =  PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<BaseResponse> updateStatus( @CurrentUser CustomUserDetails currentUser,@Valid @RequestBody StatusReq stReq) {
//    BaseResponse resp = new BaseResponse();
//    try {
//      xhCtvtPhieuKnClHdrService.approve(currentUser,stReq);
//      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//    } catch (Exception e) {
//      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//      resp.setMsg(e.getMessage());
//      log.error("Phê duyệt thông tin : {}", e);
//    }
//    return ResponseEntity.ok(resp);
//  }
//
//
//  @ApiOperation(value = "Xoá thông tin đề xuất", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
//  @PostMapping(value =  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
//    BaseResponse resp = new BaseResponse();
//    try {
//      xhCtvtPhieuKnClHdrService.delete(idSearchReq);
//      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//    } catch (Exception e) {
//      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//      resp.setMsg(e.getMessage());
//      log.error("Xoá thông tin : {}", e);
//    }
//
//    return ResponseEntity.ok(resp);
//  }
//
//  @ApiOperation(value = "Xoá danh sách thông tin đề xuất", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
//  @PostMapping(value =  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
//    BaseResponse resp = new BaseResponse();
//    try {
//      xhCtvtPhieuKnClHdrService.deleteMulti(idSearchReq);
//      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//    } catch (Exception e) {
//      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//      resp.setMsg(e.getMessage());
//      log.error("Xoá thông tin : {}", e);
//    }
//    return ResponseEntity.ok(resp);
//  }
//
//  @ApiOperation(value = "Kết xuất danh sách mua", response = List.class)
//  @PostMapping(value =  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
//  @ResponseStatus(HttpStatus.OK)
//  public void exportList(@CurrentUser CustomUserDetails currentUser ,@Valid @RequestBody  SearchXhCtvtPhieuKnCl objReq, HttpServletResponse response) throws Exception {
//    try {
//      xhCtvtPhieuKnClHdrService.export( currentUser,objReq, response);
//
//    } catch (Exception e) {
//      log.error("Kết xuất danh sách dánh sách mua : {}", e);
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
@ApiOperation(value = "Xem truoc", response = List.class)
@PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<BaseResponse> preview(@RequestBody HashMap<String, Object> body) {
  BaseResponse resp = new BaseResponse();
  try {

    resp.setData(xhHoSoKyThuatService.preview(body));
    resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
    resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
  } catch (Exception e) {
    resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
    resp.setMsg(e.getMessage());
  }
  return ResponseEntity.ok(resp);
}
}