package com.tcdt.qlnvhang.controller.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.BAN_TRUC_TIEP + PathContains.QD_GIAO_NVU_XUAT_HANG )
@Slf4j
@Api(tags = "Xuất hàng - Bán trực tiếp - Kế hoạch bán trực tiếp - Quyết định giao nhiệm vụ xuất hàng")
public class XhQdNvXhBttControler extends BaseController {

    @Autowired
    private XhQdNvXhBttService xhQdNvXhBttService;

    @ApiOperation(value = "Tra cứu Quyết định giao nhiệm vụ xuất hàng  ", response = List.class)
    @PostMapping(value=  PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody XhQdNvXhBttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdNvXhBttService.searchPage(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @PostMapping(value =PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody XhQdNvXhBttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdNvXhBttService.create(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quyết định giao nhiệm vụ xuất hàng trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @PostMapping(value =PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request,
                                               @Valid @RequestBody XhQdNvXhBttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdNvXhBttService.update(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật Quyết định phê duyệt kế hoạch bán đấu giá trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết Quyết định giao nhiệm vụ xuất hàng", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID phương án kế hoạch bán trực tiếp", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdNvXhBttService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch bán trực tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
