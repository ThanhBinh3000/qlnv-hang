package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLHDMVT_DS_GOI_THAU")
public class QlhdmvtDsGoiThau {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "STT")
    private Long stt;

    @Column(name = "TEN_GOI_THAU")
    private String tenGoiThau;

    @Column(name = "SO_HIEU_GOI_THAU")
    private String soHieuGoiThau;

    @Column(name = "SO_LUONG")
    private Long soLuong;

    @Column(name = "THUE_VAT")
    private Long thueVat;

    @Column(name = "DON_GIA_TRUOC_THUE")
    private Long donGiaTruocThue;

    @Column(name = "THUE")
    private String thue;

    @Column(name = "GIA_TRU0C_THUE")
    private Long giaTru0CThue;

    @Column(name = "GIA_SAU_THUE")
    private Long giaSauThue;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "QLHD_MUA_VAT_TU_ID")
    private Long qlhdMuaVatTuId;
}
