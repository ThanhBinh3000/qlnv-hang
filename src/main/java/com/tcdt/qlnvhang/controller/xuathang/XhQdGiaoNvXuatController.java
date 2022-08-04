package com.tcdt.qlnvhang.controller.xuathang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.QD_GIAO_NVU_XUAT_HANG)
@Api(tags = "Quản lý Quyết định giao nhiệm vụ xuất hàng ")
public class XhQdGiaoNvXuatController {

    @Autowired
    private XhQdGiaoNvuXuatService service;

    @ApiOperation(value = "Tạo mới Quản lý Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @PostMapping
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody XhQdGiaoNvuXuatReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quản lý Quyết định giao nhiệm vụ xuất hàng lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quản lý Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @PutMapping
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhQdGiaoNvuXuatReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quản lý Quyết định giao nhiệm vụ xuất hàng lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết Quản lý Quyết định giao nhiệm vụ xuất hàng", response = List.class)
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
            log.error("Chi tiết Quản lý Quyết định giao nhiệm vụ xuất hàng lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa Quản lý Quyết định giao nhiệm vụ xuất hàng", response = List.class)
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
            log.error("Xóa Quản lý Quyết định giao nhiệm vụ xuất hàng lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối Quản lý Quyết định giao nhiệm vụ xuất hàng", response = List.class)
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
            log.error("Phê duyệt/ từ chối Quản lý Quyết định giao nhiệm vụ xuất hàng lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu Quản lý Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @GetMapping()
    public ResponseEntity<BaseResponse> search(XhQdGiaoNvuXuatSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quản lý Quyết định giao nhiệm vụ xuất hàng lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple Quyết định giao nhiệm vụ xuất hàng", response = List.class)
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
            log.error("Delete multiple Quyết định giao nhiệm vụ xuất hàng lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Export Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody XhQdGiaoNvuXuatSearchReq req) {

        try {
            service.exportToExcel(req, response);
        } catch (Exception e) {
            log.error("Error can not export", e);
        }
    }
}
