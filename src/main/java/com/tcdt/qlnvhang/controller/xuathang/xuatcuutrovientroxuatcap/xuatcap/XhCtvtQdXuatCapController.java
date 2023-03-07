package com.tcdt.qlnvhang.controller.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhCtvtQdXuatCap;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapService;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathContains.XUAT_CUU_TRO + PathContains.XUAT_CAP + PathContains.QUYET_DINH_PHE_DUYET)
@Slf4j
@Api(tags = "Xuất hàng DTQG - Xuất cứu trợ viện trợ - Xuất cấp - Quyết định xuất cấp")
public class XhCtvtQdXuatCapController {
    @Autowired
    private XhCtvtQdXuatCapService xhCtvtQdXuatCapService;

    @ApiOperation(value = "Tra cứu thông tin quyết định xuất cấp", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPage(@CurrentUser CustomUserDetails currentUser, @RequestBody SearchXhCtvtQdXuatCap objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhCtvtQdXuatCapService.search(objReq, currentUser));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới quyết định xuất cấp ", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhCtvtQdXuatCapReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhCtvtQdXuatCapService.save(currentUser, objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật thông tin quyết định xuất cấp", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody XhCtvtQdXuatCapReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhCtvtQdXuatCapService.update(currentUser, objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin quyết định xuất cấp", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(@ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("id") Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhCtvtQdXuatCapService.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá quyết định xuất cấp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhCtvtQdXuatCapService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách quyết định xuất cấp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhCtvtQdXuatCapService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
