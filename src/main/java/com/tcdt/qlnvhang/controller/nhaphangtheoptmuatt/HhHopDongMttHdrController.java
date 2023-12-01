package com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.HopDongMttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclSearch;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.SearchHopDongMttHdrDTO;
import com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.hopdong.hopdongphuluc.HopDongMttHdrService;
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
@RequestMapping(value = "/mua-truc-tiep"+ PathContains.HD_MTT)
@Slf4j
@Api(tags = "Hợp đồng bảng kê phiếu mua hàng")
public class HhHopDongMttHdrController extends BaseController {
    @Autowired
    HopDongMttHdrService hopDongMttHdrService;

    @Autowired
    HopDongMttHdrRepository hopDongMttHdrRepository;


    @ApiOperation(value = "Tra cứu thông tin hợp đồng", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPage(@Valid @RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.searchPage(req));
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

    @ApiOperation(value = "Tra cứu thông tin hợp đồng", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU + "/ds-tao-qd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> dsTaoQd(@Valid @RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.dsDuocTaoQdGvuNhang(req));
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

    @ApiOperation(value = "Tra cứu thông tin hợp đồng", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU + "/hop-dong", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPageHopDongHdr(@Valid @RequestBody SearchHopDongMttHdrDTO req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.searchPageHopDongHdr(req));
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

    @ApiOperation(value = "Tạo mới thông tin hợp đồng ", response = List.class)
    @PostMapping(value =  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(HttpServletRequest request, @Valid @RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.save(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới thông tin hợp đồng ", response = List.class)
    @PostMapping(value =  PathContains.URL_TAO_MOI + "/phu-luc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insertPL(HttpServletRequest request, @Valid @RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.savePl(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Cập nhật thông tin hợp đồng", response = List.class)
    @PostMapping(value =  PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(HttpServletRequest request, @Valid @RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.update(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin hợp đồng", response = List.class)
    @GetMapping(value =  PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids") Long ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.detail(ids));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy thông tin nhiều hợp đồng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_CHI_TIET + "/multi", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> dsHDongByListId(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.dsHDongByListId(idSearchReq.getIds()));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin", response = List.class)
    @PostMapping(value =  PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody HopDongMttHdrReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hopDongMttHdrService.approve(stReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Xoá thông tin hợp đồng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hopDongMttHdrService.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách thông tin hợp đồng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            hopDongMttHdrService.deleteMulti(idSearchReq.getIds());
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
    @PostMapping(value =  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportList(@Valid @RequestBody  HopDongMttHdrReq objReq, HttpServletResponse response) throws Exception {
        try {
            hopDongMttHdrService.export(objReq,response);

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

    @ApiOperation(value = "Xem truoc", response = List.class)
    @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> preview(@RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrService.preview(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu thông tin hợp đồng", response = List.class)
    @PostMapping(value = "/find-by-id-dvi-ban", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> findByIdDviBan(@Valid @RequestBody HopDongMttHdrReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(hopDongMttHdrRepository.findByIdKqCgia(req.getIdKqCgia()).get());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (
                Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }
}

