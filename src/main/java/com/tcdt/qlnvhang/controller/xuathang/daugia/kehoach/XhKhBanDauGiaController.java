package com.tcdt.qlnvhang.controller.xuathang.daugia.kehoach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaService;
import com.tcdt.qlnvhang.util.DataUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.DAU_GIA + PathContains.DX_KH_BDG)
@Slf4j
@Api(tags = "Xuất hàng - Bán đấu giá - Kế hoạch bán đấu giá - Đề xuất kế hoạch bán đấu giá ")
public class XhKhBanDauGiaController extends BaseController {
  @Autowired
  private XhDxKhBanDauGiaService xhDxKhBanDauGiaService;

  @ApiOperation(value = "Tra cứu ", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody XhDxKhBanDauGiaReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.searchPage(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tra cứu: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tạo mới ", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> save(@Valid @RequestBody XhDxKhBanDauGiaReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.create(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Tạo mới: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Sửa ", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhDxKhBanDauGiaReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.update(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Cập nhật: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy chi tiết", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(
      @ApiParam(value = "ID", example = "1", required = true) @PathVariable("id") Long id) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.detail(id));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy chi tiết trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xóa ", response = List.class)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhDxKhBanDauGiaService.delete(idSearchReq.getId());
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("xóa: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xóa dánh sách ", response = List.class)
  @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      xhDxKhBanDauGiaService.deleteMulti(idSearchReq.getIdList());
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xóa danh sách: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Kết xuất danh sách ", response = List.class)
  @PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void exportListQdBtcBnToExcel(@Valid @RequestBody XhDxKhBanDauGiaReq objReq, HttpServletResponse response) throws Exception {

    try {
      xhDxKhBanDauGiaService.export(objReq, response);
    } catch (Exception e) {

      log.error("Kết xuất danh sách : {}", e);
      final Map<String, Object> body = new HashMap<>();
      body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      body.put("msg", e.getMessage());

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);
    }

  }

  @ApiOperation(value = "Phê duyêt", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> updateStatus(@RequestBody XhDxKhBanDauGiaReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.approve(stReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      // TODO: handle exception
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Phê duyệt trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa", response = List.class)
  @PostMapping(value = "/count-sl-kh", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> getCountSl(HttpServletRequest request,
                                                 @Valid @RequestBody CountKhlcntSlReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.countSoLuongKeHoachNam(objReq));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Lấy giá bán tối thiểu", response = List.class)
  @GetMapping(value = PathContains.GIA_BAN_TOI_THIEU + "/{cloaiVthh}/{maDvi}/{namKhoach}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> getGiaBanToiDa(
      @ApiParam(value = "cloaiVthh", example = "010101", required = true) @PathVariable("cloaiVthh") String cloaiVthh,
      @ApiParam(value = "maDvi", example = "010101", required = true) @PathVariable("maDvi") String maDvi,
      @ApiParam(value = "namKhoach", example = "2023", required = true) @PathVariable("namKhoach") Integer namKhoach) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.getGiaBanToiThieu(cloaiVthh, maDvi, namKhoach));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Lấy giá bán tối đa trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xem truoc", response = List.class)
  @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> preview(@RequestBody HashMap<String, Object> request) {
    BaseResponse resp = new BaseResponse();
    try {
      resp.setData(xhDxKhBanDauGiaService.preview(DataUtils.safeToString(request.get("tenBaoCao"))));
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }
}
