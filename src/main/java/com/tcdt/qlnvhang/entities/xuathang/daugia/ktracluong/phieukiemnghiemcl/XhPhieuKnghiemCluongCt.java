package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_PHIEU_KNGHIEM_CLUONG_CT")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhPhieuKnghiemCluongCt implements Serializable {
    private static final long serialVersionUID = -1315211820556764708L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KNGHIEM_CLUONG_CT_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_KNGHIEM_CLUONG_CT_SEQ", allocationSize = 1, name = "XH_PHIEU_KNGHIEM_CLUONG_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    private Long idHdr;

    private String tenTchuan;

    private String ketQuaKiemTra; // Ket qua phan tich

    private String phuongPhap;

    private String trangThai;

    private String chiSoNhap;

    private String kieu;

}
