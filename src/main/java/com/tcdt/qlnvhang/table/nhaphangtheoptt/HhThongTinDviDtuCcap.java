package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

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
    private Long idHdr;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private String nguoiDdien;
    private String chucVu;
    private String sdt;
    private String fax;
    private String soTkhoan;
    private String moTai;
    private String giayUq;
    private String type;
    private String mst;

}
