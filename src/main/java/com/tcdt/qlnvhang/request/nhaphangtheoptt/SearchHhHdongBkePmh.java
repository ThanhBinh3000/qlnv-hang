package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;
@Data
public class SearchHhHdongBkePmh extends BaseRequest {
    Integer namHd;
    String soHdong;
    String tenHdong;
    String dviMua;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayKyHdTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayKyHdDen;
    String maDvi;
    String TrangThai;

}
