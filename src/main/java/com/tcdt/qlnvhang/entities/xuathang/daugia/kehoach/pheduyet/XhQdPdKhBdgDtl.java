package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhQdPdKhBdgDtl.TABLE_NAME)
@Data
public class XhQdPdKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBdgDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBdgDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBdgDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private Integer nam;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private String loaiHopDong;
    private Integer thoiGianKyHdong;
    private String thoiGianKyHdongGhiChu;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private String donViTinh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private BigDecimal tongTienKhoiDiemDx;
    private BigDecimal tongTienDatTruocDx;
    private BigDecimal tongTienDuocDuyet;
    private BigDecimal tongKtienDtruocDduyet;
    private String trangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenPthucTtoan;
    @Transient
    private String tenTrangThai;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucVthh;

    public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
        this.mapDmucVthh = mapDmucVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucThanhToan;

    public void setMapDmucThanhToan(Map<String, String> mapDmucThanhToan) {
        this.mapDmucThanhToan = mapDmucThanhToan;
        if (!DataUtils.isNullObject(getPthucTtoan())) {
            setTenPthucTtoan(mapDmucThanhToan.getOrDefault(getPthucTtoan(), null));
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @Transient
    private List<XhQdPdKhBdgPl> children = new ArrayList<>();
    //Thông tin đấu giá
    private Long idQdKq;
    private String soQdKq;
    private LocalDate ngayKyQdKq;
    private String soQdDc;
    private String soQdPd;
    private BigDecimal soDviTsanThanhCong;
    private BigDecimal soDviTsanKhongThanh;
    private String ketQuaDauGia;
    @Transient
    private XhQdPdKhBdg xhQdPdKhBdg;
    @Transient
    List<XhTcTtinBdgHdr> listTtinDg = new ArrayList<>();
}