package com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhQdGiaoNvXhDtl.TABLE_NAME)
@Data
public class XhQdGiaoNvXhDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NV_XH_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdGiaoNvXhDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdGiaoNvXhDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdGiaoNvXhDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal tonKho;
    private BigDecimal soLuongXuatBan;
    private String trangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;

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

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @Transient
    private List<XhQdGiaoNvXhDdiem> children = new ArrayList<>();
}