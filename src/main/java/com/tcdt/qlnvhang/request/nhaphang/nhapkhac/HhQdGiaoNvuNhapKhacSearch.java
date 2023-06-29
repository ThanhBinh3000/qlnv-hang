package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class HhQdGiaoNvuNhapKhacSearch extends BaseRequest {
    private Integer nam;
    private String soQd;
    private String maDvi;
    private String trangThai;
    private String loaiVthh;
    private String trichYeu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date tuNgayQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date denNgayQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date tuNgayDuyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date denNgayDuyet;
    String tuNgayQdStr;
    String denNgayQdStr;
    String tuNgayDuyetStr;
    String denNgayDuyetStr;
}
