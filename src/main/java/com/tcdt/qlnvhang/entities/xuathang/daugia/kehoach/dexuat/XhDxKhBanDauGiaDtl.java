package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhDxKhBanDauGiaDtl.TABLE_NAME)
@Data
public class XhDxKhBanDauGiaDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxKhBanDauGiaDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhDxKhBanDauGiaDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDxKhBanDauGiaDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private BigDecimal slChiTieu;
    private BigDecimal tongSlKeHoachDd;
    private BigDecimal tongSlXuatBanDx;
    private BigDecimal tongTienDatTruocDx;
    private String donViTinh;
    private String diaChi;

    @Transient
    private String tenDvi;
    @Transient
    private List<XhDxKhBanDauGiaPhanLo> children = new ArrayList<>();
}
