package com.tcdt.qlnvhang.request.xuathang.bangkecanhang;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XhBangKeCanHangCtReq {
    private Long id;
    private Long bkCanHangID;
    private Integer soBB;
    private Integer tlCaBi;
}
