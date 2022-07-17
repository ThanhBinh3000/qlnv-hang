package com.tcdt.qlnvhang.controller.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbangiaonhan.NhBbGiaoNhanVtSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.vattu.bienbaogiaonhan.NhBbGiaoNhanVtService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.BIEN_BAN_BAN_GIAO_MAU_VT)
@Api(tags = "Quản lý Biên bản bàn giao mẫu vật tư ")
public class NhBbGiaoNhanVtController {

    @Autowired
    private NhBbGiaoNhanVtService service;

    @ApiOperation(value = "Tạo mới Quản lý Biên bản bàn giao mẫu vật tư", response = List.class)
    @PostMapping
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody NhBbGiaoNhanVtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quản lý Biên bản bàn giao mẫu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quản lý Biên bản bàn giao mẫu vật tư", response = List.class)
    @PutMapping
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody NhBbGiaoNhanVtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quản lý Biên bản bàn giao mẫu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết Quản lý Biên bản bàn giao mẫu vật tư", response = List.class)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết Quản lý Biên bản bàn giao mẫu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa Quản lý Biên bản bàn giao mẫu vật tư", response = List.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.delete(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quản lý Biên bản bàn giao mẫu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối Quản lý Biên bản bàn giao mẫu vật tư", response = List.class)
    @PutMapping("/status")
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.updateStatusQd(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối Quản lý Biên bản bàn giao mẫu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu Quản lý Biên bản bàn giao mẫu vật tư", response = List.class)
    @GetMapping()
    public ResponseEntity<BaseResponse> search(NhBbGiaoNhanVtSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quản lý Biên bản bàn giao mẫu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple Biên bản bàn giao mẫu vật tư", response = List.class)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/multiple")
    public final ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.deleteMultiple(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            resp.setMsg(e.getMessage());
            log.error("Delete multiple Biên bản bàn giao mẫu vật tư lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Export Biên bản bàn giao mẫu vật tư", response = List.class)
    @PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody NhBbGiaoNhanVtSearchReq req) {

        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=bien_ban_ban_giao_mau_vat_tu_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            service.exportToExcel(req, response);
        } catch (Exception e) {
            log.error("Error can not export", e);
        }
    }
}
