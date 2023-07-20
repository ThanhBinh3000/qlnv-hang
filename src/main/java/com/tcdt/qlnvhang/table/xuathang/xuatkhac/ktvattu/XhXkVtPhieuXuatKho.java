package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = XhXkVtPhieuXuatKho.TABLE_NAME)
public class XhXkVtPhieuXuatKho extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_VT_PHIEU_XUAT_KHO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_VT_PHIEU_XUAT_KHO_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_VT_PHIEU_XUAT_KHO_SEQ", allocationSize = 1, name = "XH_XK_VT_PHIEU_XUAT_KHO_SEQ")
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String soPhieu;
    private String loai;
    private String maQhns;
    private LocalDate ngayXuat;
    private LocalDate thoiGianGiaoHang;
    private BigDecimal duNo;
    private BigDecimal duCo;
    private String soCanCu;
    private Long idCanCu;
    private String maDiaDiem; // mã địa điểm (mã lô/ngăn kho)
    private String soPhieuKncl;
    private Long idPhieuKncl;
    private String loaiVthh;
    private String cloaiVthh;
    private String canBoLapPhieu;
    private String ldChiCuc;
    private String ktvBaoQuan;
    private String keToanTruong;
    private String hoTenNgh;
    private String cccdNgh;
    private String donViNgh;
    private String diaChiNgh;
    private BigDecimal slThucXuat;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private String maSo;
    private BigDecimal slTonKho;
    private BigDecimal slLayMau;
    private String donViTinh;

    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoai;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
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
    @Transient
    private Boolean ketQuaKiemDinh;
    @Transient
    private String tenTrangThaiXhQdGiaoNvXh;

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
}
