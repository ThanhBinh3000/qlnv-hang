package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhCgiaReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPthucTkhaiReq;

import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhPthucTkhaiMuaTtService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Tổ chức triển khai mua trực tiếp")
public class HhPthucTkhaiMuaTtController extends BaseController {

    @Autowired
    private HhPthucTkhaiMuaTtService hhPthucTkhaiMuaTtService;


@ApiOperation(value = "Tra cứu thông tin đấu thầu ", response = List.class)
@PostMapping(value =PathContains.TKHAI_MTT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
                                              @Valid @RequestBody SearchHhPthucTkhaiReq objReq) {
    BaseResponse resp = new BaseResponse();
    try {

            resp.setData(hhPthucTkhaiMuaTtService.selectPage(objReq));

        resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
        resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
    } catch (

            Exception e) {
        resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
        resp.setMsg(e.getMessage());
        log.error("Tra cứu thông tin đấu thầu gạo trace: {}", e);
    }

    return ResponseEntity.ok(resp);
}


    @ApiOperation(value = "Tạo mới thông tin đấu thầu gạo", response = List.class)
    @PostMapping(value =PathContains.TKHAI_MTT + PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(HttpServletRequest request, @Valid @RequestBody HhCgiaReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPthucTkhaiMuaTtService.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin đấu thầu gạo trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

//    @ApiOperation(value = "Lấy chi tiết thông tin đấu thầu gạo", response = List.class)
//    @GetMapping(value =PathContains.TKHAI_MTT + PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> detail(@PathVariable("ids") String ids) {
//        BaseResponse resp = new BaseResponse();
//        try {
//            resp.setData(hhPthucTkhaiMuaTtService.detail(ids));
//            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//        } catch (Exception e) {
//            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//            resp.setMsg(e.getMessage());
//            log.error("Lấy chi tiết thông tin đấu thầu gạo trace: {}", e);
//        }
//        return ResponseEntity.ok(resp);
//    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin đấu thầu gạo", response = List.class)
    @PostMapping(value =PathContains.TKHAI_MTT + PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody HhCgiaReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hhPthucTkhaiMuaTtService.approve(stReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt thông tin đấu thầu gạo trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách Quyết định phê duyệt kế hoạch mua trực tiếp ", response = List.class)
    @PostMapping(value= PathContains.TKHAI_MTT + PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportListQdBtcBnToExcel(@Valid @RequestBody SearchHhPthucTkhaiReq objReq, HttpServletResponse response) throws Exception{

        try {
            hhPthucTkhaiMuaTtService.export(objReq,response);
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

    @ApiOperation(value = "Xem truoc", response = List.class)
    @PostMapping(value = PathContains.TKHAI_MTT + PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody HhCgiaReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPthucTkhaiMuaTtService.preview(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }


}
