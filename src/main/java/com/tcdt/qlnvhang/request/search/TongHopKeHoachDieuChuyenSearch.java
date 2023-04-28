package com.tcdt.qlnvhang.request.search;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
public class TongHopKeHoachDieuChuyenSearch extends BaseRequest {
    private Integer namKeHoach;
    private String maTongHop;
    private String soDeXuat;
    private String loaiDieuChuyen;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private LocalDateTime thoiGianTongHop;
    private String trichYeu;
    private String maDVi;
    private String loaiHangHoa;
    private String chungLoaiHangHoa;
}
