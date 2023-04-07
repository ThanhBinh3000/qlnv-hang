package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
public class SearchKhCnCtrinhNcReq extends BaseRequest {
    private String maDeTai;
    private String tenDeTai;
    private String capDeTai;
    private String trangThai;
    private String maDvi;
    private String dvpl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianDen;

}
