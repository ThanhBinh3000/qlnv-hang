package com.tcdt.qlnvhang.response.dtvt;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DtNhaThauVtRes {
    private Long id;
    private Integer stt;
    private String ten;
    private String diaChi;
    private String soDienThoai;
    private Integer diem;
    private Integer xepHang;
    private BigDecimal donGiaDuThau;
    private BigDecimal giaDuThau;
    private String lyDo;
}
