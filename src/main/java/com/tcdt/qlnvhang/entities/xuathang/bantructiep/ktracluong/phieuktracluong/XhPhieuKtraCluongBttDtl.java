package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_PHIEU_KTRA_CLUONG_BTT_DTL")
@Data
public class XhPhieuKtraCluongBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KTRA_CLUONG_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KTRA_CLUONG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_KTRA_CLUONG_DTL_SEQ", allocationSize = 1, name = "XH_PHIEU_KTRA_CLUONG_DTL_SEQ")
    @Column(name = "ID")
    private Long id;

    private Long idHdr;

    private String tenTchuan;

    private String ketQuaKiemTra; // Ket qua phan tich

    private String phuongPhap;

    private String trangThai;

    private String chiSoNhap;

    private String kieu;

    private String danhGia;

}
