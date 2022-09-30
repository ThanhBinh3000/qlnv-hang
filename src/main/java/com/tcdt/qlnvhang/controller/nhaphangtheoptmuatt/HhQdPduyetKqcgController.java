package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPduyetKqcgHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhQdPduyetKqcg;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhQdPduyetKqcgService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mua-truc-tiep")
@Api(tags = "Quyết định phê duyệt phê duyệt kết quả chào giá")
public class HhQdPduyetKqcgController extends BaseController {

    @Autowired
    HhQdPduyetKqcgService hhQdPduyetKqcgService;

    @ApiOperation(value = "Tra cứu quyết định phê duyệt kết quả chào giá", response = List.class)
    @PostMapping(value=  PathContains.QD_PD_KQCG + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhQdPduyetKqcg objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPduyetKqcgService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới quyết định phê duyệt kết quả chào giá", response = List.class)
    @PostMapping(value=PathContains.QD_PD_KQCG+ PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> save(@Valid @RequestBody HhQdPduyetKqcgHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPduyetKqcgService.save(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới quyết định phê duyệt kết quả chào giá", response = List.class)
    @PostMapping(value=PathContains.QD_PD_KQCG+ PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> update(@Valid @RequestBody HhQdPduyetKqcgHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPduyetKqcgService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin quyết định phê duyệt kết quả chào giá", response = List.class)
    @GetMapping(value =PathContains.QD_PD_KQCG+ PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID Quyết định điều chỉnh qđ phê duyệt KHLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPduyetKqcgService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa đề xuất quyết định phê duyệt kết quả chào giá", response = List.class)
    @PostMapping(value=  PathContains.QD_PD_KQCG + PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhQdPduyetKqcgService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("xóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa dánh sách quyết định phê duyệt kết quả chào giá", response = List.class)
    @PostMapping(value=  PathContains.QD_PD_KQCG + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhQdPduyetKqcgService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa danh sách: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
    @ApiOperation(value = "Kết xuất danh sách quyết định phê duyệt kết quả chào giá", response = List.class)
    @PostMapping(value= PathContains.QD_PD_KQCG + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhQdPduyetKqcg objReq, HttpServletResponse response) throws Exception{

        try {
            hhQdPduyetKqcgService.export(objReq,response);
        } catch (Exception e) {

            log.error("Kết xuất danh sách đề xuất kế hoạch mua trực tiếp: {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }

    }

    @ApiOperation(value = "Phê duyêt đề xuất kế hoạch mưa trực tiếp ", response = List.class)
    @PostMapping(value=PathContains.QD_PD_KQCG + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> updateStatusUbtvqh(@Valid @RequestBody StatusReq statusReq, HttpServletRequest req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPduyetKqcgService.approve(statusReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

}
