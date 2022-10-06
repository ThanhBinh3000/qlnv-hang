package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhThongTinDviDtuCcapReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private String nguoiDdien;
    private String chucVu;
    private String sdt;
    private String fax;
    private String soTkhoan;
    private String moTai;
    private String giayUq;
    private String type;
}
