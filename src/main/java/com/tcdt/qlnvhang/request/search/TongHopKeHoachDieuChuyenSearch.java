package com.tcdt.qlnvhang.request.search;


import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class TongHopKeHoachDieuChuyenSearch extends BaseRequest {
    private Integer nam;
    private String maTongHop;
    private String loaiDieuChuyen;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private LocalDate thoiGianTongHop;
    private String trichYeu;
    private String maDonVi;
}
