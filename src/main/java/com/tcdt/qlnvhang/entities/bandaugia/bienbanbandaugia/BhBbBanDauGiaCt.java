package com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BH_BB_BAN_DAU_GIA_CT")
@Data
@NoArgsConstructor
public class BhBbBanDauGiaCt implements Serializable {
    private static final long serialVersionUID = -6460659025678257635L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_BAN_DAU_GIA_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_BAN_DAU_GIA_CT_SEQ", allocationSize = 1, name = "BB_BAN_DAU_GIA_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "STT")
    private Long stt;

    @Column(name = "HO_TEN")
    private String hoTen;

    @Column(name = "CHUC_VU")
    private String chucVu;

    @Column(name = "NOI_CONG_TAC")
    private String noiCongTac;

    @Column(name = "LOAI_TPTG")
    private String loaiTptg;

    @Column(name = "BB_BAN_DAU_GIA_ID")
    private Long bbBanDauGiaId;

    @Column(name = "MST_CMTND_CCCD_HO_CHIEU")
    private String mstCmtndCccdHoChieu;

    @Column(name = "DIA_CHI")
    private String diaChi;
}
