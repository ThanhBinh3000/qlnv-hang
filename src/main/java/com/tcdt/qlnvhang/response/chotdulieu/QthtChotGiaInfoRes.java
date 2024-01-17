package com.tcdt.qlnvhang.response.chotdulieu;

import lombok.Data;

import java.util.List;

@Data
public class QthtChotGiaInfoRes {
    private List<QthtChotDieuChinhGia> qthtChotDieuChinhGia;
    private List<QthtQuyetDinhChinhGia> qthtQuyetDinhChinhGia;
    private QthtDieuChinhKHLCNT qthtDieuChinhKHLCNT;
}
