package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxuatKhMttHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhDxKhMttHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhPhieuNhapKhoHdrService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/phieu-nhap-kho")
@Slf4j
@Api(tags = "Nhập hàng - Mua trực tiếp - nhập kho - phiếu nhập kho")
public class HhPhieuNhapKhoControler {

    @Autowired
    private HhPhieuNhapKhoHdrService hhPhieuNhapKhoHdrService;

    @ApiOperation(value = "Tra cứu phiếu nhập kho mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.PHIEU_NHAP_KHO_MTT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhPhieuNhapKhoReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPhieuNhapKhoHdrService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới phiếu nhập kho mua trực tiếp", response = List.class)
    @PostMapping(value =PathContains.PHIEU_NHAP_KHO_MTT  + PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
                                               @RequestBody HhPhieuNhapKhoHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPhieuNhapKhoHdrService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

}
