package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchDcnbBienBanLayMau extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String loaiDc;
    private String trangThai;
}
