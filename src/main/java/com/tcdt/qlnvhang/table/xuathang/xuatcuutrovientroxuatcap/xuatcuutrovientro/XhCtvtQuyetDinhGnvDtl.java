package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = XhCtvtQuyetDinhGnvDtl.TABLE_NAME)
@Getter
@Setter


public class XhCtvtQuyetDinhGnvDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_GNV_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtQuyetDinhGnvDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtQuyetDinhGnvDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhCtvtQuyetDinhGnvDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idDx;
  private BigDecimal soLuongDx;
  private BigDecimal soLuongGiao;
  private String loaiHinhNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private String noiDungDx;
  private String idDonViNhan;
  private String loaiVthh;
  private String cloaiVthh;
  private String maDvi;
  private BigDecimal soLuong;
  private BigDecimal tonKhoDvi;
  private BigDecimal tonKhoLoaiVthh;
  private BigDecimal tonKhoCloaiVthh;
  private String trangThai;
  @Column(name = "TY_LE_THU_HOI_SAU_XAY_XAT")
  private BigDecimal tyLeThuHoiSauXayXat;
  @Column(name = "SL_THU_HOI_SAU_XAY_XAT")
  private BigDecimal slGaoThuHoiSauXayXat;
  @Column(name = "SL_THOC_DE_XAY_XAT")
  private BigDecimal slThocDeXayXat;
  private Integer namNhap;
  @JsonIgnore
  @Transient
  private Map<String, Map<String, Object>> mapVthh;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String donViTinh;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi = new ArrayMap<>();
  @Transient
  private String tenDvi;
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
  private String tenTrangThai;
  @Transient
  private Long idQdPdDtl;

  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    this.mapDmucDvi = mapDmucDvi;
    if (!DataUtils.isNullObject(getMaDvi())) {
      String maCuc = getMaDvi().length() >= 6 ? getMaDvi().substring(0, 6) : "";
      String maChiCuc = getMaDvi().length() >= 8 ? getMaDvi().substring(0, 8) : "";
      String maDiemKho = getMaDvi().length() >= 10 ? getMaDvi().substring(0, 10) : "";
      String maNhaKho = getMaDvi().length() >= 12 ? getMaDvi().substring(0, 12) : "";
      String maNganKho = getMaDvi().length() >= 14 ? getMaDvi().substring(0, 14) : "";
      String maLoKho = getMaDvi().length() >= 16 ? getMaDvi().substring(0, 16) : "";
      String tenCuc = mapDmucDvi.containsKey(maCuc) ? mapDmucDvi.get(maCuc) : null;
      String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
      String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
      String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
      String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
      String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
      String tenDvi = mapDmucDvi.containsKey(maDvi) ? mapDmucDvi.get(maDvi) : null;
      setTenCuc(tenCuc);
      setTenChiCuc(tenChiCuc);
      setTenDiemKho(tenDiemKho);
      setTenNhaKho(tenNhaKho);
      setTenNganKho(tenNganKho);
      setTenLoKho(tenLoKho);
      setTenDvi(tenDvi);
    }
  }

  public void setMapVthh(Map<String, Map<String, Object>> mapVthh) {
    this.mapVthh = mapVthh;
    if (mapVthh != null) {
      if (mapVthh.containsKey(getLoaiVthh())) {
        setTenLoaiVthh(DataUtils.safeToString(mapVthh.get(getLoaiVthh()).get("ten")));
        setDonViTinh(DataUtils.safeToString(mapVthh.get(getLoaiVthh()).get("maDviTinh")));
      }
      if (mapVthh.containsKey(getCloaiVthh())) {
        setTenCloaiVthh(DataUtils.safeToString(mapVthh.get(getCloaiVthh()).get("ten")));
        setDonViTinh(DataUtils.safeToString(mapVthh.get(getLoaiVthh()).get("maDviTinh")));
      }
    }
  }


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhCtvtQuyetDinhGnvHdr xhCtvtQuyetDinhGnvHdr;

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }
}
