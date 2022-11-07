package com.tcdt.qlnvhang.entities.nhaphang.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = NhHoSoKyThuat.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class NhHoSoKyThuat extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 6932032273505129317L;
    public static final String TABLE_NAME = "NH_HO_SO_KY_THUAT";
    public static final String CAN_CU = TABLE_NAME + "_" + "CAN_CU";
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HO_SO_KY_THUAT_SEQ")
    @SequenceGenerator(sequenceName = "HO_SO_KY_THUAT_SEQ", allocationSize = 1, name = "HO_SO_KY_THUAT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "BIEN_BAN_GIAO_MAU_ID")
    private String bienBanGiaoMauId;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "HOP_DONG_ID")
    private Long hopDongId;

    @Column(name = "NGAY_HOP_DONG")
    private LocalDate ngayHopDong;

    @Column(name = "DON_VI_CUNG_CAP")
    private String donViCungCap;

    @Column(name = "NGAY_KIEM_TRA")
    private LocalDate ngayKiemTra;

    @Column(name = "DIA_DIEM_KIEM_TRA")
    private String diaDiemKiemTra;

    @Column(name = "SO_LUONG_THUC_NHAP")
    private BigDecimal soLuongThucNhap;

    @Column(name = "THOI_GIAN_NHAP")
    private LocalDate thoiGianNhap;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Column(name = "VBTL_CAN_BO_SUNG")
    private String vbtlCanBoSung;

    @Column(name = "CBTL_CAN_HOAN_THIEN")
    private String cbtlCanHoanThien;

    @Column(name = "TG_BS_TRUOC_NGAY")
    private LocalDate tgBsTruocNgay;

    @Column(name = "TG_HT_TRUOC_NGAY")
    private LocalDate tgHtTruocNgay;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    @Transient
    String tenVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private Integer so;
    private Integer nam;

    @Transient
    private List<NhHoSoKyThuatCt> chiTiets = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fdkCanCus = new ArrayList<>();
}
