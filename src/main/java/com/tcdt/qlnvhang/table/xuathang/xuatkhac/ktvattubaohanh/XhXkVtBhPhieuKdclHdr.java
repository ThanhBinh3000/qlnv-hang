package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhXkVtBhPhieuKdclHdr.TABLE_NAME)
@Data
public class XhXkVtBhPhieuKdclHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_VT_BH_PHIEU_KDCL_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhPhieuKdclHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkVtBhPhieuKdclHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXkVtBhPhieuKdclHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private Long idQdGiaoNvNh;
  private String soQdGiaoNvNh;
  private LocalDate ngayQdGiaoNvXh;
  private Integer soLanLm;
  private Long idBbLayMau;
  private String soBbLayMau;
  private LocalDate ngayLayMau;
  private Integer soLuongMau;
  private Integer slTonKho;
  private String soPhieu;
  private LocalDate ngayLapPhieu;
  private LocalDate ngayKiemDinh;
  private String dviKiemDinh;
  private String loaiVthh;
  private String cloaiVthh;
  private String donViTinh;
  private String maDiaDiem;
  private String ppLayMau;
  private String nhanXetKetLuan;
  private Boolean isDat;
  private Boolean mauBiHuy;
  private String trangThai;
  private String canBoTao;
  private String thuKho;
  private String tpKtbq;
  private String lanhDaoCuc;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;

  private Long idBbBaoHanh;
  private String soBbBaoHanh;

  @OneToMany(mappedBy = "phieuKdclHdr", cascade = CascadeType.ALL)
  private List<XhXkVtBhPhieuKdclDtl> phieuKdclDtl = new ArrayList<>();

  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
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
  private String tenThuKho;
  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi;
  @JsonIgnore
  @Transient
  private Map<String, String> mapVthh;

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
