package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhDxKhBanTrucTiepDtl.TABLE_NAME)
@Data
public class XhDxKhBanTrucTiepDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_TRUC_TIEP_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxKhBanTrucTiepDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhDxKhBanTrucTiepDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDxKhBanTrucTiepDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private BigDecimal soLuongChiCuc;
    private String maDvi;
    private String diaChi;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDaDuyet;
    private String donViTinh;
    private BigDecimal thanhTien;
    @Transient
    private String tenDvi;
    @Transient
    private BigDecimal donGiaDeXuat;
    @Transient
    private BigDecimal tienDuocDuyet;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        boolean isNewValue = !Objects.equals(this.mapDmucDvi, mapDmucDvi);
        this.mapDmucDvi = mapDmucDvi;
        if (isNewValue && !DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
    }

    @Transient
    private List<XhDxKhBanTrucTiepDdiem> children = new ArrayList<>();
}