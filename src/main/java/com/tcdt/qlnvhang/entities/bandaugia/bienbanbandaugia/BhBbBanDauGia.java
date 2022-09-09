package com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BH_BB_BAN_DAU_GIA")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhBbBanDauGia extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2543642789249190419L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_BAN_DAU_GIA_SEQ")
    @SequenceGenerator(sequenceName = "BB_BAN_DAU_GIA_SEQ", allocationSize = 1, name = "BB_BAN_DAU_GIA_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "THONG_BAO_BDG_ID")
    private Long thongBaoBdgId;

    @Column(name = "DON_VI_THONG_BAO")
    private String donViThongBao;

    @Column(name = "NGAY_TO_CHUC_TU")
    private LocalDate ngayToChucTu;

    @Column(name = "NGAY_TO_CHUC_DEN")
    private LocalDate ngayToChucDen;

    @Column(name = "DIA_DIEM")
    private String diaDiem;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPduyet;

    @Column(name = "SO")
    private Integer so;

    @Transient
    private List<BhBbBanDauGiaCt> cts = new ArrayList<>();

    @Transient
    private List<BhQdPheDuyetKhBdgThongTinTaiSan> ct1s = new ArrayList<>();
}
