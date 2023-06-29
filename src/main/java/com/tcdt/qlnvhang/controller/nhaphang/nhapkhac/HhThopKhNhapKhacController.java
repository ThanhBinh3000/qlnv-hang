package com.tcdt.qlnvhang.controller.nhaphang.nhapkhac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhThopKhNhapKhacService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
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

    @ApiOperation(value = "Cập nhật", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid HttpServletRequest request,
                                               @RequestBody HhThopKhNhapKhacReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.capNhat(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật trace: {}", e);
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
            resp.setData(service.chiTiet(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy danh sách tổng hợp chưa được tạo quyết định", response = List.class)
    @GetMapping(value = "/ds-th-chua-tao-qd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> layDsTongHopChuaTaoQd() {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.layDsTongHopChuaTaoQd());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            service.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            service.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách đề xuất kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(PathContains.URL_KET_XUAT)
    @ResponseStatus(HttpStatus.OK)
    public void exportDsKhlcnt(@Valid @RequestBody HhThopKhNhapKhacSearch searchReq, HttpServletResponse response)
            throws Exception {
        try {
            service.exportList(searchReq, response);
        } catch (Exception e) {
            log.error("Kết xuất danh sách đề xuất kế hoạch lựa chọn nhà thầu trace: {}", e);
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
