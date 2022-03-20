package com.tcdt.qlnvhang.response.dtvt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class QdPheDuyetKqlcntVtRes {
    private Long id;
    private String soQuyetDinh;
    private LocalDate ngayKy;
    private BigDecimal giaTriTrungThau;
    private LocalDate thoiGianThucHien;
    private LocalDate thoiGianNhapHang;
}
