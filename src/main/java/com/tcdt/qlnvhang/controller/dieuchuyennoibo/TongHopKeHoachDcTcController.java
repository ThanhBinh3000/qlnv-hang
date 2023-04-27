package com.tcdt.qlnvhang.controller.dieuchuyennoibo;

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucHdrReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.THKeHoachDieuChuyenTongCucService;
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

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = PathContains.DIEU_CHUYEN_NOI_BO +PathContains.TONG_HOP_KE_HOACH_DIEU_CHUYEN)
@Slf4j
@Api(tags = "Điều chuyển nội bộ - Tổng hợp kế hoạch điều chuyển")
public class TongHopKeHoachDcTcController extends BaseController {

    @Autowired
    THKeHoachDieuChuyenTongCucService thKeHoachDieuChuyenTongCucService;

    @ApiOperation(value = "Tra cứu thông tin tổng hợp", response = List.class)
    @PostMapping(value = "/tra-cuu-tong-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colectionTongCuc(@CurrentUser CustomUserDetails currentUser,@RequestBody TongHopKeHoachDieuChuyenSearch objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenTongCucService.searchPage(currentUser, objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Tạo mới thông tin tổng hợp ", response = List.class)
    @PostMapping(value = "/them-moi-kh-tong-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenTongCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenTongCucService.save(currentUser,objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết thông tin tổng hợp", response = List.class)
    @GetMapping(value =  "chi-tiet-kh-tong-cuc" + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detailTongCuc(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids")List<Long> ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenTongCucService.detail(ids).get(0));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

//    @ApiOperation(value = "Xoá thông tin tổng hợp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping(value =  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
//        BaseResponse resp = new BaseResponse();
//        try {
//            thKeHoachDieuChuyenTongCucService.delete(idSearchReq);
//            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//        } catch (Exception e) {
//            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//            resp.setMsg(e.getMessage());
//            log.error("Xoá thông tin : {}", e);
//        }
//
//        return ResponseEntity.ok(resp);
//    }
//
//    @ApiOperation(value = "Xoá danh sách thông tin tổng hợp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping(value =  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
//        BaseResponse resp = new BaseResponse();
//        try {
//            thKeHoachDieuChuyenTongCucService.deleteMulti(idSearchReq);
//            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//        } catch (Exception e) {
//            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//            resp.setMsg(e.getMessage());
//            log.error("Xoá thông tin : {}", e);
//        }
//        return ResponseEntity.ok(resp);
//    }


    @ApiOperation(value = "Tổng hợp kế hoạch điều chuyển", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/lap-ke-hoach-tong-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> createTableTongCuc(@CurrentUser CustomUserDetails currentUser,@RequestBody  TongHopKeHoachDieuChuyenSearch req) throws Exception {
        BaseResponse resp = new BaseResponse();
        resp.setData(thKeHoachDieuChuyenTongCucService.createPlan(currentUser, req));
        resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
        resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật thông tin đề xuất", response = List.class)
    @PostMapping(value =  "cap-nhat-tong-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenTongCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenTongCucService.update(currentUser,objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }


//    @ApiOperation(value = "Kết xuất danh sách mua", response = List.class)
//    @PostMapping(value =  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public void exportList(@CurrentUser CustomUserDetails currentUser ,@Valid @RequestBody  TongHopKeHoachDieuChuyenSearch objReq, HttpServletResponse response) throws Exception {
//        try {
//            thKeHoachDieuChuyenService.export( currentUser,objReq, response);
//
//        } catch (Exception e) {
//            log.error("Kết xuất danh sách dánh sách mua : {}", e);
//            final Map<String, Object> body = new HashMap<>();
//            body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            body.put("msg", e.getMessage());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.setCharacterEncoding("UTF-8");
//            final ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(response.getOutputStream(), body);
//
//        }
//    }
}

