package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
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
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HO_SO_KY_THUAT_SEQ")
//    @SequenceGenerator(sequenceName = "HO_SO_KY_THUAT_SEQ", allocationSize = 1, name = "HO_SO_KY_THUAT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "SO_BB_LAY_MAU")
    private String soBbLayMau;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Transient
    private String tenDvi;

    @Column(name = "SO_HO_SO_KY_THUAT")
    private String soHoSoKyThuat;

    @Column(name = "NAM")
    private Integer nam;

    //bo sung cot cho ktcl xuat kho
    private Integer idBbLayMauXuat;

    private Boolean kqKiemTra;

    @Transient
    private List<NhHoSoKyThuatCt> children = new ArrayList<>();

    @Transient
    private List<NhHoSoBienBan> listHoSoBienBan = new ArrayList<>();

    @Transient
    private BienBanLayMau bienBanLayMau;
    @Transient
    private NhQdGiaoNvuNhapxuatHdr qdGiaoNvuNhapxuatHdr;

    @Transient
    private String soBbKtnq;
    @Transient
    private String soBbKtvh;
    @Transient
    private String soBbKthskt;
}
