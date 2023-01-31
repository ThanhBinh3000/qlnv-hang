package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class XhQdDchinhKhBdgSearchReq extends BaseRequest {
    private Integer namKh;

    private  String soQdPd;
    private  String soQdDc;

    private String trichYeu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private  Date ngayKyQdDen;

    private  String soTrHdr;

    private String loaiVthh;

    private  String trangThai;

    private  String maDvi;
    private Integer lastest;
}
