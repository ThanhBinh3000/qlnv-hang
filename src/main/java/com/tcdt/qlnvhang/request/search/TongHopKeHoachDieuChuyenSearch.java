package com.tcdt.qlnvhang.request.search;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Data
@NoArgsConstructor
public class TongHopKeHoachDieuChuyenSearch extends BaseRequest {
    private Integer nam;
    private String maTongHop;
    private String loaiDieuChuyen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tuNgay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date denNgay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianTongHop;
    private String trichYeu;
    private String maDVi;
    private String loaiHH;
    private String chungLoaiHH;
}
