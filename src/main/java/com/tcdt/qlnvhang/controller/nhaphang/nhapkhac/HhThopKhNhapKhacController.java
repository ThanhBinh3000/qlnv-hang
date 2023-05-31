package com.tcdt.qlnvhang.controller.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhThopKhNhapKhacService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.NHAP_KHAC + PathContains.TH_NK)
@Api(tags = "Nhập hàng - Nhập khác - Tổng hợp đề xuất Kế hoạch nhập khác")
public class HhThopKhNhapKhacController {
    @Autowired
    private HhThopKhNhapKhacService service;

    @ApiOperation(value = "Tra cứu", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> selectAll(@Valid HttpServletRequest request,
                                                  @RequestBody HhThopKhNhapKhacSearch objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.timKiem(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
    @ApiOperation(value = "Lấy danh sách đề xuất được tổng hợp", response = List.class)
    @PostMapping(value = PathContains.DS_DX_CHUA_TH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> layDsDxuatChuaTongHop(@Valid HttpServletRequest request,
                                                  @RequestBody HhThopKhNhapKhacSearch objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.layDsDxuatChuaTongHop(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy danh sách đề xuất được tổng hợp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
    @ApiOperation(value = "Tạo mới", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
                                               @RequestBody HhThopKhNhapKhacReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.themMoi(objReq));
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
