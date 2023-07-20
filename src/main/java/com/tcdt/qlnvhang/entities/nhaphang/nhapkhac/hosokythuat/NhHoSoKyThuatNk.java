package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = NhHoSoKyThuatNk.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class NhHoSoKyThuatNk extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 6932032273505129317L;
    public static final String TABLE_NAME = "NH_HO_SO_KY_THUAT_NK";
    public static final String CAN_CU = TABLE_NAME + "_" + "CAN_CU";
    
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HO_SO_KY_THUAT_SEQ")
//    @SequenceGenerator(sequenceName = "HO_SO_KY_THUAT_SEQ", allocationSize = 1, name = "HO_SO_KY_THUAT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "ID_BB_LAY_MAU")
    private Long idBbLayMau;

    @Column(name = "SO_BB_LAY_MAU")
    private String soBbLayMau;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "TEN_NGUOI_TAO")
    private String tenNguoiTao;

    @Transient
    private String tenDvi;

    @Column(name = "SO_HO_SO_KY_THUAT")
    private String soHoSoKyThuat;

    @Column(name = "NAM")
    private Integer nam;

    //bo sung cot cho ktcl xuat kho
    private Integer idBbLayMauXuat;

    private Boolean kqKiemTra;

    @Column(name = "SO_BB_KTRA_NGOAI_QUAN")
    private String soBbKtraNgoaiQuan;

    @Column(name = "SO_BB_KTRA_VAN_HANH")
    private String soBbKtraVanHanh;

    @Column(name = "SO_BB_KTRA_HSKT")
    private String soBbKtraHskt;

    @Transient
    private List<NhHoSoKyThuatCtNk> children = new ArrayList<>();

    @Transient
    private List<NhHoSoBienBanNk> listHoSoBienBan = new ArrayList<>();

}
