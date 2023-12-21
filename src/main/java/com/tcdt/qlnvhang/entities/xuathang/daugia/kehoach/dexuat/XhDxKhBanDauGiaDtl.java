package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhDxKhBanDauGiaDtl.TABLE_NAME)
@Data
public class XhDxKhBanDauGiaDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA_DTL";

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
    private BigDecimal soTienDuocDuyet;
    @Transient
    private BigDecimal soTienDtruocDduyet;
    @Transient
    private String tenDvi;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
    }

    @Transient
    private List<XhDxKhBanDauGiaPhanLo> children = new ArrayList<>();
}