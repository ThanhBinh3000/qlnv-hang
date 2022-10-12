package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBienBanNghiemThuReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhBbNghiemThu;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhBienBanNghiemThuService;
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
@Api(tags = "Biên bản nghiệm thu bảo quản lần đầu")
public class HhBienBanNghiemThuController extends BaseController {

    @Autowired
    private HhBienBanNghiemThuService hhBienBanNghiemThuService;

    @ApiOperation(value = "Tra cứu biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @PostMapping(value=  PathContains.BB_NT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhBbNghiemThu objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBienBanNghiemThuService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @PostMapping(value=  PathContains.BB_NT + PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> save(@Valid @RequestBody HhBienBanNghiemThuReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBienBanNghiemThuService.save(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @PostMapping(value=  PathContains.BB_NT + PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> update(@Valid @RequestBody HhBienBanNghiemThuReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBienBanNghiemThuService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @GetMapping(value =PathContains.BB_NT+ PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID biên bản nghiệm thu bảo quản lần đầu", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBienBanNghiemThuService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @PostMapping(value=  PathContains.BB_NT + PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhBienBanNghiemThuService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("xóa: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa dánh sách biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @PostMapping(value=  PathContains.BB_NT + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhBienBanNghiemThuService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa danh sách: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách biên bản nghiệm thu bảo quản lần đầu", response = List.class)
    @PostMapping(value= PathContains.BB_NT + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhBbNghiemThu objReq, HttpServletResponse response) throws Exception{

        try {
            hhBienBanNghiemThuService.export(objReq,response);
        } catch (Exception e) {

            log.error("Kết xuất danh sách biên bản nghiệm thu bảo quản lần đầu: {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }

    }

    @ApiOperation(value = "Phê duyêt biên bản nghiệm thu bảo quản lần đầu ", response = List.class)
    @PostMapping(value=PathContains.BB_NT + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> updateStatusUbtvqh(@Valid @RequestBody StatusReq statusReq, HttpServletRequest req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhBienBanNghiemThuService.approve(statusReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

}
