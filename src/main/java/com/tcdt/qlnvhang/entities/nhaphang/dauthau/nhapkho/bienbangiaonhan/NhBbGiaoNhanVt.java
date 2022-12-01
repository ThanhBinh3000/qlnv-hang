package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = NhBbGiaoNhanVt.TABLE_NAME)
public class NhBbGiaoNhanVt extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 9043465194089068380L;
    public static final String TABLE_NAME = "NH_BB_GIAO_NHAN_VT";
    public static final String CAN_CU = TABLE_NAME + "_CAN_CU";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_GIAO_NHAN_VT_SEQ")
    @SequenceGenerator(sequenceName = "BB_GIAO_NHAN_VT_SEQ", allocationSize = 1, name = "BB_GIAO_NHAN_VT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "BB_KT_NHAP_KHO_ID")
    private Long bbKtNhapKhoId;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "NGAY_KY")
    private LocalDate ngayKy;

    @Column(name = "HOP_DONG_ID")
    private Long hopDongId;

    @Column(name = "NGAY_HOP_DONG")
    private LocalDate ngayHopDong;

    @Column(name = "BB_GUI_HANG_ID")
    private Long bbGuiHangId;

    @Column(name = "NGAY_KY_BB_GH")
    private LocalDate ngayKyBbGh;

    @Column(name = "HO_S_KY_THUAT_ID")
    private Long hoSKyThuatId;

    @Column(name = "NGAY_KY_HSKT")
    private LocalDate ngayKyHskt;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;

    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "SO")
    private Integer so;

    @Column(name = "NAM")
    private Integer nam;

    @Transient
    private List<NhBbGiaoNhanVtCt> chiTiets = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCus = new ArrayList<>();
}
