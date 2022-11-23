package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_QD_PD_KH_BDG_PL_DTL")
@Data
public class XhQdPdKhBdgPlDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_PL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_PL_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_PL_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_PL_DTL_SEQ")
    private Long id;
    private Long idPl;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNganKho;
    @Transient
    private String tenNganKho;
    private String maLoKho;
    @Transient
    private String tenLoKho;
    private String maDviTsan;
    private BigDecimal soLuong;
    private String DviTinh;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal tienDatTruoc;
}
