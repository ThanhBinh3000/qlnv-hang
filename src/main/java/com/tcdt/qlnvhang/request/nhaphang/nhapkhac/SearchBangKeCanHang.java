package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SearchBangKeCanHang extends BaseRequest {
    private Integer nam;
    private String loaiVthh;
    private String soQdinhNk;
    private String soBangKe;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maLoKho;
    private String maNganKho;
    private String maDvi;
}
