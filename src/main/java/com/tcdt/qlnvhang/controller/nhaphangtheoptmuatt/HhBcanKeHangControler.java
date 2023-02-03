package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBcanKeHangHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhBcanKeHangReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhBcanKeHangService;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
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
@RequestMapping(value = "/bang-can-ke")
@Slf4j
@Api(tags = "Nhập hàng - Mua trực tiếp - nhập kho - bảng cân kê hàng")
public class HhBcanKeHangControler {

    @Autowired
    private HhBcanKeHangService hhBcanKeHangService;

    @ApiOperation(value = "Tra cứu bảng cân kê hàng mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.BANG_CAN_KE_MTT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhBcanKeHangReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBcanKeHangService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới bảng cân kê hàng mua trực tiếp", response = List.class)
    @PostMapping(value =PathContains.BANG_CAN_KE_MTT  + PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
                                               @RequestBody HhBcanKeHangHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBcanKeHangService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật bảng cân kê hàng mua trực tiếp", response = List.class)
    @PostMapping(value =PathContains.BANG_CAN_KE_MTT + PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid HttpServletRequest request,
                                               @RequestBody HhBcanKeHangHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBcanKeHangService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết bảng cân kê mua trực tiếp", response = List.class)
    @GetMapping(value =PathContains.BANG_CAN_KE_MTT + PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID", example = "1", required = true) @PathVariable("id") Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBcanKeHangService.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa bảng cân kê mua trực tiếp", response = List.class)
    @PostMapping(value = PathContains.BANG_CAN_KE_MTT + PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhBcanKeHangService.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách bảng cân kê mua trực tiếp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =PathContains.BANG_CAN_KE_MTT + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhBcanKeHangService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyêt bảng cân kê hàng mua trực tiếp  ", response = List.class)
    @PostMapping(value=PathContains.BANG_CAN_KE_MTT + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatusUbtvqh(@Valid @RequestBody StatusReq statusReq, HttpServletRequest req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBcanKeHangService.approve(statusReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách bảng kê cân hàng mua trực tiếp ", response = List.class)
    @PostMapping(value= PathContains.BANG_CAN_KE_MTT + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhBcanKeHangReq objReq, HttpServletResponse response) throws Exception{

        try {
            hhBcanKeHangService.export(objReq,response);
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

}
