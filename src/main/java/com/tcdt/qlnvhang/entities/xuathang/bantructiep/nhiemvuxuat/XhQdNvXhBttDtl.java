package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhQdNvXhBttDtl.TABLE_NAME)
@Data
public class XhQdNvXhBttDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdNvXhBttDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdNvXhBttDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdNvXhBttDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private BigDecimal soLuong;
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
    private List<XhQdNvXhBttDvi> children = new ArrayList<>();
}