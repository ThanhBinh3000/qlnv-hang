package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class HhBbNghiemThuNhapKhacSearch extends BaseRequest {
    private Integer namKhoach;
    private String soQd;
    private String soBbNtBq;
    private String loaiVthh;
    private String maDvi;
    private String maDviChiCuc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date tuNgayLP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date denNgayLP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date tuNgayKT;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date denNgayKT;
    private String tuNgayLPStr;
    private String denNgayLPStr;
    private String tuNgayKTStr;
    private String denNgayKTStr;
    private String maNganKho;
    private String maLoKho;
    private String trangThaiQdnk;
    private Long idQdGiaoNvnh;
}
