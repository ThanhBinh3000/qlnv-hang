package com.tcdt.qlnvhang.controller.dieuchuyennoibo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenCucHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucHdrReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.THKeHoachDieuChuyenService;
import com.tcdt.qlnvhang.util.Contains;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = PathContains.DIEU_CHUYEN_NOI_BO +PathContains.TONG_HOP_KE_HOACH_DIEU_CHUYEN)
@Slf4j
@Api(tags = "Điều chuyển nội bộ - Tổng hợp kế hoạch điều chuyển")
public class TongHopKeHoachDcController extends BaseController {

    @Autowired
    THKeHoachDieuChuyenService thKeHoachDieuChuyenService;


    @ApiOperation(value = "Tra cứu thông tin tổng hợp", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser,@RequestBody TongHopKeHoachDieuChuyenSearch objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            if(currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
                resp.setData(thKeHoachDieuChuyenService.searchPageCuc(currentUser, objReq));
            }else{
                resp.setData(thKeHoachDieuChuyenService.searchPageTongCuc(currentUser, objReq));
            }
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
    @PostMapping(value = "/them-moi-kh-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenService.save(currentUser,objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới thông tin  : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tạo mới thông tin tổng hợp ", response = List.class)
    @PostMapping(value = "/them-moi-kh-tong-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenTongCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenService.saveTongCuc(currentUser,objReq));
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
    @GetMapping(value =  PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids")List<Long> ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenService.detail(ids).get(0));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
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
            resp.setData(thKeHoachDieuChuyenService.detailTongCuc(ids).get(0));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Lấy chi tiết thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin", response = List.class)
    @PostMapping(value =  PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateStatus(@CurrentUser CustomUserDetails currentUser,@Valid @RequestBody StatusReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            thKeHoachDieuChuyenService.approve(currentUser,stReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Yêu cầu xác định điểm nhập", response = List.class)
    @PostMapping(value =  "yeu-cai-xac-dinh-diem-nhap", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> yeuCauXacDinhDiemNhap(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            thKeHoachDieuChuyenService.yeuCauXacDinhDiemNhap(currentUser,objReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Xoá thông tin tổng hợp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@CurrentUser CustomUserDetails currentUser,@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            thKeHoachDieuChuyenService.delete(currentUser,idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }

        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xoá danh sách thông tin tổng hợp", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@CurrentUser CustomUserDetails currentUser,@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            thKeHoachDieuChuyenService.deleteMulti(currentUser, idSearchReq);
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xoá thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tổng hợp kế hoạch điều chuyển", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/lap-ke-hoach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> createTable(@CurrentUser CustomUserDetails currentUser,@RequestBody  TongHopKeHoachDieuChuyenSearch req) throws Exception {
        BaseResponse resp = new BaseResponse();
        if(currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            if (Objects.equals(req.getLoaiDieuChuyen(), Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                resp.setData(thKeHoachDieuChuyenService.createPlanChiCuc(currentUser, req));
            } else if (Objects.equals(req.getLoaiDieuChuyen(), Contains.GIUA_2_CUC_DTNN_KV)) {
                resp.setData(thKeHoachDieuChuyenService.createPlanCuc(currentUser, req));
            } else if (Objects.equals(req.getLoaiDieuChuyen(), Contains.TAT_CA)) {
                resp.setData(thKeHoachDieuChuyenService.createPlanChiCuc(currentUser, req));
                resp.setOtherData(thKeHoachDieuChuyenService.createPlanCuc(currentUser, req));
            }
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            resp.setData(thKeHoachDieuChuyenService.createPlanTongCuc(currentUser, req));
        }
        resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
            return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "Cập nhật thông tin đề xuất", response = List.class)
    @PostMapping(value =  "cap-nhat-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenService.update(currentUser,objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Cập nhật thông tin đề xuất", response = List.class)
    @PostMapping(value =  "cap-nhat-tong-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody ThKeHoachDieuChuyenTongCucHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(thKeHoachDieuChuyenService.updateTongCuc(currentUser,objReq));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Cập nhật thông tin : {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Kết xuất danh sách mua", response = List.class)
    @PostMapping(value =  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportList(@CurrentUser CustomUserDetails currentUser ,@Valid @RequestBody  TongHopKeHoachDieuChuyenSearch objReq, HttpServletResponse response) throws Exception {
        try {
            thKeHoachDieuChuyenService.export( currentUser,objReq, response);

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
}
