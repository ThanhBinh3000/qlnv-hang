package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BTT_DVI_DTL")
@Data
public class XhQdPdKhBttDviDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_PL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BTT_DVI_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BTT_DVI_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BTT_DVI_DTL_SEQ")
    private Long id;

    private Long idDvi;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    @Transient
    private String diaDiemKho;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;

    private String maNganKho;
    @Transient
    private String tenNganKho;

    private String maLoKho;
    @Transient
    private String tenLoKho;

    private String maDviTsan;

    private BigDecimal duDau;

    private BigDecimal soLuong;

    private BigDecimal donGiaDeXuat;

    private BigDecimal donGiaVat;

    private String dviTinh;

    @Transient
    private List<XhTcTtinBtt> children = new ArrayList<>();

}
