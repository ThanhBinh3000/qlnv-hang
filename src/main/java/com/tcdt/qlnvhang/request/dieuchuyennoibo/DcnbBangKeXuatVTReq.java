package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DcnbBangKeXuatVTReq extends BaseRequest {
    private Integer nam;
    private String maDvi;
    private String loaiDc;
    private String loaiQdinh;
}
