package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhHoSoKyThuatDtl.TABLE_NAME)
@Getter
@Setter
public class XhHoSoKyThuatDtl extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_HO_SO_KY_THUAT_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhHoSoKyThuatDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhHoSoKyThuatDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhHoSoKyThuatDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String soBienBan;
  private LocalDate ngayLapBb;
  private String soHskt;
  private String maDviNhapHskt;
  private LocalDate ngayTaoHskt;
  private Long idBbLayMau;
  private String soBbLayMau;
  private String soQdGiaoNvNh;
  private Long idQdGiaoNvNh;
  private String dviCungCap;
  private LocalDate tgianKtra;
  private String kquaKtra;
  private String diaDiemKtra;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long soLuongNhap;
  private LocalDate tgianNhap;
  private String ppLayMau;
  private String kyMaHieu;
  private String soSerial;
  private String noiDung;
  private String ketLuan;
  private String trangThai;

  //  BB_KTRA_NGOAI_QUAN = "BBKTNQ",
  //  BB_KTRA_VAN_HANH = "BBKTVH",
  //  BB_KTRA_HOSO_KYTHUAT = "BBKTHSKT",
  private String loaiBb;
  private String thoiDiemLap;
  @JsonIgnore
  @Transient
  private Map<String, String> mapVthh;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  private LocalDate tgianBsung;

  @OneToMany(mappedBy = "xhHoSoKyThuatDtl", cascade = CascadeType.ALL)
  private List<XhHoSoKyThuatRow> xhHoSoKyThuatRow = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhHoSoKyThuatHdr xhHoSoKyThuatHdr;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhHoSoKyThuatDtl.TABLE_NAME + "_DINH_KEM'")
  private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();

  public void setFileDinhKem(List<FileDKemJoinHoSoKyThuatDtl> children) {
    this.fileDinhKem.clear();
    for (FileDKemJoinHoSoKyThuatDtl child : children) {
      child.setDataType(XhHoSoKyThuatDtl.TABLE_NAME + "_DINH_KEM");
      child.setParent(this);
    }
    this.fileDinhKem.addAll(children);
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhHoSoKyThuatDtl.TABLE_NAME + "_CAN_CU'")
  private List<FileDKemJoinHoSoKyThuatDtl> canCu = new ArrayList<>();

  public void setCanCu(List<FileDKemJoinHoSoKyThuatDtl> children) {
    this.canCu.clear();
    for (FileDKemJoinHoSoKyThuatDtl child : children) {
      child.setDataType(XhHoSoKyThuatDtl.TABLE_NAME + "_CAN_CU");
      child.setParent(this);
    }
    this.fileDinhKem.addAll(children);
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhHoSoKyThuatDtl.TABLE_NAME + "_VAN_BAN_BSUNG'")
  private List<FileDKemJoinHoSoKyThuatDtl> vanBanBsung = new ArrayList<>();

  public void setVanBanBsung(List<FileDKemJoinHoSoKyThuatDtl> children) {
    this.vanBanBsung.clear();
    for (FileDKemJoinHoSoKyThuatDtl child : children) {
      child.setDataType(XhHoSoKyThuatDtl.TABLE_NAME + "_VAN_BAN_BSUNG");
      child.setParent(this);
    }
    this.vanBanBsung.addAll(children);
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
