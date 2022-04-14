package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLHD_MUA_VAT_TU")
public class QlhdMuaVatTu extends BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CAN_CU_ID")
    private Long canCuId;

    @Column(name = "SO_HOP_DONG")
    private String soHopDong;

    @Column(name = "TEN_HOP_DONG")
    private String tenHopDong;

    @Column(name = "DON_VI_TRUNG_THAU_ID")
    private Long donViTrungThauId;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "THONG_TIN_CHUNG_ID")
    private Long thongTinChungId;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "MA_DON_VI")
    private String maDonVi;

    @Transient
    private List<QlhdmvtDsGoiThau> danhSachGoiThau;

    @Transient
    private List<QlhdmvtPhuLucHopDong> phuLucHopDongList;
}
