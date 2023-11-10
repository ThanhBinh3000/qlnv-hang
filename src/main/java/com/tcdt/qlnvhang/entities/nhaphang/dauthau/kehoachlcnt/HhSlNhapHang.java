package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = HhSlNhapHang.TABLE_NAME)
@Data
public class HhSlNhapHang {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_SL_NHAP_HANG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_SL_NHAP_HANG_SEQ")
    @SequenceGenerator(sequenceName = "HH_SL_NHAP_HANG_SEQ", allocationSize = 1, name = "HH_SL_NHAP_HANG_SEQ")
    private Long id;
    private Long idDxKhlcnt;
    private Long idQdKhlcnt;
    private Integer namKhoach;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private String kieuNhap;
    private BigDecimal slDeXuat;
    private BigDecimal slDaThucHien;
}
