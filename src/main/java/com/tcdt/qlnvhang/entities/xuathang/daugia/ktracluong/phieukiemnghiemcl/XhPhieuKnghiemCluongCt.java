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

    @Column(name = "TEN_TCHUAN")
    private String tenTchuan;

    @Column(name = "KET_QUA_KIEM_TRA")
    private String ketQuaKiemTra; // Ket qua phan tich

    @Column(name = "PHUONG_PHAP")
    private String phuongPhap;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "PHIEU_KT_CHAT_LUONG_ID")
    private Long phieuKtChatLuongId;

    @Column(name = "CHI_SO_NHAP")
    private String chiSoNhap;

    @Column(name = "KIEU")
    private String kieu;

}
