package com.tcdt.qlnvhang.controller.nhaphangtheoptt;

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPthucTkhaiReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.HhPthucTkhaiMuaTtService;
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
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/mua-truc-tiep")
@Slf4j
@Api(tags = "Tổ chức triển khai mua trực tiếp")
public class HhPthucTkhaiMuaTtController extends BaseController {

    @Autowired
    private HhPthucTkhaiMuaTtService hhPthucTkhaiMuaTtService;

    @ApiOperation(value = "Tra cứu thông tin tổ chức triển khai mua trực tiếp", response = List.class)
    @PostMapping(value=  PathContains.TKHAI_MTT + PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody SearchHhPthucTkhaiReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPthucTkhaiMuaTtService.searchPageTkhai(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới thông tin tổ chức triển khai mua trực tiếp", response = List.class)
    @PostMapping(value = PathContains.TKHAI_MTT+ PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(HttpServletRequest request, @Valid @RequestBody HhQdPheduyetKhMttHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPthucTkhaiMuaTtService.save(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin đấu thầu gạo trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin tổ chức triển khai mua trực tiếp", response = List.class)
    @GetMapping(value = PathContains.TKHAI_MTT+ PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tổ chức triển khai", example = "1", required = true) @PathVariable("ids") String ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hhPthucTkhaiMuaTtService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin đấu thầu gạo trace: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

}
