package com.tcdt.qlnvhang.response.dtvt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
