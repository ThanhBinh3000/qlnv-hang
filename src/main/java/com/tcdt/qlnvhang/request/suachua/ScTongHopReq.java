package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScTongHopReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maDanhSach;
    private String tenDanhSach;
    private LocalDate thoiGianThTu;
    private LocalDate thoiGianThDen;
    private String trangThai;

}
