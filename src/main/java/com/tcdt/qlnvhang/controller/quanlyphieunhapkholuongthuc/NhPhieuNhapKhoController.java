package com.tcdt.qlnvhang.controller.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.quanlyphieunhapkho.NhPhieuNhapKhoService;
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
@RequestMapping(PathContains.PHIEU_NHAP_KHO)
@Api(tags = "Nhập hàng - Đấu thầu - Phiếu nhập kho")
public class NhPhieuNhapKhoController {

    @Autowired
    private NhPhieuNhapKhoService nhPhieuNhapKhoService;

    @ApiOperation(value = "Tạo mới Quản lý phiếu nhập kho lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody NhPhieuNhapKhoReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhPhieuNhapKhoService.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quản lý phiếu nhập kho lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quản lý phiếu nhập kho lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody NhPhieuNhapKhoReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhPhieuNhapKhoService.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quản lý phiếu nhập kho lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết Quản lý phiếu nhập kho lương thực", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> detail(@PathVariable Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhPhieuNhapKhoService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết Quản lý phiếu nhập kho lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa Quản lý phiếu nhập kho lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            nhPhieuNhapKhoService.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quản lý phiếu nhập kho lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối Quản lý phiếu nhập kho lương thực", response = List.class)
    @PostMapping(PathContains.URL_PHE_DUYET)
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody NhPhieuNhapKhoReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhPhieuNhapKhoService.approve(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối Quản lý phiếu nhập kho lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu Quản lý phiếu nhập kho lương thực", response = List.class)
    @GetMapping
    public ResponseEntity<BaseResponse> search(NhPhieuNhapKhoSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(nhPhieuNhapKhoService.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quản lý phiếu nhập kho lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple phiếu nhập kho lương thực", response = List.class)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/multiple")
    public final ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(nhPhieuNhapKhoService.deleteMultiple(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            resp.setMsg(e.getMessage());
            log.error("Delete multiple phiếu nhập kho lương thực lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Export phiếu nhập kho lương thực", response = List.class)
    @PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody NhPhieuNhapKhoSearchReq req) {

        try {
//            nhPhieuNhapKhoService.exportToExcel(req, response);
        } catch (Exception e) {
            log.error("Error can not export", e);
        }
    }

    @ApiOperation(value = "Get số phiếu nhập kho lương thực", response = List.class)
    @GetMapping("/so")
    public ResponseEntity<BaseResponse> getSo() {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(nhPhieuNhapKhoService.getSo());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Get số phiếu nhập kho lương thực lỗi", e);
        }
        return ResponseEntity.ok(resp);
    }
}
