package com.tcdt.qlnvhang.controller.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtReq;
import com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtService;
import com.tcdt.qlnvhang.util.PathContains;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathContains.QL_PHE_DUYET_KQLCNT_VT)
@Api(tags = "Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư")
public class QdPheDuyetKqlcntVtController {

    @Autowired
    private QdPheDuyetKqlcntVtService qdPheDuyetKqlcntVtService;

    @ApiOperation(value = "Tạo mới Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư", response = List.class)
    @PostMapping
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody QdPheDuyetKqlcntVtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qdPheDuyetKqlcntVtService.create(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tạo mới Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Sửa Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư", response = List.class)
    @PutMapping
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody QdPheDuyetKqlcntVtReq request) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qdPheDuyetKqlcntVtService.update(request));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Sửa Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Chi tiết quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư", response = List.class)
    @GetMapping("/id")
    public ResponseEntity<BaseResponse> detail(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qdPheDuyetKqlcntVtService.detail(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Chi tiết Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Xóa quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư", response = List.class)
    @DeleteMapping("/id")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qdPheDuyetKqlcntVtService.delete(id));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Xóa Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Phê duyệt/ từ chối quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư", response = List.class)
    @PutMapping("/status")
    public ResponseEntity<BaseResponse> updateStatus(@Valid @RequestBody StatusReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qdPheDuyetKqlcntVtService.updateStatusQd(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Phê duyệt/ từ chối Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "Tra cứu quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư", response = List.class)
    @GetMapping()
    public ResponseEntity<BaseResponse> search(QdPheDuyetKqlcntVtSearchReq req) {
        BaseResponse resp = new BaseResponse();
        try {
            resp.setData(qdPheDuyetKqlcntVtService.search(req));
            resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
            resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
        } catch (Exception e) {
            resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
            resp.setMsg(e.getMessage());
            log.error("Tra cứu Quyết định phê duyệt kết quả lựa chọn nhà thầu vật tư lỗi: {}", e);
        }
        return ResponseEntity.ok(resp);
    }
}
