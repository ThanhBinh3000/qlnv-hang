package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.bangkebanle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = XhBangKeBtt.TABLE_NAME)
@Data
public class XhBangKeBtt implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BANG_KE_BTT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBangKeBtt.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhBangKeBtt.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBangKeBtt.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String soBangKe;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private BigDecimal slXuatBanQdPd;
    private BigDecimal soLuongConLai;
    private String nguoiPhuTrach;
    private String diaChi;
    private LocalDate ngayBanHang;
    private String loaiVthh;
    private String cloaiVthh;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String tenBenMua;
    private String diaChiBenMua;
    private String cmtBenMua;
    private String ghiChu;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
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