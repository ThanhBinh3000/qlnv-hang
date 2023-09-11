package com.tcdt.qlnvhang.controller.dieuchuyennoibo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CurrentUser;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBienBanLayMauHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuKtChatLuongHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKtChatLuong;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.impl.DcnbPhieuKiemTraChatLuongServiceImpl;
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

@RestController
@RequestMapping(value = PathContains.DIEU_CHUYEN_NOI_BO +PathContains.PHIEU_KIEM_TRA_CHAT_LUONG)
@Slf4j
@Api(tags = "Điều chuyển nội bộ - Phiếu kiểm tra chất lượng")
public class DcnbPhieuKiemTraChatLuongController {
    @Autowired
    private DcnbPhieuKiemTraChatLuongServiceImpl dcnbPhieuKiemTraChatLuongServiceImpl;

    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;

    @ApiOperation(value = "Tra cứu thông tin", response = List.class)
    @PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@CurrentUser CustomUserDetails currentUser,
                                                  @RequestBody SearchPhieuKtChatLuong objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbPhieuKiemTraChatLuongServiceImpl.searchPage(currentUser,objReq));
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

    @ApiOperation(value = "Tra cứu thông tin", response = List.class)
    @PostMapping(value = PathContains.DANH_SACH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> list(@CurrentUser CustomUserDetails currentUser,
                                                  @RequestBody SearchPhieuKtChatLuong objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbPhieuKiemTraChatLuongServiceImpl.searchList(currentUser,objReq));
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

    @ApiOperation(value = "Tra cứu biên bản lấy mẫu", response = List.class)
    @PostMapping(value = "bien-ban-lay-mau", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getListBienBanLayMau(@CurrentUser CustomUserDetails currentUser,
                                                  @RequestBody SearchDcnbBienBanLayMau param) {
        BaseResponse resp = new BaseResponse();
        try {
            param.setMaDvi(currentUser.getDvql());
            resp.setData(dcnbBienBanLayMauHdrRepository.searchList(param));
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

    @ApiOperation(value = "Tạo mới thông tin ", response = List.class)
    @PostMapping(value =  PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody DcnbPhieuKtChatLuongHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbPhieuKiemTraChatLuongServiceImpl.save(currentUser,objReq));
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

    @ApiOperation(value = "Cập nhật thông tin", response = List.class)
    @PostMapping(value =  PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> update(@CurrentUser CustomUserDetails currentUser, @Valid @RequestBody DcnbPhieuKtChatLuongHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbPhieuKiemTraChatLuongServiceImpl.update(currentUser,objReq));
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

    @ApiOperation(value = "Lấy chi tiết thông tin", response = List.class)
    @GetMapping(value =  PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(
            @ApiParam(value = "ID thông tin", example = "1", required = true) @PathVariable("ids")List<Long> ids) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbPhieuKiemTraChatLuongServiceImpl.detail(ids).get(0));
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
    public ResponseEntity<BaseResponse> updateStatus( @CurrentUser CustomUserDetails currentUser,@Valid @RequestBody StatusReq stReq) {
        BaseResponse resp = new BaseResponse();
        try {
            dcnbPhieuKiemTraChatLuongServiceImpl.approve(currentUser,stReq);
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

    @ApiOperation(value = "Xoá thông tin", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            dcnbPhieuKiemTraChatLuongServiceImpl.delete(idSearchReq);
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

    @ApiOperation(value = "Xoá danh sách thông tin", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value =  PathContains.URL_XOA_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> deleteMulti(@Valid @RequestBody IdSearchReq idSearchReq) {
        BaseResponse resp = new BaseResponse();
        try {
            dcnbPhieuKiemTraChatLuongServiceImpl.deleteMulti(idSearchReq);
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

    @ApiOperation(value = "Kết xuất danh sách ", response = List.class)
    @PostMapping(value =  PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void exportList(@CurrentUser CustomUserDetails currentUser ,@Valid @RequestBody  SearchPhieuKtChatLuong objReq, HttpServletResponse response) throws Exception {
        try {
            dcnbPhieuKiemTraChatLuongServiceImpl.export( currentUser,objReq, response);

        } catch (Exception e) {
            log.error("Kết xuất danh sách : {}", e);
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
    public ResponseEntity<BaseResponse> preview(@RequestBody DcnbPhieuKtChatLuongHdrReq objReq) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(dcnbPhieuKiemTraChatLuongServiceImpl.preview(objReq));
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
