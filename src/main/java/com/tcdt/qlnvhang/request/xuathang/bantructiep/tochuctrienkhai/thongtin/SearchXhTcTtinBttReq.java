package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SearchXhTcTtinBttReq extends BaseRequest {

    Integer namKh;

    String maDvi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date  ngayCgiaTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date  ngayCgiaDen;

    String maDviChiCuc;

    String tochucCanhan;

    Integer lastest ;

    String loaiVthh;

    String pthucBanTrucTiep;

    Integer typeSoQdKq;
}
