package com.tcdt.qlnvhang.request.xuathang.bbhaodoi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhBienBanHaoDoiCtReq {
    private String thoigianBaoquan;
    private double slBaoQuan;
    private double slHaoHut;
    private double DinhMucHaoHut;
}
