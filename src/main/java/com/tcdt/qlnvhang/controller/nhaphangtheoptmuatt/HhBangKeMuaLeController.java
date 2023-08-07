package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.bangkethumuale.BangKeMuaLeReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.hopdong.bangkethumuale.BangKeMuaLeService;
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
@RequestMapping(value = "/mua-truc-tiep"+ PathContains.BK_ML)
@Slf4j
@Api(tags = "Bảng kê thu mua lẻ")
public class HhBangKeMuaLeController extends BaseController {
    @Autowired
    BangKeMuaLeService bangKeMuaLeService;


    @ApiOperation(value = "Tra cứu thông tin bảng kê thu mua lẻ", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser,
                                                  @Valid @RequestBody BangKeMuaLeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(bangKeMuaLeService.searchPage(currentUser, objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (
                Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới thông tin bảng kê thu mua lẻ ", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(HttpServletRequest request, @Valid @RequestBody BangKeMuaLeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(bangKeMuaLeService.save(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Cập nhật thông tin bảng kê thu mua lẻ", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request, @Valid @RequestBody BangKeMuaLeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(bangKeMuaLeService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin bảng kê thu mua lẻ", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(bangKeMuaLeService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá thông tin bảng kê thu mua lẻ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            bangKeMuaLeService.delete(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách thông tin bảng kê thu mua lẻ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            bangKeMuaLeService.deleteMulti(idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách mua", response = List.class)
    @PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportList(@CurrentUser CustomUserDetails currentUser,@RequestBody BangKeMuaLeReq objReq, HttpServletResponse response) throws Exception {
        try {
            bangKeMuaLeService.export(currentUser, objReq, response);

        } catch (Exception e) {
            log.error("Kết xuất danh sách dánh sách mua : {}", e);
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
