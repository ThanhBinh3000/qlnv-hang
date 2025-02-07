package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhHoSoKyThuatHdr.TABLE_NAME)
@Data
public class XhHoSoKyThuatHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_HO_SO_KY_THUAT_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhHoSoKyThuatHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhHoSoKyThuatHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhHoSoKyThuatHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String soHskt;
  private Long idHsktNh;
  private String soHsktNh;
  private Long idBbLayMau;
  private String soBbLayMau;
  private String soBbLayMauNh;
  private String soQdGiaoNvNh;
  private String soBbKtNgoaiQuan;
  private String soBbKtVanHanh;
  private String soBbKtHskt;
  private LocalDate ngayTaoNh;
  private LocalDate ngayDuyetNh;
  private String maDvi;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String lyDo;
  private String trangThai;
  private String kqKiemTra;
  private String type;
  @OneToMany(mappedBy = "xhHoSoKyThuatHdr", cascade = CascadeType.ALL)
  private List<XhHoSoKyThuatDtl> xhHoSoKyThuatDtl = new ArrayList<>();

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

  private String canBoTaoHoSo;
  private String canBoTaoHoSoNh;

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

  public void setMapVthh(Map<String, String> mapVthh) {
    this.mapVthh = mapVthh;
    if (!DataUtils.isNullObject(getLoaiVthh())) {
      setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
    }
    if (!DataUtils.isNullObject(getCloaiVthh())) {
      setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
    }
  }

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }
}
