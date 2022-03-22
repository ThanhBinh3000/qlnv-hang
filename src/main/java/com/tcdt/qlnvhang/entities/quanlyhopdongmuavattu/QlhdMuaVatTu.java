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
@Table(name = "QLHD_MUA_VAT_TU")
public class QlhdMuaVatTu {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CAN_CU_ID")
    private Long canCuId;

    @Column(name = "DV_TRUNG_THAU_ID")
    private Long dvTrungThauId;

    @Column(name = "SO_HOP_DONG")
    private String soHopDong;

    @Column(name = "TEN_HOP_DONG")
    private String tenHopDong;

    @Column(name = "DON_VI_TRUNG_THAU_ID")
    private Long donViTrungThauId;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "THONG_TIN_CHUNG_ID")
    private Long thongTinChungId;
}
