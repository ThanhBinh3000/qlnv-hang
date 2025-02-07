package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhQdPdKhBdgDtl.TABLE_NAME)
@Data
public class XhQdPdKhBdgDtl implements Serializable {
  public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_DTL";
  private static final long serialVersionUID = 1L;
  @Transient
  List<XhTcTtinBdgHdr> listTtinDg = new ArrayList<>();
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
  private String trangThaiNhapLieu;
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
  @Transient
  private String tenTrangThaiNhapLieu;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucVthh;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucThanhToan;
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

  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    boolean isNewValue = !Objects.equals(this.mapDmucDvi, mapDmucDvi);
    this.mapDmucDvi = mapDmucDvi;
    if (isNewValue && !DataUtils.isNullObject(getMaDvi())) {
      setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
    }
  }

  public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
    boolean isNewValue = !Objects.equals(this.mapDmucVthh, mapDmucVthh);
    this.mapDmucVthh = mapDmucVthh;
    if (isNewValue && !DataUtils.isNullObject(getLoaiVthh())) {
      setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
    }
    if (isNewValue && !DataUtils.isNullObject(getCloaiVthh())) {
      setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
    }
  }

  public void setMapDmucThanhToan(Map<String, String> mapDmucThanhToan) {
    boolean isNewValue = !Objects.equals(this.mapDmucThanhToan, mapDmucThanhToan);
    this.mapDmucThanhToan = mapDmucThanhToan;
    if (isNewValue && !DataUtils.isNullObject(getPthucTtoan())) {
      setTenPthucTtoan(mapDmucThanhToan.getOrDefault(getPthucTtoan(), null));
    }
  }

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }

  public String getTrangThaiNhapLieu() {
    if (DataUtils.safeToString(trangThaiNhapLieu).equals(Contains.TRANG_THAI_NHAP_LIEU.DANG_NHAP)) {
      setTenTrangThaiNhapLieu("Đang nhập");
    } else if (DataUtils.safeToString(trangThaiNhapLieu).equals(Contains.TRANG_THAI_NHAP_LIEU.HOAN_THANH)) {
      setTenTrangThaiNhapLieu("Hoàn thành");
    }
    return trangThaiNhapLieu;
  }
}