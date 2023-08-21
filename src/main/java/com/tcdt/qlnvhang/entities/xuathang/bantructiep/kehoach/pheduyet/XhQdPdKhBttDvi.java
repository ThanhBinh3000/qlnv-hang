package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BTT_DVI")
@Data
public class XhQdPdKhBttDvi implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_DVI";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BTT_DVI_SEQ ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BTT_DVI_SEQ ", allocationSize = 1, name = "XH_QD_PD_KH_BTT_DVI_SEQ ")
    private Long id;
    private Long idDtl;
    private String maDvi;
    private BigDecimal soLuongChiCuc;
    private String diaChi;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDaDuyet;
    private String donViTinh;
    @Transient
    private String tenDvi;
    @Transient
    List<XhQdPdKhBttDviDtl> children =new ArrayList<>();
}
