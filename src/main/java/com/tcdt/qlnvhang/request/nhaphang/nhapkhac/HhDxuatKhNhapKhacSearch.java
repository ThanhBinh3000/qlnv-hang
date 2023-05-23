package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class HhDxuatKhNhapKhacSearch extends BaseRequest {
    private Integer namKhoach;
    private String soDxuat;
    private String maDviDxuat;
    private String trangThai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date tuNgayDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date denNgayDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date tuNgayDuyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date denNgayDuyet;
    String tuNgayDxuatStr;
    String denNgayDxuatStr;
    String tuNgayDuyetStr;
    String denNgayDuyetStr;
}
