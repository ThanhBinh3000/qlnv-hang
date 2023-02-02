package com.tcdt.qlnvhang.controller.xuathang.daugia.kehoach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.tonghop.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgService;
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
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.DAU_GIA + PathContains.TH_KH_BDG )
@Slf4j
@Api(tags = "Xuất hàng - Bán đấu giá - Kế hoạch bán đấu giá - Tổng hợp đề xuất kế hoạch bán đấu giá")
public class XhThopDxKhBdgController extends BaseController {

    @Autowired
    private XhThopDxKhBdgService xhThopDxKhBdgService;

    @ApiOperation(value = "Tổng hợp đề xuất kế hoạch bán đấu giá  ", response = List.class)
    @PostMapping()
    public ResponseEntity<BaseResponse> sumarryData(HttpServletRequest request
            , @Valid @RequestBody XhThopChiTieuReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhThopDxKhBdgService.sumarryData(objReq,request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tổng hợp : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới tổng hợp đề xuất kế hoạch bán đấu giá", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(HttpServletRequest request,
                                               @Valid @RequestBody XhThopDxKhBdgReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhThopDxKhBdgService.create(objReq, request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới tổng hợp đề xuất kế hoạch bán đấu giá trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật tổng hợp ", response = List.class)
    @PostMapping(value=  PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhThopDxKhBdgReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhThopDxKhBdgService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật tổng hợp : {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Tra cứu tổng hợp ", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> selectPage(@RequestBody SearchXhThopDxKhBdg objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhThopDxKhBdgService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu tổng hợp  trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách ", response = List.class)
    @PostMapping(value= PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchXhThopDxKhBdg objReq, HttpServletResponse response) throws Exception{

        try {
            xhThopDxKhBdgService.export(objReq,response);
        } catch (Exception e) {

            log.error("Kết xuất danh sách tổng hợp đề xuất kế hoạch mua trực tiếp: {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }

    }

    @ApiOperation(value = "Xóa tổng hợp ", response = List.class)
    @PostMapping(value=  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhThopDxKhBdgService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("xóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa dánh sách tổng hợp ", response = List.class)
    @PostMapping(value=  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhThopDxKhBdgService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa danh sách: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết tổng hợp đề xuất kế hoạch mua trực tiếp", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID tổng hựp đề xuất kế hoạch mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhThopDxKhBdgService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết tổng hợp đề xuất kế hoạch mua trực tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
