package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "XH_HOP_DONG_BTT_DVI")
@Data
public class XhHopDongBttDvi {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_DVI";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private Long idDtl;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuongDeXuat;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaDuocDuyet;
    private BigDecimal thanhTien;
    private BigDecimal soLuongKyHd;
    private BigDecimal donGiaKyHd;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;

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
}