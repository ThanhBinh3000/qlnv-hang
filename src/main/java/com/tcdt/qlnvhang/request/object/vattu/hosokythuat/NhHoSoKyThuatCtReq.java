package com.tcdt.qlnvhang.request.object.vattu.hosokythuat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatCtReq {
    private Long id;
    private String tenHoSo;
    private String loaiTaiLieu;
    private Integer soLuong;
    private String ghiChu;
}
