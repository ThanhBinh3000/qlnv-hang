package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListHdSearhReq extends BaseRequest {
    String loaiVthh;
    String maDvi;
    String trangThai;
}
