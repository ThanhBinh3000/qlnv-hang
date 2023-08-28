package com.tcdt.qlnvhang.request.feign;

import lombok.Data;

@Data
public class KeHoachRequest {
    private Integer namKh;
    private String maDvi;
    private String loaiVthh;

    //thongtingia
    private String trangThai;
    private Long namKeHoach;
    private String cloaiVthh;
    private String loaiGia;
}
