package com.tcdt.qlnvhang.controller.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgService;
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
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.DAU_GIA + PathContains.QD_DC_KH )
@Slf4j
@Api(tags = "Xuất hàng - Bán đấu giá - Kế hoạch bán đấu giá - Điều chỉnh KH bán đấu giá")
public class XhQdDchinhKhBdgController extends BaseController {
    @Autowired
    private XhQdDchinhKhBdgService xhQdDchinhKhBdgService;

    @ApiOperation(value = "Tra cứu  ", response = List.class)
    @PostMapping(value=   PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody XhQdDchinhKhBdgReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdDchinhKhBdgService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới lương thực", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert( @Valid @RequestBody XhQdDchinhKhBdgReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdDchinhKhBdgService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết  ", response = List.class)
    @GetMapping(value =  PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID phương án kế hoạch ", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdDchinhKhBdgService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhQdDchinhKhBdgService.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá  trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Xoá danh sách ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMuilti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            xhQdDchinhKhBdgService.deleteMulti(idSearchReq.getIdList());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá  trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Cập nhật  ", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request,
                                               @Valid @RequestBody XhQdDchinhKhBdgReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdDchinhKhBdgService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03  ", response = List.class)
    @PostMapping(value =  PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody XhQdDchinhKhBdgReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhQdDchinhKhBdgService.approve(stReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt bán đấu giá trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Kết xuất danh sách ", response = List.class)
    @PostMapping(value=  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody XhQdDchinhKhBdgReq objReq, HttpServletResponse response) throws Exception{

        try {
            xhQdDchinhKhBdgService.export(objReq,response);
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
