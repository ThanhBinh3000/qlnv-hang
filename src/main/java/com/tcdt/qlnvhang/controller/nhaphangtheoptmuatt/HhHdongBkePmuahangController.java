package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhHdongBkePmuahangService;
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
@RequestMapping(value = "/mua-truc-tiep")
@Slf4j
@Api(tags = "Hợp đồng bảng kê phiếu mua hàng")
public class HhHdongBkePmuahangController extends BaseController {
    @Autowired
    HhHdongBkePmuahangService hhHdongBkePmuahangService;


    @ApiOperation(value = "Tra cứu danh sách hợp đồng mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.HD_BK_PMH + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhHdongBkePmh objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhHdongBkePmuahangService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới danh sách hợp đồng mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.HD_BK_PMH + PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> save(@Valid @RequestBody HhHdongBkePmuahangReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhHdongBkePmuahangService.save(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật danh sách hợp đồng mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.HD_BK_PMH + PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> update(@Valid @RequestBody HhHdongBkePmuahangReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhHdongBkePmuahangService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhât: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin hợp đồng mua trực tiếp", response = List.class)
    @GetMapping(value =PathContains.HD_BK_PMH+ PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID hợp đồng mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhHdongBkePmuahangService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa đề xuất hợp đồng mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.HD_BK_PMH + PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhHdongBkePmuahangService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("xóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa dánh sách hợp đồng mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.HD_BK_PMH + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhHdongBkePmuahangService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa danh sách: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
    @ApiOperation(value = "Kết xuất danh sách hợp đồng mua trực tiếp", response = List.class)
    @PostMapping(value= PathContains.HD_BK_PMH + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhHdongBkePmh objReq, HttpServletResponse response) throws Exception{

        try {
            hhHdongBkePmuahangService.export(objReq,response);
        } catch (Exception e) {

            log.error("Kết xuất danh sách đề xuất kế hoạch mua trực tiếp: {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }

    }

    @ApiOperation(value = "Phê duyêt danh sách hợp đồng mua trực tiếp ", response = List.class)
    @PostMapping(value=PathContains.HD_BK_PMH + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> updateStatusUbtvqh(@Valid @RequestBody StatusReq statusReq, HttpServletRequest req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhHdongBkePmuahangService.approve(statusReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
