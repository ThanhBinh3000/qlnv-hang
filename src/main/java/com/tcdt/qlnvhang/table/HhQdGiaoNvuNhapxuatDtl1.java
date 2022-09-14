package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tcdt.qlnvhang.entities.kehoachluachonnhathau.DiaDiemNhap;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NH_QD_GIAO_NVU_NHAPXUAT_CT1")
@Data
public class HhQdGiaoNvuNhapxuatDtl1 implements Serializable {


    private static final long serialVersionUID = -6895604378943086007L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_CT1_SEQ")
    @SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_CT1_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_CT1_SEQ")
    private Long id;

    @Column(name = "id_hdr")
    private Long idHdr;

    @Column(name = "HOP_DONG_ID")
    private Long hopDongId;

    @Transient
    private String soHd;

    @Transient
    List<HhHopDongDdiemNhapKho> dongDdiemNhapKhos =new ArrayList<>();

}
