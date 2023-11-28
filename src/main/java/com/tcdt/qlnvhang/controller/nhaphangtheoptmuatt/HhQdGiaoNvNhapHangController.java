package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdGiaoNvNhapHangReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhQdGiaoNvNhReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhQdGiaoNvNhapHangService;
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
@Api(tags = "Quyết định giao nhiệm vụ nhập hàng")
public class HhQdGiaoNvNhapHangController {
    
    @Autowired
    private HhQdGiaoNvNhapHangService hhQdGiaoNvNhapHangService;


    @ApiOperation(value = "Tra cứu ", response = List.class)
    @PostMapping(value=  PathContains.QD_NV_NH + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhQdGiaoNvNhReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdGiaoNvNhapHangService.searchPage(objReq));
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
    @PostMapping(value=PathContains.QD_NV_NH+ PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody HhQdGiaoNvNhapHangReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdGiaoNvNhapHangService.save(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật ", response = List.class)
    @PostMapping(value=PathContains.QD_NV_NH+ PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody HhQdGiaoNvNhapHangReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdGiaoNvNhapHangService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin ", response = List.class)
    @GetMapping(value =PathContains.QD_NV_NH+ PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID ", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdGiaoNvNhapHangService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa đề xuất ", response = List.class)
    @PostMapping(value=  PathContains.QD_NV_NH + PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhQdGiaoNvNhapHangService.delete(idSearchReq);
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
    @PostMapping(value=  PathContains.QD_NV_NH + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhQdGiaoNvNhapHangService.deleteMulti(idSearchReq);
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
    @PostMapping(value= PathContains.QD_NV_NH + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhQdGiaoNvNhReq objReq, HttpServletResponse response) throws Exception{

        try {
            hhQdGiaoNvNhapHangService.export(objReq,response);
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

    @ApiOperation(value = "Phê duyêt đề xuất kế hoạch mưa trực tiếp ", response = List.class)
    @PostMapping(value=PathContains.QD_NV_NH + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatusUbtvqh(@Valid @RequestBody StatusReq statusReq, HttpServletRequest req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdGiaoNvNhapHangService.approve(statusReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
    @ApiOperation(value = "Cập nhật địa điểm  nhập hàng", response = List.class)
    @PostMapping(value=PathContains.QD_NV_NH+ "/cap-nhat-ddiem-nhap", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateDdiem(@Valid @RequestBody HhQdGiaoNvNhapHangReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhQdGiaoNvNhapHangService.updateDiem(objReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xem truoc", response = List.class)
    @PostMapping(value = PathContains.QD_NV_NH + PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody HhQdGiaoNvNhapHangReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdGiaoNvNhapHangService.preview(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
