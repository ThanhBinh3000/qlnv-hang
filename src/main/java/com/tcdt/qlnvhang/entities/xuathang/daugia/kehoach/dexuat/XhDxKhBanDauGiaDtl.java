package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_DX_KH_BAN_DAU_GIA_DTL")
@Data
public class XhDxKhBanDauGiaDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_DTL_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_DTL_SEQ")
    private Long id;

    private Long idHdr;

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
    private List<XhDxKhBanDauGiaPhanLo> children = new ArrayList<>();

}
