package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.table.FileDinhKem;
import io.swagger.models.auth.In;
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
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_GIAO_NHAN_VT_SEQ")
//    @SequenceGenerator(sequenceName = "BB_GIAO_NHAN_VT_SEQ", allocationSize = 1, name = "BB_GIAO_NHAN_VT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "SO_BB_GIAO_NHAN")
    private String soBbGiaoNhan;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    private LocalDate ngayHd;

    @Column(name = "SO_HO_SO_KY_THUAT")
    private String soHoSoKyThuat;

    @Column(name = "SO_BB_NHAP_DAY_KHO")
    private String soBbNhapDayKho;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "KET_LUAN")
    private String ketLuan;

    @Column(name = "MA_DVI")
    private String maDvi;
    private String maQhns;
    private String cbPhongKtbq;

    @Transient
    private String tenDvi;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenCuc;
    @Transient
    private String tenChiCuc;
    @Transient
    private List<NhBbGiaoNhanVtCt> chiTiets = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuLapBb = new ArrayList<>();

    @Transient
    private NhBbNhapDayKho bbNhapDayKho;
    @Transient
    private NhBienBanGuiHang bbGuiHang;
    @Transient
    private BienBanLayMau bbLayMau;

}
