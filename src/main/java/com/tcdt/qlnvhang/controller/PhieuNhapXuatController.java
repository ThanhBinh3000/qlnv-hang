package com.tcdt.qlnvhang.controller;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.search.QlnvQdKQDGHangSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.PhieuNhapXuatService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.NHAP_XUAT_HISTORY)
@Api(tags = "Lích sử phiếu nhập xuất")
public class PhieuNhapXuatController {
    @Autowired
    private PhieuNhapXuatService phieuNhapXuatService;

    @ApiOperation(value = "Tra cứu nhập xuất thẻ kho", response = List.class)
    @PostMapping(value = "/ds-nhap-xuat-the-kho", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdKQDGHangSearchReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
            resp.setData(phieuNhapXuatService.listPhieuTheKhoCt(objReq));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }

        return ResponseEntity.ok(resp);
    }
}
