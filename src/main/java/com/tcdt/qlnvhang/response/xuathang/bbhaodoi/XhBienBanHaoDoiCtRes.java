package com.tcdt.qlnvhang.response.xuathang.bbhaodoi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhBienBanHaoDoiCtRes {
    private Long id;
    private String thoigianBaoquan;
    private double slBaoQuan;
    private double slHaoHut;
    private double DinhMucHaoHut;
}
