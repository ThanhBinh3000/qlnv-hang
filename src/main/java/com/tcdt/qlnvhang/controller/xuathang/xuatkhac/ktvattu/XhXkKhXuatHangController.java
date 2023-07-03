package com.tcdt.qlnvhang.controller.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktluongthuc.XhXkDanhSachService;
import com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangService;
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

import java.util.List;

@RestController
@RequestMapping(value = PathContains.XUAT_HANG_DTQG + PathContains.XUAT_KHAC + PathContains.KTCL_VT_TRUOC_HET_HAN + PathContains.KH_XUAT_HANG)
@Slf4j
@Api(tags = "Xuất hàng DTQG - Xuất khác - Danh sách kế hoạch xuất hàng vt,tb có thời hạn lưu kho lớn hơn 12 tháng của Cục DTNN KV")
public class XhXkKhXuatHangController {
    @Autowired
    XhXkKhXuatHangService xhXkKhXuatHangService;

    @ApiOperation(value = "Tra cứu", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser, @RequestBody XhXkKhXuatHangRequest objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(xhXkKhXuatHangService.searchPage(currentUser, objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

//  @ApiOperation(value = "Lấy chi tiết", response = List.class)
//  @GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<BaseResponse> detail(@ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids") List<Long> ids) {
//    BaseResponse resp = new BaseResponse();
//    try {
//      resp.setData(xhXkDanhSachService.detail(ids).get(0));
//      resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
//      resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
//    } catch (Exception e) {
//      e.printStackTrace();
//      resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
//      resp.setMsg(e.getMessage());
//      log.error("Lấy chi tiết thông tin : {}", e);
//    }
//    return ResponseEntity.ok(resp);
//  }
}
