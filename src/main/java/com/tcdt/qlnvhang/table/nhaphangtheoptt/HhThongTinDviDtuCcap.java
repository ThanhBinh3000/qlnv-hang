package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_THONG_TIN_DVI_DTU_CCAP")
@Data

public class HhThongTinDviDtuCcap implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_THONG_TIN_DVI_DTU_CCAP";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_THONG_TIN_DVI_DTU_CCAP_SEQ")
    @SequenceGenerator(sequenceName = "HH_THONG_TIN_DVI_DTU_CCAP_SEQ", allocationSize = 1, name = "HH_THONG_TIN_DVI_DTU_CCAP_SEQ")

    private Long id;
    Long idHdr;
    String shgt;
    String tenGthau;
    BigDecimal soLuong;
    BigDecimal donGiaVat;
    Long vat;
    BigDecimal giaTruocVat;
    BigDecimal giaSauVat;
    String type;
    String maDvi;
    @Transient
    String tenDvi;
    String diaDiemNhap;
    String trangThai;
    @Transient
    private List<HhDiaDiemGiaoNhanHang> children=new ArrayList<>();


}
