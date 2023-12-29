package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class SearchHhDcQdPduyetKhMttReqDTO extends BaseRequest {
    private Integer namKh;

    private String soQd;

    private String trichYeu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date ngayQdTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private  Date ngayQdDen;

    private String trangThai;

    private String maDvi;

    private Integer lastest;

    private String loaiVthh;
}
