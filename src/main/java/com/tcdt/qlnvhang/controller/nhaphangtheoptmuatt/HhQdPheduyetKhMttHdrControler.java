package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrReq;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrSearchReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhQdPheduyetKhMttHdrService;
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
@Api(tags = "Nhập hàng - Mua trựu Tiếp - Kế hoạch mua trực tiếp - Quyết định phê duyệt kế hoạch mua trực tiếp")
public class HhQdPheduyetKhMttHdrControler {

    @Autowired
    private HhQdPheduyetKhMttHdrService hhQdPheduyetKhMttHdrService;

    @ApiOperation(value = "Tra cứu Quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.QD_PD_MTT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody HhQdPheduyetKhMttHdrSearchReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }



    @ApiOperation(value = "Tạo mới Quyết định phê duyệt kế hoạch mua trực tiếp ", response = List.class)
    @PostMapping(value=PathContains.QD_PD_MTT+ PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody HhQdPheduyetKhMttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật Quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class)
    @PostMapping(value =PathContains.QD_PD_MTT+ PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request,
                                               @Valid @RequestBody HhQdPheduyetKhMttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật Quyết định phê duyệt kế hoạch mua trực tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class)
    @GetMapping(value =PathContains.QD_PD_MTT + PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID phương án kế hoạch bán trực tiếp", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch bán trực tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class)
    @GetMapping(value =PathContains.QD_PD_MTT + PathContains.URL_CHI_TIET + "/detailByIdThhdr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detailBySoQd(HttpServletRequest request,
                                                  @Valid @RequestBody HhQdPheduyetKhMttHdrReq objReq, HttpServletResponse response) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.detailBySoQd(objReq.getSoQd()));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá quyết định phê duyệt kế hoạch mua tiếp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.QD_PD_MTT +PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {hhQdPheduyetKhMttHdrService.delete(idSearchReq.getId());
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


    @ApiOperation(value = "Xoá danh sách quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.QD_PD_MTT + PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMuilti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhQdPheduyetKhMttHdrService.deleteMulti(idSearchReq.getIdList());
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


    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 Quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class)
    @PostMapping(value = PathContains.QD_PD_MTT + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.approve(stReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            // TODO: handle exception
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt Quyết định phê duyệt kế hoạch mua trực tiếp trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách Quyết định phê duyệt kế hoạch mua trực tiếp ", response = List.class)
    @PostMapping(value= PathContains.QD_PD_MTT + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody HhQdPheduyetKhMttHdrSearchReq  objReq, HttpServletResponse response) throws Exception{

        try {
            hhQdPheduyetKhMttHdrService.export(objReq,response);
        } catch (Exception e) {

            log.error("Kết xuất danh sách Quyết định phê duyệt kế hoạch mua trực tiếp : {}", e);
            final Map<String, Object> body = new HashMap<>();
            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            body.put("msg", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }

    }

    @ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch mua trực tiếp", response = List.class)
    @GetMapping(value =PathContains.QD_PD_MTT + "/chi-tiet-ke-hoach/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detailDtl( @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.detailDtl(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xem trước", response = List.class)
    @PostMapping(value = PathContains.QD_PD_MTT + PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody HhQdPheduyetKhMttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhQdPheduyetKhMttHdrService.preview(objReq));
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
