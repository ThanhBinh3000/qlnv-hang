package com.tcdt.qlnvhang.controller.xuathang.bantructiep.kehoach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttService;
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
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.BAN_TRUC_TIEP + PathContains.QD_PD_BTT )
@Slf4j
@Api(tags = "Xuất hàng - Bans trực tiếp - Kế hoạch bán trực tiếp - Quyết định phê duyệt KH bán trực tiếp")
public class XhQdPdKhBttControler extends BaseController {

    @Autowired
    private XhQdPdKhBttService xhQdPdKhBttService;

    @ApiOperation(value = "Tra cứu Quyết định phê duyệt kế hoạch bán trực tiếp  ", response = List.class)
    @PostMapping(value=  PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody XhQdPdKhBttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdPdKhBttService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới Quyết định phê duyệt kế hoạch bán trực tiếp lương thực", response = List.class)
    @PostMapping(value =PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody XhQdPdKhBttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdPdKhBttService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quyết định phê duyệt kế hoạch bán trục tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật Quyết định phê duyệt kế hoạch bán trực tiếp", response = List.class)
    @PostMapping(value =PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request,
                                               @Valid @RequestBody XhQdPdKhBttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdPdKhBttService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật Quyết định phê duyệt kế hoạch bán đấu giá trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch bán trực tiếp", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID phương án kế hoạch bán trực tiếp", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdPdKhBttService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch bán trực tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 Quyết định phê duyệt kế hoạch bán trực tiếp", response = List.class)
    @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody XhQdPdKhBttHdrReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdPdKhBttService.approve(stReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt quyết định phê duyệt kế hoạch bán đấu giá trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá quyết định phê duyệt kế hoạch bán trực tiếp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhQdPdKhBttService.delete(idSearchReq.getId());
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

    @ApiOperation(value = "Xoá danh sách quyết định phê duyệt kế hoạch bán trực tiếp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMuilti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhQdPdKhBttService.deleteMulti(idSearchReq.getIdList());
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

    @ApiOperation(value = "Kết xuất danh sách quyết định phê duyệt kế hoạch bán trực tiếp ", response = List.class)
    @PostMapping(value= PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody XhQdPdKhBttHdrReq objReq, HttpServletResponse response) throws Exception{

        try {
            xhQdPdKhBttService.export(objReq,response);
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

}
