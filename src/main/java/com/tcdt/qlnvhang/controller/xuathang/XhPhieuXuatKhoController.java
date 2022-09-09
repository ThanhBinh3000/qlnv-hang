package com.tcdt.qlnvhang.controller.xuathang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoRes;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;
import com.tcdt.qlnvhang.service.xuathang.bbtinhkho.XhBienBanTinhKhoService;
import com.tcdt.qlnvhang.service.xuathang.xuatkho.XhPhieuXuatKhoService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.XH_PHIEU_XUAT_KHO)
@Api(tags = "Quản lý Phiếu xuất kho")
public class XhPhieuXuatKhoController {

    @Autowired
    XhPhieuXuatKhoService service;

    @ApiOperation(value = "Tra cứu phiếu xuất kho", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> search(HttpServletRequest request, @Valid @RequestBody XhPhieuXuatKhoSearchReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            Page<XhPhieuXuatKhoRes> dataPage = service.search(objReq);

            resp.setData(dataPage);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới phiếu xuất kho", response = List.class)
    @PostMapping
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody XhPhieuXuatKhoReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới phiếu xuất kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết phiếu xuất kho", response = List.class)
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
            log.error("Chi tiết phiếu xuất kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa phiếu xuất kho", response = List.class)
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

    @ApiOperation(value = "Phê duyệt/ từ chối phiếu xuất kho", response = List.class)
    @PutMapping("/status")
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.updateStatus(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối phiếu xuất kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple phiếu xuất kho", response = List.class)
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
            log.error("Delete multiple phiếu xuất kho lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }
}
