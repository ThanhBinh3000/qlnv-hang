package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

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
@Table(name = XhQdPdKhBttDvi.TABLE_NAME)
@Data
public class XhQdPdKhBttDvi implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_DVI";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBttDvi.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBttDvi.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBttDvi.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idDtl;
    private String maDvi;
    private BigDecimal soLuongChiCuc;
    private String diaChi;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDaDuyet;
    private String donViTinh;
    private BigDecimal thanhTien;
    private BigDecimal tienDuocDuyet;

    @Transient
    private String tenDvi;

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
    List<XhQdPdKhBttDviDtl> children = new ArrayList<>();
    //Print preview
    @Transient
    private BigDecimal donGiaDuocDuyet;
}