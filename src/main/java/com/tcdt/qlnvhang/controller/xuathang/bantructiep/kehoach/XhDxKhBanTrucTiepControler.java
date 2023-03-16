package com.tcdt.qlnvhang.controller.xuathang.bantructiep.kehoach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.CountKhMttSlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepService;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepServicelmpl;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.BAN_TRUC_TIEP + PathContains.DX_KH_BTT )
@Slf4j
@Api(tags = "Xuất hàng - Bán trực tiếp - Kế hoạch bán trực tiếp - Đề xuất kế hoạch bán trực tiếp")
public class XhDxKhBanTrucTiepControler extends BaseController {

    @Autowired
    private XhDxKhBanTrucTiepService xhDxKhBanTrucTiepService;

    @Autowired
    private XhDxKhBanTrucTiepServicelmpl xhDxKhBanTrucTiepServicelmpl;

    @ApiOperation(value = "Tra cứu đề xuất kế hoạch bán trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody XhDxKhBanTrucTiepHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhDxKhBanTrucTiepService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới đề xuất kế hoạch bán trực tiếp ", response = List.class)
    @PostMapping(value=  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody XhDxKhBanTrucTiepHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhDxKhBanTrucTiepService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa đề xuất kế hoạch bán trực tiếp ", response = List.class)
    @PostMapping(value=  PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhDxKhBanTrucTiepHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhDxKhBanTrucTiepService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết kế hoạch bán trực tiếp", response = List.class)
    @GetMapping(value =PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID", example = "1", required = true) @PathVariable("id") Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhDxKhBanTrucTiepService.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyêt kế hoạch bán trực tiếp", response = List.class)
    @PostMapping(value=PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus( @RequestBody XhDxKhBanTrucTiepHdrReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhDxKhBanTrucTiepService.approve(stReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa kế hoạch bán trực tiếp  ", response = List.class)
    @PostMapping(value=  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhDxKhBanTrucTiepService.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("xóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa dánh sách kế hoạch bán trực tiếp ", response = List.class)
    @PostMapping(value=  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody XhDxKhBanTrucTiepHdrReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhDxKhBanTrucTiepService.deleteMulti(idSearchReq.getIds());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa danh sách: {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Kết xuất danh sách kế hoạch bán trực tiếp ", response = List.class)
    @PostMapping(value= PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody XhDxKhBanTrucTiepHdrReq objReq, HttpServletResponse response) throws Exception{

        try {
            xhDxKhBanTrucTiepService.export(objReq,response);
        } catch (Exception e) {

            log.error("Kết xuất danh sách : {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }

    }

    @ApiOperation(value = "Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa", response = List.class)
    @PostMapping(value =  "/count-sl-kh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getCountSl(HttpServletRequest request,
                                                   @Valid @RequestBody CountKhMttSlReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhDxKhBanTrucTiepServicelmpl.countSoLuongKeHoachNam(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy tổng số lượng đã lên kế hoạch trong năm theo đơn vị, loại vật tư  hàng hóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
