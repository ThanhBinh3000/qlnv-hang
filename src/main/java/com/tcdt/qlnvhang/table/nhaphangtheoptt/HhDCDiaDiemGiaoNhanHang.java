package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_DC_DDIEM_GIAO_NHAN_HANG")
@Data
public class HhDCDiaDiemGiaoNhanHang implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DC_DDIEM_GIAO_NHAN_HANG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DC_DDIEM_GIAO_NHAN_HANG_SEQ")
    @SequenceGenerator(sequenceName = "HH_DC_DDIEM_GIAO_NHAN_HANG_SEQ", allocationSize = 1, name = "HH_DC_DDIEM_GIAO_NHAN_HANG_SEQ")
    private Long id;
    private Long idHdPluc;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;

}
