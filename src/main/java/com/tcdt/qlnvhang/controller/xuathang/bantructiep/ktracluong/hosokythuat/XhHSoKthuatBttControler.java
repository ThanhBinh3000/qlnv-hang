package com.tcdt.qlnvhang.controller.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttHdrReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttService;
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
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.BAN_TRUC_TIEP + PathContains.HO_SO_KY_THUAT )
@Slf4j
@Api(tags = "Xuất hàng - Bán trực tiếp - Kiểm tra chất lượng - Hồ sơ kỹ thuật")
public class XhHSoKthuatBttControler extends BaseController {

    @Autowired
    private XhHSoKthuatBttService xhHSoKthuatBttService;

    @ApiOperation(value = "Tra cứu hồ sơ kỹ thuật bán trưc tiếp vật tư  ", response = List.class)
    @PostMapping(value=  PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody XhHSoKthuatBttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhHSoKthuatBttService.searchPage(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu hồ sơ kỹ thuật bán trực tiếp vật tư: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới hồ sơ kỹ thuật bán trưc tiếp vật tư", response = List.class)
    @PostMapping(value =PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody XhHSoKthuatBttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhHSoKthuatBttService.create(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới hồ sơ kỹ thuật bán trực tiếp vật tư trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật hồ sơ kỹ thuật bán trưc tiếp vật tư", response = List.class)
    @PostMapping(value =PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request,
                                               @Valid @RequestBody XhHSoKthuatBttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhHSoKthuatBttService.update(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật hồ sơ kỹ thuật bán trực tiếp vật tư trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết hồ sơ kỹ thuật bán trực tiếp vật tư", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID phương án hồ sơ kỹ thuật bán trực tiếp vật tư", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhHSoKthuatBttService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết hồ sơ kỹ thuật bán trực tiếp vậy tư trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 hồ sơ kỹ thuật bán trực tiếp vật tư", response = List.class)
    @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody XhHSoKthuatBttHdrReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhHSoKthuatBttService.approve(stReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt hồ sơ kỹ thuật bán trực tiếp vật tư trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá hồ sơ kỹ thuật bán trực tiếp vật tư ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhHSoKthuatBttService.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá hồ sơ kỹ thuật bán trực tiếp vật tư trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách hồ sơ kỹ thuật bán trực tiếp vật tư", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMuilti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhHSoKthuatBttService.deleteMulti(idSearchReq.getIdList());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá quyết định phê duyệt kế hoạch bán đấu giá trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
