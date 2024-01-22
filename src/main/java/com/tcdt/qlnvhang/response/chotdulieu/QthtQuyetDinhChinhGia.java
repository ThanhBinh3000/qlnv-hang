package com.tcdt.qlnvhang.response.chotdulieu;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QthtQuyetDinhChinhGia {
    private String soQuyetDinhDieuChinhGia;
    private LocalDate ngayKyQuyetDinhDieuChinh;
    private List<String> danhSachTenCuc;
    private List<String> danhSachMaCuc;
}
