package com.tcdt.qlnvhang.controller.xuatcuutro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuatcuutro.DeXuatCuuTroRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhQdGnvCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhQdGnvCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuatcuutro.DeXuatCuuTroService;
import com.tcdt.qlnvhang.service.xuatcuutro.QuyetDinhGnvCuuTroService;
import com.tcdt.qlnvhang.service.xuatcuutro.TongHopCuuTroService;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import com.tcdt.qlnvhang.table.XhQdCuuTroHdr;
import com.tcdt.qlnvhang.table.XhQdGnvCuuTroHdr;
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
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.XUAT_CUU_TRO + PathContains.QUYET_DINH_GIAO_NHIEM_VU)
@Api(tags = "Quản lý Quyết định giao nhiệm vụ xuất hàng cứu trợ viện trợ")
public class QuyetDinhGnvCuuTroController extends BaseController {
  @Autowired
  private DeXuatCuuTroRepository deXuatCuuTroRepository;
  @Autowired
  private DeXuatCuuTroService deXuatCuuTroService;
  @Autowired
  private QuyetDinhGnvCuuTroService quyetDinhGnvCuuTroService;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @ApiOperation(value = "Tạo mới", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> create(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhQdGnvCuuTroHdrSearchReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdGnvCuuTroHdr data = quyetDinhGnvCuuTroService.create(currentUser, req);
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

  @ApiOperation(value = "Cập nhật", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhQdGnvCuuTroHdrSearchReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdGnvCuuTroHdr data = quyetDinhGnvCuuTroService.update(currentUser, req);
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

  @ApiOperation(value = "Chi tiết", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(@CurrentUser CustomUserDetails currentUser, @PathVariable("id") Long id) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdGnvCuuTroHdr data = quyetDinhGnvCuuTroService.detail(currentUser, id);
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

  @ApiOperation(value = "Xoá", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      if (StringUtils.isEmpty(idSearchReq.getId()))
        throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

      Optional<XhDxCuuTroHdr> byId = deXuatCuuTroRepository.findById(idSearchReq.getId());
      if (!byId.isPresent())
        throw new Exception("Không tìm thấy dữ liệu cần xoá");

      deXuatCuuTroRepository.delete(byId.get());

      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Xoá danh sách", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      quyetDinhGnvCuuTroService.deleteListId(idSearchReq.getIds());
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error("Xoá thông tin hợp đồng trace: {}", e);
    }
    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tra cứu", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> search(@CurrentUser CustomUserDetails currentUser, @RequestBody XhQdGnvCuuTroHdrSearchReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {

      Page<XhQdGnvCuuTroHdr> dataPage = quyetDinhGnvCuuTroService.searchPage(currentUser, objReq);

      resp.setData(dataPage);
      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (

        Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Kết xuất", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(PathContains.URL_KET_XUAT)
  @ResponseStatus(HttpStatus.OK)
  public void exportToExcel(@CurrentUser CustomUserDetails currentUser, @RequestBody XhQdGnvCuuTroHdrSearchReq objReq, HttpServletResponse response)
      throws Exception {
    try {
      quyetDinhGnvCuuTroService.export(currentUser, objReq, response);
    } catch (Exception e) {
      // TODO: handle exception
      log.error("Tổng hợp phương án cứu trợ trace: {}", e);
      final Map<String, Object> body = new HashMap<>();
      body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      body.put("msg", e.getMessage());

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);
    }
  }

  @ApiOperation(value = "Cập nhật trạng thái phê duyệt", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> updateStatus(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody StatusReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      XhQdGnvCuuTroHdr data = quyetDinhGnvCuuTroService.updateStatus(currentUser, stReq);
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
}
