package com.tcdt.qlnvhang.controller.xuathang;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.bbtinhkho.XhBienBanTinhKhoService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.XH_BIEN_BAN_TINH_KHO)
@Api(tags = "Quản lý Biên bản tịnh kho")
public class XhBienBanTinhKhoController {

    @Autowired
    XhBienBanTinhKhoService service;

    @ApiOperation(value = "Xóa Biên bản tịnh kho", response = List.class)
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

    @ApiOperation(value = "Phê duyệt/ từ chối Biên bản tịnh kho", response = List.class)
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
            log.error("Phê duyệt/ từ chối Biên bản tịnh kho lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple Biên bản tịnh kho", response = List.class)
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
            log.error("Delete multiple Biên bản tịnh kho lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }
}
