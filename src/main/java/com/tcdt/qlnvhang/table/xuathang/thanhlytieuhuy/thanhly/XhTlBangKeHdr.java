package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhTlBangKeHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhTlBangKeHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BANG_KE_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBangKeHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = XhTlBangKeHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBangKeHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayLap;
    private String soQdXh;
    private Long idQdXh;
    private String ngayQdXh;
    private String soPhieuXuatKho;
    private Long idPhieuXuatKho;
    private String diaDiemKho;
    private String maDiaDiem;
    private Long idDsHdr;
    private String nguoiGiamSat;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String lyDoTuChoi;
    private String trangThai;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private BigDecimal tongTrongLuong;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoCc;
    @Transient
    private List<XhTlBangKeDtl> children = new ArrayList<>();

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }

    @Transient
    private XhTlPhieuXuatKhoHdr xhTlPhieuXuatKhoHdr;

    // Print preview
    @Transient
    private String ngay;
    @Transient
    private String thang;
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

    public String getNgay() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getDayOfMonth());
    }
    public String getThang() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getMonthValue());
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

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
