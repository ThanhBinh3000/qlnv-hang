package com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = XhXkDanhSachHdr.TABLE_NAME)
@Data
public class XhXkDanhSachHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_DANH_SACH_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkDanhSachHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkDanhSachHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkDanhSachHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Long idTongHop;
    private String maTongHop;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private BigDecimal slHetHan;
    private BigDecimal slTonKho;
    private Integer thoiHanLk;
    private LocalDate ngayNhapKho;
    private LocalDate ngayHetHanBh;
    private String bienPhapXuLy;
    private LocalDate ngayDeXuat;
    private LocalDateTime ngayTongHop;
    private String lyDo;
    private String trangThai;
    private String loai;
    private Long idTongHopTc;
    private String maTongHopTc;
    private LocalDateTime ngayTongHopTc;


    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
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
    private Integer namNhap;
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

    public void setSlHetHan(BigDecimal slHetHan) {
        this.slHetHan = ObjectUtils.isEmpty(slHetHan) ? BigDecimal.ZERO : slHetHan;
    }

    public void setSlTonKho(BigDecimal slTonKho) {
        this.slTonKho = ObjectUtils.isEmpty(slTonKho) ? BigDecimal.ZERO : slTonKho;
    }
}
