package com.tcdt.qlnvhang.controller.dieuchuyennoibo;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbKeHoachNhapXuatService;
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
@RequestMapping(value = PathContains.DIEU_CHUYEN_NOI_BO +PathContains.KE_HOACH_NHAP_XUAT)
@Slf4j
@Api(tags = "Điều chuyển nội bộ - Bảng kê cân hàng")
public class DcnbKeHoachNhapXuatController {
    @Autowired
    DcnbKeHoachNhapXuatService dcnbKeHoachNhapXuatService;


    @ApiOperation(value = "Lấy chi tiết thông tin bảng kê cân hàng", response = List.class)
    @GetMapping(value =  PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("id")Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbKeHoachNhapXuatService.detailKhDtl(id));
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

}
