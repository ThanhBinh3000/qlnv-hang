package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_BB_NGHIEM_THU_DTL")
@Data
public class HhBbanNghiemThuDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BB_NGHIEM_THU_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BB_NGHIEM_THU_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_BB_NGHIEM_THU_DTL_SEQ", allocationSize = 1, name = "HH_BB_NGHIEM_THU_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String noiDung;
    private String dvt;
    private BigDecimal soLuongTn;
    private BigDecimal donGiaTn;
    private BigDecimal donGiaQt;
    private BigDecimal thanhTienTn;
    private BigDecimal soLuongQt;
    private BigDecimal thanhTienQt;
    private BigDecimal tongGtri;

}
