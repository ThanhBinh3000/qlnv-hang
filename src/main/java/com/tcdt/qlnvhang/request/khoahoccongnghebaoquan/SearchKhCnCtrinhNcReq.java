package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;


@Data
public class SearchKhCnCtrinhNcReq extends BaseRequest {
    private String maDeTai;
    private String tenDeTai;
    private String capDeTai;
    private String trangThai;
    private String tuNam;
    private String denNam;
    private String maDvi;

}
