package com.tcdt.qlnvhang.request.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.request.xuathang.bbtinhkho.XhBienBanTinhKhoCtReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhBienBanHaoDoiReq {
    private Long id;
    private Long bbTinhKhoId;
    private String maDvi;
    private String capDvi;
    private String nguyenNhan;
    private String kienNghi;
    List<XhBienBanHaoDoiCtReq> ds = new ArrayList<>();
}
