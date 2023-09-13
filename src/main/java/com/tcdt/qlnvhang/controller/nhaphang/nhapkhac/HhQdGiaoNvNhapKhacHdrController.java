package com.tcdt.qlnvhang.controller.nhaphang.nhapkhac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacSearch;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacSearch;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhQdGiaoNvNhapKhacService;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhQdPdNhapKhacService;
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
@RequestMapping(value = PathContains.NHAP_KHAC + PathContains.QD_PDUYET_GIAO_NV_NK)
@Api(tags = "Nhập hàng - Nhập khác - Đề xuất Kế hoạch nhập khác")
public class HhQdGiaoNvNhapKhacHdrController {
    @Autowired
    private HhQdGiaoNvNhapKhacService service;
    @ApiOperation(value = "Tạo mới", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
                                               @RequestBody HhQdGiaoNvuNhapKhacHdrReq objReq) {
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
                                               @RequestBody HhQdGiaoNvuNhapKhacHdrReq objReq) {
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

    @ApiOperation(value = "Tra cứu", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> selectAll(@Valid HttpServletRequest request,
                                                  @RequestBody HhQdGiaoNvuNhapKhacSearch objReq) {
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

    @ApiOperation(value = "Tra cứu", response = List.class)
    @PostMapping(value = "/ds-qd-nvu-duoc-lap-bb", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> dsQdNvuDuocLapBb(@Valid HttpServletRequest request,
                                                  @RequestBody HhQdGiaoNvuNhapKhacSearch objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.dsQdNvuDuocLapBb(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu trace: {}", e);
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
    @ApiOperation(value = "Xoá", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            service.xoa(idSearchReq);
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
            service.xoaNhieu(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04", response = List.class)
    @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.pheDuyet(stReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
    @ApiOperation(value = "Kết xuất danh sách đề xuất kế hoạch nhập khác", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(PathContains.URL_KET_XUAT)
    @ResponseStatus(HttpStatus.OK)
    public void export(@Valid @RequestBody HhQdGiaoNvuNhapKhacSearch searchReq, HttpServletResponse response)
            throws Exception {
        try {
            service.xuatFile(searchReq, response);
        } catch (Exception e) {
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }
    }

    @ApiOperation(value = "Kết xuất danh sách đề xuất kế hoạch nhập khác", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(PathContains.URL_KET_XUAT + "/bblm")
    @ResponseStatus(HttpStatus.OK)
    public void exportBbLm(@Valid @RequestBody HhQdGiaoNvuNhapKhacSearch searchReq, HttpServletResponse response)
            throws Exception {
        try {
            service.xuatFileBbLm(searchReq, response);
        } catch (Exception e) {
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }
    }

    @ApiOperation(value = "Kết xuất danh sách đề xuất kế hoạch nhập khác", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(PathContains.URL_KET_XUAT + "/pkncl")
    @ResponseStatus(HttpStatus.OK)
    public void exportPkncl(@Valid @RequestBody HhQdGiaoNvuNhapKhacSearch searchReq, HttpServletResponse response)
            throws Exception {
        try {
            service.xuatFilePkncl(searchReq, response);
        } catch (Exception e) {
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }
    }

    @ApiOperation(value = "Xem truoc", response = List.class)
    @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody HhQdGiaoNvuNhapKhacSearch req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.preview(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
