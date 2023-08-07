package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhPhieuKnclHdr.TABLE_NAME)
@Getter
@Setter
public class XhPhieuKnclHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_PHIEU_KNCL_HDR";

  //phan chung
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhPhieuKnclHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhPhieuKnclHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhPhieuKnclHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soBbQd;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private String lyDoTuChoi;
  private String trangThai;
  private Long nguoiKyQdId;
  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  @JsonIgnore
  @Transient
  private Map<String, Map<String, Object>> mapVthh;
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
  @Transient
  private String donViTinh;

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

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }


  //phan rieng
  private String maQhns;
  private Long idQdGnv;
  private String soQdGnv;
  private LocalDate ngayKyQdGnv;
  private Long idBbLayMau;
  private String soBbLayMau;
  private LocalDate ngayBbLayMau;
  private String soBbTinhKho;
  private LocalDate ngayBbTinhKho;
  private LocalDate ngayKiemNghiem;
  private String ktvBaoQuan;
  private String dviKiemNghiem;
  private String ketQua;
  private String ketLuan;
  private String loaiBb;
  private String type;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhPhieuKnclHdr.TABLE_NAME + "_DINH_KEM'")
  private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();

  public void setFileDinhKem(List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem) {
    this.fileDinhKem.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhPhieuKnclHdr.TABLE_NAME + "_DINH_KEM");
        s.setXhPhieuKnclHdr(this);
      });
      this.fileDinhKem.addAll(fileDinhKem);
    }
  }

  @OneToMany(mappedBy = "xhPhieuKnclHdr", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<XhPhieuKnclDtl> xhPhieuKnclDtl = new ArrayList<>();

  public void setXhPhieuKnclDtl(List<XhPhieuKnclDtl> xhPhieuKnclDtl) {
    this.getXhPhieuKnclDtl().clear();
    if (!DataUtils.isNullOrEmpty(xhPhieuKnclDtl)) {
      xhPhieuKnclDtl.forEach(s -> {
        s.setXhPhieuKnclHdr(this);
      });
      this.xhPhieuKnclDtl.addAll(xhPhieuKnclDtl);
    }
  }
}
