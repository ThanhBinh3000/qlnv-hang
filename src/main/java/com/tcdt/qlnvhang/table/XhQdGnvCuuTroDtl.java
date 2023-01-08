package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = XhQdGnvCuuTroDtl.TABLE_NAME)
@Data
public class XhQdGnvCuuTroDtl extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_GNV_CUU_TRO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdGnvCuuTroDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhQdGnvCuuTroDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdGnvCuuTroDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private String noiDung;
  private String maDvi;
  private String maDiaDiem;
  private String tenCloaiVthh;
  private BigDecimal tongTichLuong;
  private BigDecimal tonKho;
  private BigDecimal soLuongNoiDung;
  private BigDecimal soLuongGiao;
  private BigDecimal soLuongXuat;
  private BigDecimal soLuong;
  private String donViTinh;
  private String trangThai;

  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi;
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
      String maChiCuc = getMaDiaDiem().length() >= 8 ? getMaDiaDiem().substring(0, 8) : "";
      String maDiemKho = getMaDiaDiem().length() >= 10 ? getMaDiaDiem().substring(0, 10) : "";
      String maNhaKho = getMaDiaDiem().length() >= 12 ? getMaDiaDiem().substring(0, 12) : "";
      String maNganKho = getMaDiaDiem().length() >= 14 ? getMaDiaDiem().substring(0, 14) : "";
      String maLoKho = getMaDiaDiem().length() >= 16 ? getMaDiaDiem().substring(0, 16) : "";
      String tenDvi = mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null;
      String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
      String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
      String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
      String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
      String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
      setTenChiCuc(tenChiCuc);
      setTenDiemKho(tenDiemKho);
      setTenNhaKho(tenNhaKho);
      setTenNganKho(tenNganKho);
      setTenLoKho(tenLoKho);
    }
  }
}
