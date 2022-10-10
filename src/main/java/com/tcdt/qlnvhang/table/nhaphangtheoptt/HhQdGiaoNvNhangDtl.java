package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_QD_GIAO_NV_NHAP_HANG_DTL")
@Data
public class HhQdGiaoNvNhangDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_GIAO_NV_NHAP_HANG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_GIAO_NV_NH_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_GIAO_NV_NH_DTL_SEQ", allocationSize = 1, name = "HH_QD_GIAO_NV_NH_DTL_SEQ")
    private Long id;
    private Long idQdHdr;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<HhQdGiaoNvNhDdiem> hhQdGiaoNvNhDdiemList = new ArrayList<>();
}
