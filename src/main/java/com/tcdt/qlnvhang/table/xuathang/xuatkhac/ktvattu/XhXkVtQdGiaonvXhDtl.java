package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = XhXkVtQdGiaonvXhDtl.TABLE_NAME)
public class XhXkVtQdGiaonvXhDtl {

    public static final String TABLE_NAME = "XH_XK_VT_QD_GIAONV_XH_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_VT_QD_GIAONV_XH_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_VT_QD_GIAONV_XH_DTL_SEQ", allocationSize = 1, name = "XH_XK_VT_QD_GIAONV_XH_DTL_SEQ")
    Long id;
    String maDiaDiem;
    String loaiVthh;
    String donViTinh;
    String cloaiVthh;
    BigDecimal slLayMau;
    BigDecimal slTonKho;
    BigDecimal slXuatGiam;
    String maDviTsan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhXkVtQdGiaonvXhHdr xhXkVtQdGiaonvXhHdr;

    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;
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

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDiaDiem())) {
            String maCuc = getMaDiaDiem().length() >= 6 ? getMaDiaDiem().substring(0, 6) : "";
            String maChiCuc = getMaDiaDiem().length() >= 8 ? getMaDiaDiem().substring(0, 8) : "";
            String maDiemKho = getMaDiaDiem().length() >= 10 ? getMaDiaDiem().substring(0, 10) : "";
            String maNhaKho = getMaDiaDiem().length() >= 12 ? getMaDiaDiem().substring(0, 12) : "";
            String maNganKho = getMaDiaDiem().length() >= 14 ? getMaDiaDiem().substring(0, 14) : "";
            String maLoKho = getMaDiaDiem().length() >= 16 ? getMaDiaDiem().substring(0, 16) : "";
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

    public void setMapVthh(Map<String, String> mapVthh) {
        this.mapVthh = mapVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
        }
    }

    public BigDecimal getSlLayMau() {
        return !ObjectUtils.isEmpty(slLayMau) ? slLayMau : BigDecimal.ZERO;
    }

    public void setSlLayMau(BigDecimal slLayMau) {
        this.slLayMau = !ObjectUtils.isEmpty(slLayMau) ? slLayMau : BigDecimal.ZERO;
    }

    public BigDecimal getSlTonKho() {
        return !ObjectUtils.isEmpty(slTonKho) ? slTonKho : BigDecimal.ZERO;
    }

    public void setSlTonKho(BigDecimal slTonKho) {
        this.slTonKho = !ObjectUtils.isEmpty(slTonKho) ? slTonKho : BigDecimal.ZERO;
    }

    public BigDecimal getSlXuatGiam() {
        return !ObjectUtils.isEmpty(slXuatGiam) ? slXuatGiam : BigDecimal.ZERO;
    }

    public void setSlXuatGiam(BigDecimal slXuatGiam) {
        this.slXuatGiam = !ObjectUtils.isEmpty(slXuatGiam) ? slXuatGiam : BigDecimal.ZERO;
    }
}
