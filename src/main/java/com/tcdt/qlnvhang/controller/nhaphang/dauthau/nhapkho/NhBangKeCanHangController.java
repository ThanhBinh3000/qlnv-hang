package com.tcdt.qlnvhang.controller.nhaphang.dauthau.nhapkho;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangService;
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
@RequestMapping(PathContains.QL_BANG_KE_CAN_HANG_LT)
@Api(tags = "Nhập hàng - Đấu Thầu - Nhập kho - Bảng kê cân hàng")
public class NhBangKeCanHangController {

    @Autowired
    private NhBangKeCanHangService nhBangKeCanHangService;

    @ApiOperation(value = "Tạo mới Quản lý lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody QlBangKeCanHangLtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhBangKeCanHangService.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quản lý lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody QlBangKeCanHangLtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhBangKeCanHangService.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết Quản lý lương thực", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> detail(@PathVariable Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhBangKeCanHangService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa Quản lý lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(bangKeCanHangService.delete(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối Quản lý lương thực", response = List.class)
    @PostMapping(PathContains.URL_PHE_DUYET)
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody QlBangKeCanHangLtReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(nhBangKeCanHangService.approve(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu Quản lý lương thực", response = List.class)
    @GetMapping()
    public ResponseEntity<BaseResponse> search(QlBangKeCanHangLtSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(bangKeCanHangService.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quản lý lương thực lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple lương thực", response = List.class)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/multiple")
    public final ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(bangKeCanHangService.deleteMultiple(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            resp.setMsg(e.getMessage());
            log.error("Delete multiple lương thực lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Count lương thực", response = List.class)
    @PostMapping("/count")
    public ResponseEntity<BaseResponse> count() {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(bangKeCanHangService.count());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Count lương thực lỗi", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Export lương thực", response = List.class)
    @PostMapping(value = "/export/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody QlBangKeCanHangLtSearchReq req) {

        try {
//            bangKeCanHangService.exportToExcel(req, response);
        } catch (Exception e) {
            log.error("Error can not export", e);
        }

    }

    @ApiOperation(value = "Get số lương thực", response = List.class)
    @GetMapping("/so")
    public ResponseEntity<BaseResponse> getSo() {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(bangKeCanHangService.getSo());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Get số lương thực lỗi", e);
        }
        return ResponseEntity.ok(resp);
    }
}
