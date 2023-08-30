package com.tcdt.qlnvhang.request.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QdPdHsmtSearchReq extends BaseRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date tuNgayKy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    Date denNgayKy;
    private String namKhoach;
    private String soQd;
    private String soQdPdKhlcnt;
    private String loaiVthh;
    private String trichYeu;
    private String maDvi;
}
