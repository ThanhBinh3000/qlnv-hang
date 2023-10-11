package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = XhCtvtQuyetDinhPdDtl.TABLE_NAME)
@Getter
@Setter
public class XhCtvtQuyetDinhPdDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_PD_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtQuyetDinhPdDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtQuyetDinhPdDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhCtvtQuyetDinhPdDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idDx;
  private String soDx;
  private LocalDate ngayKyDx;
  private String trichYeuDx;
  private BigDecimal soLuongDx;
  private String loaiHinhNhapXuat;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private String noiDungDx;
  private String loaiVthh;
  private String cloaiVthh;
  private String maDvi;
  private BigDecimal soLuong;
  private BigDecimal soLuongXc;
  private BigDecimal soLuongNhuCauXuat;
  private BigDecimal tonKhoDvi;
  private BigDecimal tonKhoLoaiVthh;
  private BigDecimal tonKhoCloaiVthh;
  private String donViTinh;
  private Long idQdGnv;
  private String soQdGnv;
  private LocalDate ngayKetThuc;
  @JsonIgnore
  @Transient
  private Map<String, String> mapVthh;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi = new ArrayMap<>();
  @Transient
  private String tenDvi;

  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    this.mapDmucDvi = mapDmucDvi;
    String tenDvi = mapDmucDvi.containsKey(maDvi) ? mapDmucDvi.get(maDvi) : null;
    setTenDvi(tenDvi);
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhCtvtQuyetDinhPdHdr xhCtvtQuyetDinhPdHdr;
}
