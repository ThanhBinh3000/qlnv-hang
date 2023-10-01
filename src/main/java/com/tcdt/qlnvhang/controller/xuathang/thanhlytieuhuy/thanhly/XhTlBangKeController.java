package com.tcdt.qlnvhang.controller.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauService;
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
import java.util.List;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.XUAT_THANH_LY + PathContains.BANG_KE_CAN_HANG)
@Slf4j
@Api(tags = "Xuất thanh lý - Biên bản lấy mẫu")
public class XhTlBangKeController {

    @Autowired
    private XhTlBangKeService service;

    @ApiOperation(value = "Tra cứu ", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@RequestBody XhTlBangKeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
//            resp.setData(service.searchKiemTraChatLuong(objReq));
            resp.setData(service.searchPage(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch ( Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }



    @ApiOperation(value = "Tạo mới  ", response = List.class)
    @PostMapping(value =  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert( @RequestBody XhTlBangKeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.create(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }



    @ApiOperation(value = "Cập nhật ", response = List.class)
    @PostMapping(value =  PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody XhTlBangKeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.update(objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Lấy chi tiết ", response = List.class)
    @GetMapping(value =  PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("id")Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(service.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin", response = List.class)
    @PostMapping(value =  PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody XhTlBangKeReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            service.approve(objReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá ", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            service.delete(idSearchReq.getId());
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

//    @ApiOperation(value = "Xem truoc", response = List.class)
//    @PostMapping(value = PathContains.URL_XEM_TRUOC, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> preview(@RequestBody XhTlBangKeReq req) {
//        BaseResponse resp = new BaseResponse();
//        try {
//            resp.setData(service.preview(req));
//            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//        } catch (Exception e) {
//            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//            resp.setMsg(e.getMessage());
//        }
//        return ResponseEntity.ok(resp);
//    }

}
