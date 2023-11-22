package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdPdKhBdgPl.TABLE_NAME)
@Data
public class XhQdPdKhBdgPl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_PL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBdgPl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBdgPl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBdgPl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idDtl;
    private String maDvi;
    private BigDecimal slChiTieu;
    private BigDecimal tongSlKeHoachDd;
    private BigDecimal tongSlXuatBanDx;
    private BigDecimal tongTienDatTruocDx;
    private String donViTinh;
    private String diaChi;
    private BigDecimal soTienDuocDuyet;
    private BigDecimal soTienDtruocDduyet;
    @Transient
    private String tenDvi;
    @Transient
    List<XhQdPdKhBdgPlDtl> children =new ArrayList<>();
}
