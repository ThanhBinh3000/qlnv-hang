package com.tcdt.qlnvhang.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountKhlcntSlReq {
    Integer year;
    Integer idQd;
    String loaiVthh;
    String maDvi;
    Integer lastest ;
    String trangThai;
}
