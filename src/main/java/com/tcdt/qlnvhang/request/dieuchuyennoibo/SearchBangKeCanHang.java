package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchBangKeCanHang extends BaseRequest {
    private Integer nam;
    private String loaiDc;
    private String soQdinhDcc;
    private String soBangKe;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
}
