package com.tcdt.qlnvhang.controller.nhaphangtheoptt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxKhMttTChiThopReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxKhMttThopHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhDxKhMttThopReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhDxuatKhMttThopService;
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
@RequestMapping(value = "/mua-truc-tiep")
@Slf4j
@Api(tags = "Tổng hợp đề xuất kế hoạch mua trực tiếp")
public class HhDxuatKhMttThopHdrController  extends BaseController {

    @Autowired
    private HhDxuatKhMttThopService hhDxuatKhMttThopService;

    @ApiOperation(value = "Tổng hợp đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.TH_MUA_TT, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> sumarryData(HttpServletRequest request
            ,@Valid @RequestBody HhDxKhMttTChiThopReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhDxuatKhMttThopService.sumarrayDaTa(objReq,request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tổng hợp đề xuất kế hoạch mưa trực tiếp: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Thêm mới tổng hợp đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.TH_MUA_TT +PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> create(HttpServletRequest request
            ,@Valid @RequestBody HhDxKhMttThopHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhDxuatKhMttThopService.create(objReq,request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Thêm mới tổng hợp đề xuất kế hoạch mưa trực tiếp: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật tổng hợp đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.TH_MUA_TT +PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> update(@Valid @RequestBody HhDxKhMttThopHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhDxuatKhMttThopService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật tổng hợp đề xuất kế hoạch mưa trực tiếp: {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Tra cứu tổng hợp đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value = PathContains.TH_MUA_TT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> selectPage(@RequestBody SearchHhDxKhMttThopReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhDxuatKhMttThopService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu tổng hợp đề xuất kế hoạch mưa trực tiếp trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value= PathContains.TH_MUA_TT + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhDxKhMttThopReq objReq, HttpServletResponse response) throws Exception{

        try {
            hhDxuatKhMttThopService.export(objReq,response);
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

    @ApiOperation(value = "Xóa tổng hợp đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.TH_MUA_TT + PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhDxuatKhMttThopService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("xóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa dánh sách tổng hợp đề xuất kế hoạch mưa trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.TH_MUA_TT + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhDxuatKhMttThopService.deleteMulti(idSearchReq);
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
    @GetMapping(value = PathContains.TH_MUA_TT + PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID tổng hựp đề xuất kế hoạch mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhDxuatKhMttThopService.detail(ids));
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
