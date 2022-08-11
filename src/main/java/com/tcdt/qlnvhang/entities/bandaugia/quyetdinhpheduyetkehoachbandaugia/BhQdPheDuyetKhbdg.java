package com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "BH_QD_PHE_DUYET_KHBDG")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhQdPheDuyetKhbdg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2873038243045535881L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_PHE_DUYET_KHBDG_SEQ")
    @SequenceGenerator(sequenceName = "QD_PHE_DUYET_KHBDG_SEQ", allocationSize = 1, name = "QD_PHE_DUYET_KHBDG_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MA_DON_VI")
    private String maDonVi;

    @Column(name = "CAP_DON_VI")
    private String capDonVi;

    @Column(name = "NAM_KE_HOACH")
    private Long namKeHoach;

    @Column(name = "SO_QUYET_DINH")
    private String soQuyetDinh;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "NGAY_HIEU_LUC")
    private LocalDate ngayHieuLuc;

    @Column(name = "TONG_HOP_DE_XUAT_KHBDG_ID")
    private Long tongHopDeXuatKhbdgId;

    @Column(name = "THOI_HAN_TC_BDG")
    private LocalDate thoiHanTcBdg;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGUOI_PHE_DUYET_ID")
    private Long nguoiPheDuyetId;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

}
