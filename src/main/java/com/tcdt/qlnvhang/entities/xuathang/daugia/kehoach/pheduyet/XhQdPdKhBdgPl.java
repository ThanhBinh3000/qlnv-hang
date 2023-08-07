package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG_PL")
@Data
public class XhQdPdKhBdgPl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_PL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_PL_SEQ ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_PL_SEQ ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_PL_SEQ ")
    private Long id;
    private Long idQdDtl;
    private String maDvi;
    private BigDecimal slChiTieu;
    private BigDecimal slKeHoachDd;
    private BigDecimal soLuongChiCuc;
    private BigDecimal tienDatTruocDx;
    private String donViTinh;
    private String diaChi;
    @Transient
    private String tenDvi;
    @Transient
    List<XhQdPdKhBdgPlDtl> children =new ArrayList<>();
}
