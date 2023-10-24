package com.tcdt.qlnvhang.table.chotdulieu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = QthtKetChuyenDtl.TABLE_NAME)
public class QthtKetChuyenDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "QTHT_KET_CHUYEN_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = QthtKetChuyenDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = QthtKetChuyenDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = QthtKetChuyenDtl.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private String maDonVi;

    private String cloaiVthh;

    private String loaiVthh;

    private BigDecimal slHienThoi;

    private String tenDonViTinh;

    @Transient
    private String tenCuc;
    @Transient
    private String tenChiCuc;
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
    @Transient
    private Integer nam;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDonVi())) {
            String maCuc = getMaDonVi().length() >= 6 ? getMaDonVi().substring(0, 6) : "";
            String maChiCuc = getMaDonVi().length() >= 8 ? getMaDonVi().substring(0, 8) : "";
            String maDiemKho = getMaDonVi().length() >= 10 ? getMaDonVi().substring(0, 10) : "";
            String maNhaKho = getMaDonVi().length() >= 12 ? getMaDonVi().substring(0, 12) : "";
            String maNganKho = getMaDonVi().length() >= 14 ? getMaDonVi().substring(0, 14) : "";
            String maLoKho = getMaDonVi().length() >= 16 ? getMaDonVi().substring(0, 16) : "";
            String tenCuc = mapDmucDvi.containsKey(maCuc) ? mapDmucDvi.get(maCuc) : null;
            String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
            String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
            String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
            String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
            String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
            setTenCuc(tenCuc);
            setTenChiCuc(tenChiCuc);
            setTenDiemKho(tenDiemKho);
            setTenNhaKho(tenNhaKho);
            setTenNganKho(tenNganKho);
            setTenLoKho(tenLoKho);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
    public void setMapVthh(Map<String, String> mapVthh) {
        this.mapVthh = mapVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
        }
    }

}
