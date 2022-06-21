package com.tcdt.qlnvhang.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseNhapHangCount {
    private int tatCa;
    private int thoc;
    private int gao;
    private int muoi;
    private int vatTu;
}
