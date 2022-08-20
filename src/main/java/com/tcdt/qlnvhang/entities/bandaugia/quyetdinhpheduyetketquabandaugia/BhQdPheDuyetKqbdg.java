package com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = BhQdPheDuyetKqbdg.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhQdPheDuyetKqbdg extends BaseEntity implements Serializable {
    
    public static final String TABLE_NAME = "BH_QD_PHE_DUYET_KQBDG";
    private static final long serialVersionUID = 2536708001250297716L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_PHE_DUYET_KQBDG_SEQ")
    @SequenceGenerator(sequenceName = "QD_PHE_DUYET_KQBDG_SEQ", allocationSize = 1, name = "QD_PHE_DUYET_KQBDG_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "SO_QUYET_DINH")
    private String soQuyetDinh;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "NGAY_HIEU_LUC")
    private LocalDate ngayHieuLuc;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "THONG_BAO_BDG_ID")
    private Long thongBaoBdgId;

    @Column(name = "BIEN_BAN_BDG_ID")
    private Long bienBanBdgId;

    @Column(name = "THONG_BAO_BDG_KT_ID")
    private Long thongBaoBdgKtId;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPduyet;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<BhQdPheDuyetKhBdgThongTinTaiSan> cts = new ArrayList<>();
}
