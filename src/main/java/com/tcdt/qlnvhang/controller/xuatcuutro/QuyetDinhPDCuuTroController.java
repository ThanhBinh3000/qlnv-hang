package com.tcdt.qlnvhang.controller.xuatcuutro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuatcuutro.DeXuatCuuTroRepository;
import com.tcdt.qlnvhang.repository.xuatcuutro.QuyetDinhCuuTroRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhQdCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhThCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuatcuutro.DeXuatCuuTroService;
import com.tcdt.qlnvhang.service.xuatcuutro.QuyetDinhCuuTroService;
import com.tcdt.qlnvhang.service.xuatcuutro.TongHopCuuTroService;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import com.tcdt.qlnvhang.table.XhQdCuuTroHdr;
import com.tcdt.qlnvhang.table.XhThCuuTroHdr;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.XUAT_CUU_TRO + PathContains.QUYET_DINH_PHE_DUYET)
@Api(tags = "Quản lý Đề xuất xuất cứu trợ viện trợ")
public class QuyetDinhPDCuuTroController extends BaseController {
  @Autowired
  private DeXuatCuuTroRepository deXuatCuuTroRepository;
  @Autowired
  private QuyetDinhCuuTroRepository quyetDinhCuuTroRepository;
  @Autowired
  private DeXuatCuuTroService deXuatCuuTroService;
  @Autowired
  private TongHopCuuTroService tongHopCuuTroService;
  @Autowired
  private QuyetDinhCuuTroService quyetDinhCuuTroService;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @ApiOperation(value = "Tạo mới Quyết định phê duyệt xuất cứu trợ viện trợ", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> create(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhQdCuuTroHdrSearchReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdCuuTroHdr data = quyetDinhCuuTroService.create(currentUser, req);
      resp.setData(data);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Cập nhật Quyết định phê duyệt xuất cứu trợ viện trợ", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhQdCuuTroHdrSearchReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdCuuTroHdr data = quyetDinhCuuTroService.update(currentUser, req);
      resp.setData(data);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Chi tiết Quyết định phê duyệt cứu trợ viện trợ", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(@CurrentUser CustomUserDetails currentUser, @PathVariable("id") Long id) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdCuuTroHdr data = quyetDinhCuuTroService.detail(currentUser, id);
      resp.setData(data);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá Quyết định phê duyệt cứu trợ viện trợ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      if (StringUtils.isEmpty(idSearchReq.getId()))
        throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

      Optional<XhQdCuuTroHdr> byId = quyetDinhCuuTroRepository.findById(idSearchReq.getId());
      if (!byId.isPresent())
        throw new Exception("Không tìm thấy dữ liệu cần xoá");

      quyetDinhCuuTroRepository.delete(byId.get());

      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá danh sách Quyết định phê duyệt cứu trợ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      quyetDinhCuuTroService.deleteListId(idSearchReq.getIds());
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin hợp đồng trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tra cứu Quyết định phê duyệt xuất cứu trợ viện trợ", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> search(@CurrentUser CustomUserDetails currentUser, @RequestBody XhQdCuuTroHdrSearchReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
      Page<XhQdCuuTroHdr> dataPage = quyetDinhCuuTroService.searchPage(currentUser, objReq);
      resp.setData(dataPage);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Kết xuất danh sách Quyết định phê duyệt xuất cứu trợ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(PathContains.URL_KET_XUAT)
  @ResponseStatus(HttpStatus.OK)
  public void exportToExcel(@CurrentUser CustomUserDetails currentUser, @RequestBody XhQdCuuTroHdrSearchReq objReq, HttpServletResponse response)
      throws Exception {
    try {
      quyetDinhCuuTroService.export(currentUser, objReq, response);
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

  @ApiOperation(value = "Danh sách phương án theo chi tiết đề xuất", response = List.class)
  @PostMapping(value = "/ds-phuong-an", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> listPhuongAn(@CurrentUser CustomUserDetails currentUser, @RequestBody XhDxCuuTroHdrSearchReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {
//      Page<XhDxCuuTroHdr> dataPage = deXuatCuuTroService.listPhuongAn(currentUser, objReq);

//      resp.setData(dataPage);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Cập nhật trạng thái phê duyệt", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> updateStatus(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody StatusReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdCuuTroHdr quyetDinhHdr = quyetDinhCuuTroService.updateStatus(currentUser, stReq);
      resp.setData(quyetDinhHdr);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }
    return ResponseEntity.ok(resp);
  }
}
