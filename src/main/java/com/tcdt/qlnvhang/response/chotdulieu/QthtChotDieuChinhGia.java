package com.tcdt.qlnvhang.response.chotdulieu;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QthtChotDieuChinhGia {
    private LocalDate ngayChotDieuChinh;
    private LocalDate ngayHieuLuc;
    private List<String> danhSachTenCuc;
    private List<String> danhSachMaCuc;
}
