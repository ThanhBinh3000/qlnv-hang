package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhDxKhBanTrucTiepDdiem.TABLE_NAME)
@Data
public class XhDxKhBanTrucTiepDdiem implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_TRUC_TIEP_DDIEM ";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_TRUC_TIEP_DD_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_TRUC_TIEP_DD_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_TRUC_TIEP_DD_SEQ")
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuongDeXuat;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal thanhTienDeXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private Integer namNhap;
    @Transient
    private BigDecimal donGiaDuocDuyet;
    @Transient
    private BigDecimal thanhTienDuocDuyet;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        boolean isNewValue = !Objects.equals(this.mapDmucDvi, mapDmucDvi);
        this.mapDmucDvi = mapDmucDvi;
        if (isNewValue && !DataUtils.isNullObject(getMaDiemKho())) {
            setTenDiemKho(mapDmucDvi.getOrDefault(getMaDiemKho(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getMaNhaKho())) {
            setTenNhaKho(mapDmucDvi.getOrDefault(getMaNhaKho(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getMaNganKho())) {
            setTenNganKho(mapDmucDvi.getOrDefault(getMaNganKho(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getMaLoKho())) {
            setTenLoKho(mapDmucDvi.getOrDefault(getMaLoKho(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucVthh;

    public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
        boolean isNewValue = !Objects.equals(this.mapDmucVthh, mapDmucVthh);
        this.mapDmucVthh = mapDmucVthh;
        if (isNewValue && !DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
        }
    }
}