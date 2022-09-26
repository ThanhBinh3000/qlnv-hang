package com.tcdt.qlnvhang.controller.xuatcuutro;

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDxkhXuatKhacHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhXuatKhacDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhXuatKhacHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvDxkhXuatKhacSearchReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvDxkhXuatKhacSpecification;
import com.tcdt.qlnvhang.service.xuatcuutro.DeXuatCuuTroService;
import com.tcdt.qlnvhang.table.QlnvDxkhXuatKhacDtl;
import com.tcdt.qlnvhang.table.QlnvDxkhXuatKhacHdr;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.XUAT_CUU_TRO + PathContains.XAY_DUNG_PHUONG_AN_DE_XUAT)
@Api(tags = "Quản lý Đề xuất xuất cứu trợ viện trợ")
public class DeXuatCuuTroController extends BaseController {
  @Autowired
  private QlnvDxkhXuatKhacHdrRepository qlnvDxkhXuatKhacHdrRepository;
  @Autowired
  private DeXuatCuuTroService deXuatCuuTroService;

  @ApiOperation(value = "Tạo mới Đề xuất xuất cứu trợ viện trợ", response = List.class)
  @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> create(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhDxCuuTroHdrSearchReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      XhDxCuuTroHdr data = deXuatCuuTroService.create(currentUser, req);
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

  @ApiOperation(value = "Cập nhật Đề xuất xuất cứu trợ viện trợ", response = List.class)
  @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public final ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhDxCuuTroHdrSearchReq req) {
    BaseResponse resp = new BaseResponse();
    try {
      XhDxCuuTroHdr data = deXuatCuuTroService.update(currentUser, req);
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

  @ApiOperation(value = "Chi tiết Đề xuất xuất cứu trợ viện trợ", response = List.class)
  @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public final ResponseEntity<BaseResponse> detail(@CurrentUser CustomUserDetails currentUser, @PathVariable("id") Long id) {
    BaseResponse resp = new BaseResponse();
    try {
      XhDxCuuTroHdr data = deXuatCuuTroService.detail(currentUser, id);
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

  @ApiOperation(value = "Xoá thông tin Đề xuất kế hoạch xuất khác", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
    BaseResponse resp = new BaseResponse();
    try {
      if (StringUtils.isEmpty(idSearchReq.getId()))
        throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

      Optional<QlnvDxkhXuatKhacHdr> qOptional = qlnvDxkhXuatKhacHdrRepository.findById(idSearchReq.getId());
      if (!qOptional.isPresent())
        throw new Exception("Không tìm thấy dữ liệu cần xoá");

      qlnvDxkhXuatKhacHdrRepository.delete(qOptional.get());

      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (Exception e) {
      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
      resp.setMsg(e.getMessage());
      log.error(e.getMessage());
    }

    return ResponseEntity.ok(resp);
  }

  @ApiOperation(value = "Tra cứu Đề xuất xuất cứu trợ viện trợ", response = List.class)
  @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> search(@CurrentUser CustomUserDetails currentUser,@RequestBody XhDxCuuTroHdrSearchReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {

      Page<XhDxCuuTroHdr> dataPage = deXuatCuuTroService.searchPage(currentUser, objReq);

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

  @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Đề xuất kế hoạch xuất khác", response = List.class)
  @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseResponse> approve(HttpServletRequest request, @Valid @RequestBody StatusReq stReq) {
    BaseResponse resp = new BaseResponse();
    try {
      if (StringUtils.isEmpty(stReq.getId()))
        throw new Exception("Không tìm thấy dữ liệu");

      Optional<QlnvDxkhXuatKhacHdr> qOptional = qlnvDxkhXuatKhacHdrRepository.findById(Long.valueOf(stReq.getId()));
      if (!qOptional.isPresent())
        throw new Exception("Không tìm thấy dữ liệu");

      String status = stReq.getTrangThai();
      switch (status) {
        case Contains.CHO_DUYET:
          qOptional.get().setNguoiGuiDuyet(getUserName(request));
          qOptional.get().setNgayGuiDuyet(getDateTimeNow());
          break;
        case Contains.DUYET:
          qOptional.get().setNguoiPduyet(getUserName(request));
          qOptional.get().setNgayPduyet(getDateTimeNow());
          break;
        case Contains.TU_CHOI:
          qOptional.get().setLdoTuchoi(stReq.getLyDo());
          break;
        default:
          break;
      }

      qOptional.get().setTrangThai(stReq.getTrangThai());
      qlnvDxkhXuatKhacHdrRepository.save(qOptional.get());

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
