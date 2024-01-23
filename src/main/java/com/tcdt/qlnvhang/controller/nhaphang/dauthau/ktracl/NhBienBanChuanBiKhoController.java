package com.tcdt.qlnvhang.controller.nhaphang.dauthau.ktracl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbancbkho.NhBienBanChuanBiKhoService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(PathContains.BIEN_BAN_CHUAN_BI_KHO)
@Api(tags = "Nhập hàng - Đấu thầu - Kiểm tra chất lượng - Biên bản chuẩn bị kho ")
public class NhBienBanChuanBiKhoController {

    @Autowired
    private NhBienBanChuanBiKhoService service;

    @ApiOperation(value = "Tạo mới Quản lý Biên bản chuẩn bị kho", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody NhBienBanChuanBiKhoReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quản lý Biên bản chuẩn bị kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quản lý Biên bản chuẩn bị kho", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody NhBienBanChuanBiKhoReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quản lý Biên bản chuẩn bị kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết Quản lý Biên bản chuẩn bị kho", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết Quản lý Biên bản chuẩn bị kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa Quản lý Biên bản chuẩn bị kho", response = List.class)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@RequestBody NhBienBanChuanBiKhoReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            service.delete(req.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quản lý Biên bản chuẩn bị kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối Quản lý Biên bản chuẩn bị kho", response = List.class)
    @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody NhBienBanChuanBiKhoReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.approve(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối Quản lý Biên bản chuẩn bị kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu Quản lý Biên bản chuẩn bị kho", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> search(NhBienBanChuanBiKhoSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(service.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quản lý Biên bản chuẩn bị kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

//    @ApiOperation(value = "Delete multiple Biên bản chuẩn bị kho", response = List.class)
//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping("/delete/multiple")
//    public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
//        BaseResponse resp = new BaseResponse();
//        try {
//            resp.setData(service.deleteMultiple(req));
//            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//        } catch (Exception e) {
//            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//            resp.setMsg(e.getMessage());
//            resp.setMsg(e.getMessage());
//            log.error("Delete multiple Biên bản chuẩn bị kho lỗi ", e);
//        }
//        return ResponseEntity.ok(resp);
//    }
//
//    @ApiOperation(value = "Export Biên bản chuẩn bị kho", response = List.class)
//    @PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody NhBienBanChuanBiKhoSearchReq req) {
//
//        try {
//            service.exportToExcel(req, response);
//        } catch (Exception e) {
//            log.error("Error can not export", e);
//        }
//    }

    @ApiOperation(value = "Kết xuất Danh sách", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(PathContains.URL_KET_XUAT)
    @ResponseStatus(HttpStatus.OK)
    public void export(@Valid @RequestBody HhQdNhapxuatSearchReq searchReq, HttpServletResponse response)
            throws Exception {
        try {
            service.exportBbcbk(searchReq, response);
        } catch (Exception e) {
            log.error("Kết xuất Danh sách trace: {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }
    }

    @ApiOperation(value = "Xem trước", response = List.class)
    @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> previewVt(@RequestBody NhBienBanChuanBiKhoReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.preview(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xem trước: {?}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
