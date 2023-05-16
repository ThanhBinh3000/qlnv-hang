package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBdgPl.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgPl  implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_PL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;

    private Long idDcDtl;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal slChiTieu;

    private BigDecimal slKeHoachDd;

    private BigDecimal soLuongChiCuc;

    private String donViTinh;

    @Transient
    private BigDecimal soTienDtruocDx;

    private String diaChi;

    @Transient
    private BigDecimal soTienDtruocDd;

    @Transient
    private List<XhQdDchinhKhBdgPlDtl> children =new ArrayList<>();
}
