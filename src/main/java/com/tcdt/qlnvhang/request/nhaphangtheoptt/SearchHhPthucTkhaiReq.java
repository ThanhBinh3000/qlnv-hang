package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SearchHhPthucTkhaiReq extends BaseRequest {

    Integer namKh;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayCgiaTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayCgiadDen;
    String maDvi;
    String loaiVthh;
    String cloaiVthh;
    String trangThai;
    String trangThaiTk;
    String ctyCgia;
    String pthucMuatt;
}
