package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhHSoKthuatBttHdr.TABLE_NAME)
@Data
public class XhHSoKthuatBttHdr extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HSO_KTHUAT_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name =TABLE_NAME + "_SEQ")
    private Long id;

    private Long namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soHoSoKyThuat;

    private String soBienBanKnhap;

    private Long idQd;

    private String soQd;

    private String soHd;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;

    private String maNganKho;
    @Transient
    private String tenNganKho;

    private String maLoKho;
    @Transient
    private String tenLoKho;

    private String soBienBanKxuat;

    private Boolean kqKiemTra;

    private String noiDungKdatYeuCau;

    @Transient
    private List<XhHSoKthuatBttDtl> children = new ArrayList<>();
}
