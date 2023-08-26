package com.tcdt.qlnvhang.controller.nhaphang.dauthau.ktracl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.SearchNhHoSoBienBan;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.request.search.vattu.hosokythuat.NhHoSoKyThuatSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.hosokythuat.NhHoSoKyThuatService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(PathContains.HO_SO_KY_THUAT)
@Api(tags = "Nhập hàng - Đấu thầu - Kiểm tra chất lượng - Hồ sơ kỹ thuật ")
public class NhHoSoKyThuatController {

    @Autowired
    private NhHoSoKyThuatService service;

    @ApiOperation(value = "Tạo mới ", response = List.class)
    @PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody NhHoSoKyThuatReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới  lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa ", response = List.class)
    @PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody NhHoSoKyThuatReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa  lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết ", response = List.class)
    @GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết  lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa ", response = List.class)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            service.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa  lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối ", response = List.class)
    @PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody NhHoSoKyThuatReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.approve(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối  lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu ", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> search(@RequestBody NhHoSoKyThuatReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.searchPage(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu  lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Delete multiple Hồ sơ kỹ thuật", response = List.class)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/multiple")
    public ResponseEntity<BaseResponse> deleteMultiple(@RequestBody @Valid DeleteReq req) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(service.deleteMultiple(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            resp.setMsg(e.getMessage());
            log.error("Delete multiple Hồ sơ kỹ thuật lỗi ", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Export Hồ sơ kỹ thuật", response = List.class)
    @PostMapping(PathContains.URL_KET_XUAT)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdDcToExcel(HttpServletResponse response, @RequestBody NhHoSoKyThuatReq req) throws Exception {
        try {
            service.export(req, response);
        } catch (Exception e) {
            log.error("Kết xuất danh sách đề xuất kế hoạch lựa chọn nhà thầu trace: {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }
    }

    @ApiOperation(value = "Xem trước", response = List.class)
    @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody NhHoSoKyThuatReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.preview(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xem trước: {?}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
