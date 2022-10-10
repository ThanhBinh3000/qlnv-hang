package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class SearchHhQdGiaoNvNhReq extends BaseRequest {
    private Integer namNhap;
    private String soQd;
    private String loaiVthh;
    private String cloaiVthh;
    private String trichyeu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayQdTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayQdDen;
    private String maDvi;
    private String trangThai;

}
