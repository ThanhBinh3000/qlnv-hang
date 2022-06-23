package com.tcdt.qlnvhang.controller.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.quanlybangkecanhangluongthuc.QlBangKeCanHangLtService;
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
@RequestMapping(PathContains.QL_BANG_KE_CAN_HANG_LT)
@Api(tags = "Quản lý bảng kê cân hàng lương thực ")
public class QlBangKeCanHangLtController {

    @Autowired
    private QlBangKeCanHangLtService qlBangKeCanHangLtService;

    @ApiOperation(value = "Tạo mới Quản lý bảng kê cân hàng lương thực", response = List.class)
    @PostMapping
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody QlBangKeCanHangLtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quản lý bảng kê cân hàng lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quản lý bảng kê cân hàng lương thực", response = List.class)
    @PutMapping
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody QlBangKeCanHangLtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quản lý bảng kê cân hàng lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết Quản lý bảng kê cân hàng lương thực", response = List.class)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết Quản lý bảng kê cân hàng lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa Quản lý bảng kê cân hàng lương thực", response = List.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.delete(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quản lý bảng kê cân hàng lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối Quản lý bảng kê cân hàng lương thực", response = List.class)
    @PutMapping("/status")
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.updateStatusQd(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối Quản lý bảng kê cân hàng lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu Quản lý bảng kê cân hàng lương thực", response = List.class)
    @GetMapping()
    public ResponseEntity<BaseResponse> search(QlBangKeCanHangLtSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quản lý bảng kê cân hàng lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple bảng kê cân hàng lương thực", response = List.class)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/multiple")
    public final ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.deleteMultiple(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            resp.setMsg(e.getMessage());
            log.error("Delete multiple bảng kê cân hàng lương thực lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Count bảng kê cân hàng lương thực", response = List.class)
    @PostMapping("/count")
    public ResponseEntity<BaseResponse> count() {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qlBangKeCanHangLtService.count());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Count bảng kê cân hàng lương thực lỗi", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Export bảng kê cân hàng lương thực", response = List.class)
    @PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody QlBangKeCanHangLtSearchReq req) {

        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=bang_ke_can_hang_luong_thuc_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            qlBangKeCanHangLtService.exportToExcel(req, response);
        } catch (Exception e) {
            log.error("Error can not export", e);
        }

    }
}
